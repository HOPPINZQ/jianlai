package com.hoppinzq.service.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.aop.Self;
import com.hoppinzq.service.aop.annotation.*;
import com.hoppinzq.service.bean.*;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.dao.BlogDao;

import com.hoppinzq.service.dao.BlogLogDao;
import com.hoppinzq.service.interfaceService.CSDNService;
import com.hoppinzq.service.interfaceService.CutWordService;
import com.hoppinzq.service.util.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@ApiServiceMapping(title = "博客服务", description = "博客服务，已加入网关",roleType = ApiServiceMapping.RoleType.RIGHT)
public class BlogService implements Callable<Object> {
    @Autowired
    private BlogDao blogDao;

    @Autowired
    BlogLogDao logDao;

    @Autowired
    private RedisUtils redisUtils;

    private BlogService blogService;
    private BlogVo blogVo;

    public BlogService() {}

    /**
     * 为多线程创建的服务实例的构造方法
     * 你知道为什么要传入dao的实例对象吗？
     * 因为new Thread不在spring容器中，无法获得spring中的bean对象。
     * 因此需要（选一种，我用的是第一种） ：1、通过构造器传入已经实例化好的对象。
     * 2、线程内部手动获取applicationContext，进而获取bean
     * 3、使用@Scope(“prototype“)注解，这个注解是：在注入Bean时不采用Spring默认的单例模式，而是每次新创建一个对象
     * @param blogVo
     * @param blogDao dao实例
     */
    public BlogService(BlogVo blogVo,BlogDao blogDao) {
        this.blogVo = blogVo;
        this.blogDao=blogDao;
    }

    @Autowired
    private RPCPropertyBean rpcPropertyBean;
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Value("${zqServiceWebSpider.addr:http:127.0.0.1:8806/service}")
    private String zqServiceWebSpiderAddr;
    @Value("${zqServiceExtra.addr:http:127.0.0.1:8806/service}")
    private String zqServiceExtraAddr;

    @Value("${zqClient.authAddr:http:127.0.0.1:8804/service}")
    private String authServiceAddr;

    @Value("${lucene.indexPath:D:\\index}")
    private String indexPath;

    public final static Integer PAGE_SIZE = 16;

    @Self
    public void setSelf(BlogService blogService) {
        this.blogService = blogService;
    }

    private static final String blog2RedisKeyPrefix="BLOG:";
    private static final String blog2RedisBlogId=blog2RedisKeyPrefix+"BLOG_ID:";
    private static final String blog2RedisBlogClass=blog2RedisKeyPrefix+"BLOG_CLASS:";

    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

    /**
     * 博客雪花ID生成，在进入写博客页面就生成，方便缓存草稿
     * @return
     */
    @ApiMapping(value = "createBlogId", title = "生成博客ID")
    public long createBlogId(){
        return snowflakeIdWorker.getSequenceId();
    }
    /**
     * 草稿保存进redis
     * 流程，前端每30s调用一次该接口，从blog的text取到前端处理好的数据，然后清空一些内容，减少对数据库的压力
     * 1、第一次进入该接口，redis肯定没有该id的草稿,异步将草稿存入数据库内
     * 2、若redis有该id的草稿，覆盖之，返回ID
     * @return
     */
    @ApiMapping(value = "saveBlog2Redis", title = "保存草稿", description = "每1min会调用一次接口保存博客内容进redis",roleType = ApiMapping.RoleType.LOGIN)
    public JSONObject saveBlog2Redis(Blog blog){
        long blogId=blog.getId();
        blog.decode();
        blog.deUnicode();
        JSONObject returnJSON = JSONUtil.createJSONObject("blogId",String.valueOf(blogId));
        JSONObject saveJSON=(JSONObject)redisUtils.get(blog2RedisBlogId+blogId);
        if(saveJSON==null){
            returnJSON.put("isNew",true);
            blog.setType(1);
            blogService.insertBlogAsync(blog);
            saveJSON=new JSONObject();
        }else{
            returnJSON.put("isNew",false);
        }
        saveJSON=JSONObject.parseObject(JSONObject.toJSONString(blog));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate=new Date();
        Long now=nowDate.getTime();
        saveJSON.put("lastUpdateTime",now);
        Boolean isRedis=redisUtils.set(blog2RedisBlogId+blogId,saveJSON);
        if(!isRedis){
            logger.error("草稿保存错误");
            throw new RuntimeException("草稿保存错误！");
        }
        returnJSON.put("lastUpdateTime",simpleDateFormat.format(nowDate));
        return returnJSON;
    }

    /**
     * 获取博客类别，先从springCache中获取，再从redis获取，兜底从数据库获取并放入redis中
     * 该项目启动后会先预热缓存，因此其实一开始redis就有类别了
     * 所以第一次请求走的redis，之后的请求直接走springCache，是二层缓存
     * springCache击穿到redis
     * @return
     */
    @Cacheable(value = "blogClass")
    @ApiMapping(value = "getBlogClass", title = "获取博客类别", description = "获取的是类别树，从redis里获取，找不到则兜底从数据库获取并存入redis")
    public JSONArray getBlogClass() {
        JSONArray blogClassArray=new JSONArray();
        Object redisBlogClass=redisUtils.get(blog2RedisBlogClass+"blogClass");
        if(redisBlogClass==null){
            List<Map> blogClassMap=blogDao.queryBlogClass();
            blogClassArray=JSONArray.parseArray(JSON.toJSONString(blogClassMap));
            redisUtils.set(blog2RedisBlogClass+"blogClass",blogClassArray);
        }else{
            blogClassArray=(JSONArray)redisBlogClass;
        }
        return blogClassArray;
    }

    /**
     * 异步新增博客(草稿)
     * @param blog
     */
    @Async
    public void insertBlogAsync(Blog blog) {
        try{
            blogDao.insertOrUpdateBlog(blog);
        }catch (Exception ex){
            throw new RuntimeException("新增博客失败:"+ex);
        }
    }

    /**
     * 异步收集用户搜索的内容，切分词后入库
     * 由于异步的操作是开辟一个额外的子线程，故是不能获取到父线程的ThreadLocal中的当前登录人，得通过传参传过来
     * @param search
     */
    @Async
    public void insertSearchKey(String search,JSONObject user) {
        List<String> searchs=new ArrayList<>();
        try{
            UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
            CutWordService cutWordService= ServiceProxyFactory.createProxy(CutWordService.class,zqServiceExtraAddr,upp);
            searchs=cutWordService.cut(search);
        }catch (Exception ex){
            logger.error("切词服务挂了");
            searchs.add(search);
        }
        String author="";
        if(user!=null){
            author=user.get("id").toString();
        }
        List<SearchKey> searchKeys=new ArrayList<>(searchs.size());
        for(String key:searchs){
            searchKeys.add(new SearchKey(search,key,author));
        }
        blogDao.insertSearchKeys(searchKeys);
    }

    /**
     * 异步记录日志
     * @param requestInfo
     */
    @Async
    public void insertLog(RequestInfo requestInfo){
        logDao.insertLog(requestInfo);
    }

    /**
     * 博客新增/更新草稿为正文
     * 索引库也添加一份
     * 在抛出异常处手动回滚事务
     * @param blog
     */
    @Transactional
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "insertBlog", title = "博客新增", description = "新增博客，有则加之",roleType = ApiMapping.RoleType.LOGIN)
    public void insertBlog(Blog blog) {
        blog.decode();
        blog.setType(0);
        blog.deUnicode();
        try{
            //新增/更新博客
            if(blog.getId()==0L){
                blog.setId(snowflakeIdWorker.getSequenceId());
                blogDao.insertOrUpdateBlog(blog);
            }else{
                blogDao.insertOrUpdateBlog(blog);
                redisUtils.del(blog2RedisBlogId+blog.getId());
            }
            //删除对应博客中间表数据
            blogDao.deleteBlogClassesById(blog.getId());
            //为中间表添加数据
            blogDao.insertBlogMidClassesById(blog.classList(),blog.getId());

            //索引库添加博客，注意这个update是将草稿转为正文
            Document document = new Document();
            document.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
            document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
            document.add(new TextField("description", blog.getDescription(), Field.Store.YES));
            document.add(new TextField("text", blog.getText(), Field.Store.YES));
            document.add(new IntPoint("like", blog.getBlogLike()));
            document.add(new StoredField("like", blog.getBlogLike()));
            document.add(new NumericDocValuesField("like", blog.getBlogLike()));
            document.add(new IntPoint("collect", blog.getCollect()));
            document.add(new StoredField("collect", blog.getCollect()));
            document.add(new NumericDocValuesField("collect", blog.getCollect()));
            document.add(new StoredField("image", blog.getImage()));
            document.add(new StoredField("isCreateSelf", blog.getIsCreateSelf()));
            document.add(new StringField("classId", blog.getBlogClass(), Field.Store.YES));
            document.add(new StringField("authorName", blog.getAuthorName(), Field.Store.YES));
            document.add(new LongPoint("time", blog.getUpdateTime().getTime()));
            document.add(new StoredField("time", blog.getUpdateTime().getTime()));
            document.add(new NumericDocValuesField("time", blog.getUpdateTime().getTime()));
            document.add(new TextField("className", blog.getBlogClassName(), Field.Store.YES));
            Analyzer analyzer = new IKAnalyzer();
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.addDocument(document);
            indexWriter.close();
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("新增博客失败:"+ex);
        }
    }

    /**
     * 新增博客会直接把springCache的博客类别删除，并删除redis的博客类别
     * @param blogName
     * @param parentId
     * @return
     */
    @CacheEvict(value = "blogClass")
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "insertBlogClass", title = "博客类别新增",roleType = ApiMapping.RoleType.LOGIN)
    public List<BlogClass> insertBlogClass(String blogName,long parentId) {
        JSONObject user= (JSONObject)LoginUser.getUserHold();
        String[] blogNames=blogName.split(",");
        List<BlogClass> blogClasses=new ArrayList<>();
        for(String name:blogNames){
            blogClasses.add(new BlogClass(snowflakeIdWorker.getSequenceId(),parentId,name, user.get("id").toString()));
        }
        blogDao.insertBlogClasses(blogClasses);
        redisUtils.del(blog2RedisBlogClass+"blogClass");
        return blogClasses;
    }

    /**
     * 额外开辟线程查询博客
     * @return
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        ResultModel<Blog> recentBlogs=this.queryBlog(blogVo);
        return recentBlogs;
    }

    /**
     * 首页展示一些特定规则的博客（10个最近，10个喜欢最多的，10个收藏最多的，受欢迎的类别的博客，根据用户画像查询的博客 todo）
     * 会在线程池开辟几个线程去分别查询需要的博客
     * 捕获每个线程抛出的异常，抛出异常的返回值以空集合替代之
     * 只有线程池所有的任务都执行完毕，才会返回查询结果
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP)
    @ApiMapping(value = "mainBlog", title = "首页展示博客", description = "规则：查最新发布的10篇，最受欢迎的10篇")
    public JSONObject mainBlog() throws ExecutionException, InterruptedException {
        JSONObject jsonObject=new JSONObject();
        ExecutorService executorService= Executors.newFixedThreadPool(3);//创建线程池
        BlogVo.BuilderBlogVo blogVo=BlogVo.newBuilder().searchType(0).blogReturn(1).pageSize(10).blogVo(null);//这个blogVo可不传，要是传了将被构建的覆盖
        Callable callableR = new BlogService(blogVo.order(1).bulid(),blogDao);
        Callable callableL = new BlogService(blogVo.order(2).bulid(),blogDao);
        Callable callableC = new BlogService(blogVo.order(3).bulid(),blogDao);
        //10条最近的
        Future<Object> recentFuture=executorService.submit(callableR);
        //再查10条最喜欢的
        Future<Object> likeFuture=executorService.submit(callableL);
        //10条收藏最多的
        Future<Object> collectFuture=executorService.submit(callableC);
        //获取结果,捕获异常将返回空集合
        try{
            ResultModel<Blog> recentBlogs=(ResultModel<Blog>)recentFuture.get();
            jsonObject.put("recentBlogs",recentBlogs.getList());
        }catch (Exception ex){
            jsonObject.put("recentBlogs",Collections.emptyList());
        }

        try{
            ResultModel<Blog> likeBlogs=(ResultModel<Blog>)likeFuture.get();
            jsonObject.put("likeBlogs",likeBlogs.getList());
        }catch (Exception ex){
            jsonObject.put("likeBlogs",Collections.emptyList());
        }

        try{
            ResultModel<Blog> collectBlogs=(ResultModel<Blog>)collectFuture.get();;
            jsonObject.put("collectBlogs",collectBlogs.getList());
        }catch (Exception ex){
            jsonObject.put("collectBlogs",Collections.emptyList());
        }

        //关闭服务
        executorService.shutdown();
        long t = System.currentTimeMillis();
        while (true) {
            // 判断线程池中任务是否全部执行完毕。若执行完毕，返回数据
            if (executorService.isTerminated()) {
                break;
            }
            //不是很优雅的超时机制，这里并不是死循环，而是只在10秒内循环20次，响应时间超过10s直接返回数据，无论线程池任务是否结束
            Thread.sleep(500);
            if (System.currentTimeMillis() - t >= 10499) {
                break;
            }
        }
        return jsonObject;
    }

    /**
     * 查询博客
     * 特殊传参：searchType为0表示走数据库，searchType为1表示走索引库
     * 传参有id的情况只走数据库，无论searchType是否是1，并且会将该id下的评论也会查询一部分
     * pageIndex为0表示不分页
     * order在数据库查询和索引库查询都会生效，具体看sql语句是怎么排序的
     * blogReturn为1表示只返回部分字段（因为有时候展示博客列表并不需要博客所有字段，这会导致响应体很大）
     * blogDetail为1表示查询非常完整的博客详情（blogReturn必须不为1）,此时若blogReturn为1也只会返回部分字段
     * search为走索引库的关键字，这个关键字会从以下字段匹配。👇
     * 走索引库会根据权值进行排序，title>authorName>description>className>text
     * 走索引库的查询条件 喜欢 跟 收藏 字段如果传范围必须为 x~y 格式 (此外：x<y)
     * @param blogVo
     * @return
     */
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP)
    @ApiMapping(value = "queryBlog", title = "查询博客", description = "查询所有博客，searchType为0表示走数据库，searchType为1表示走索引库，pageIndex为0表示不分页")
    public ResultModel<Blog> queryBlog(BlogVo blogVo) {
        int page=blogVo.getPageSize();
        if(page==0){
            blogVo.setPageSize(PAGE_SIZE);
        }

        List<Blog> blogs=new ArrayList<>();
        ResultModel<Blog> resultModel=new ResultModel<>();
        try{
            long blogId=blogVo.getId();
            //若查询参数传入id将强行走数据库
            if(blogId!=0){
                blogDao.updateShow(blogId);
                if(redisUtils.get(blog2RedisBlogId+blogId)!=null){
                    logger.debug("博客:"+blogId+"命中缓存");
                    blogs=(List<Blog>)redisUtils.get(blog2RedisBlogId+blogId);
                }else{
                    logger.debug("博客:"+blogId+"未命中缓存");
                    blogs=blogDao.queryBlog(blogVo);
                    redisUtils.set(blog2RedisBlogId+blogId,blogs);
                }
                if(blogs.size()==0){
                    resultModel.setList(Collections.EMPTY_LIST);
                    return resultModel;
                }
                if(blogs.get(0).getIsComment()==0&&blogVo.getBlogDetail()!=2){
                    CommentVo commentVo=new CommentVo();
                    commentVo.setComment_search_type(2);
                    commentVo.setComment_blogId(String.valueOf(blogVo.getId()));
                    List<Comment> comments=blogDao.queryComment(commentVo);
                    blogs.get(0).setBlogComment(comments);
                }
                resultModel.setRecordCount(1);
                resultModel.setPageCount(1);
            }else{
                //查询是走数据库还是索引库，走索引库无兜底策略
                if(blogVo.getSearchType()==0){
                    blogs=blogDao.queryBlog(blogVo);
                    int total=blogDao.countBlog(blogVo);
                    resultModel.setRecordCount(total);
                    int pageCount = total % PAGE_SIZE > 0 ? (total/PAGE_SIZE) + 1 : blogs.size()/PAGE_SIZE;
                    resultModel.setPageCount(pageCount);
                }else{
                    //todo 不分页暂未实现
                    int pageIndex= blogVo.getPageIndex();
                    resultModel.setCurPage(pageIndex);
                    int pageSize=blogVo.getPageSize()==0?PAGE_SIZE:blogVo.getPageSize();
                    Integer start =0;
                    Integer end = 0;
                    if(pageIndex!=0){
                        start = (blogVo.getPageIndex() - 1) * pageSize;
                        end = blogVo.getPageIndex() * pageSize;
                    }
                    Analyzer analyzer = new IKAnalyzer();
                    BooleanQuery.Builder query = new BooleanQuery.Builder();
                    if(StringUtil.isNotEmpty(blogVo.getSearch())){
                        JSONObject user= (JSONObject)LoginUser.getUserHold();
                        blogService.insertSearchKey(blogVo.getSearch(),user);
                        String[] fields = {"title","authorName", "description", "className","text"};
                        //设置影响排序的权重, 这里设置域的权重
                        Map<String, Float> boots = new HashMap<>();
                        boots.put("title", 1000000f);
                        boots.put("authorName", 100000f);
                        boots.put("description", 10000f);
                        boots.put("className", 1000f);
                        boots.put("text", 100f);
                        //从多个域查询对象
                        //query1 = queryParser.parse("*:*");
                        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields, analyzer, boots);
                        Query querySearch = multiFieldQueryParser.parse(blogVo.getSearch());
                        query.add(querySearch, BooleanClause.Occur.MUST);
                    }
                    if(StringUtil.isNotEmpty(blogVo.getTitle())){
                        QueryParser queryBlogTitleParser = new QueryParser("title", analyzer);
                        Query queryTitle = queryBlogTitleParser.parse(blogVo.getTitle());
                        query.add(queryTitle, BooleanClause.Occur.MUST);
                    }
                    if(StringUtil.isNotEmpty(blogVo.getBlog_likes())){
                        Query queryLike = IntPoint.newRangeQuery("like", blogVo.getBlog_likes()[0], blogVo.getBlog_likes()[1]);
                        query.add(queryLike, BooleanClause.Occur.MUST);
                    }
                    if(StringUtil.isNotEmpty(blogVo.getCollects())){
                        Query queryCollect = IntPoint.newRangeQuery("collect", blogVo.getCollects()[0], blogVo.getCollects()[1]);
                        query.add(queryCollect, BooleanClause.Occur.MUST);
                    }
                    if(StringUtil.isNotEmpty(blogVo.getDescription())){
                        QueryParser queryBlogDescriptionParser = new QueryParser("description", analyzer);
                        Query queryDescription = queryBlogDescriptionParser.parse(blogVo.getDescription());
                        query.add(queryDescription, BooleanClause.Occur.MUST);
                    }
                    if(StringUtil.isNotEmpty(blogVo.get_class_name())){
                        QueryParser queryBlogClassParser = new QueryParser("className", analyzer);
                        Query queryClass = queryBlogClassParser.parse(blogVo.get_class_name());
                        query.add(queryClass, BooleanClause.Occur.MUST);
                    }
                    Directory dir = FSDirectory.open(Paths.get(indexPath));
                    IndexReader indexReader = DirectoryReader.open(dir);
                    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
                    TopDocs topDocs;
                    //排序
                    int order=blogVo.getOrder();
                    switch (order){
                        case 0:
                        default:
                            topDocs = indexSearcher.search(query.build(),end);
                            break;
                        case 1:
                            //排序规则是首先根据updateDate来排序，然后再根据timee来排序，第二个参数表示该字段是什么类型，第三个字段表示排列顺序
                            topDocs = indexSearcher.search(query.build(),end,new Sort(new SortField("time", SortField.Type.LONG, true)));
                            break;
                        case -1:
                            topDocs = indexSearcher.search(query.build(),end,new Sort(new SortField("time", SortField.Type.LONG, false)));
                            break;
                        case 2:
                            topDocs = indexSearcher.search(query.build(),end,new Sort(new SortField("like", SortField.Type.LONG, true)));
                            break;
                        case -2:
                            topDocs = indexSearcher.search(query.build(),end,new Sort(new SortField("like", SortField.Type.LONG, false)));
                            break;
                        case 3:
                            topDocs = indexSearcher.search(query.build(),end,new Sort(new SortField("collect", SortField.Type.LONG, true)));
                            break;
                        case -3:
                            topDocs = indexSearcher.search(query.build(),end,new Sort(new SortField("collect", SortField.Type.LONG, false)));
                        break;
                    }

                    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                    if (scoreDocs != null) {
                        for (int i = start; i < end; i ++) {
                            if(start>topDocs.totalHits||topDocs.totalHits==i){
                                break;
                            }
                            //获取查询到的文档唯一标识, 文档id, 这个id是lucene在创建文档的时候自动分配的
                            int docID = scoreDocs[i].doc;
                            Document doc = indexReader.document(docID);
                            Blog blog;
                            if(blogVo.getBlogReturn()!=1){
                                blog=new Blog(Long.parseLong(doc.get("id")),doc.get("title"),doc.get("description"),doc.get("text"),
                                        Integer.parseInt(doc.get("like")),Integer.parseInt(doc.get("collect")),Long.parseLong(doc.get("time")),
                                        doc.get("authorName"),doc.get("classId"),doc.get("className"),doc.get("image"),Integer.parseInt(doc.get("isCreateSelf")));
                            }else{
                                blog=new Blog(Long.parseLong(doc.get("id")),doc.get("title"),doc.get("description"),
                                        Integer.parseInt(doc.get("like")),Integer.parseInt(doc.get("collect")),Long.parseLong(doc.get("time")),
                                        doc.get("authorName"),doc.get("classId"),doc.get("className"),doc.get("image"),Integer.parseInt(doc.get("isCreateSelf")));
                            }
                            blogs.add(blog);
                        }
                        int pageCount = (int)(topDocs.totalHits % pageSize > 0 ? (topDocs.totalHits/pageSize) + 1 : topDocs.totalHits/pageSize);
                        resultModel.setPageCount(pageCount);
                        resultModel.setRecordCount((int)topDocs.totalHits);
                    }
                    indexReader.close();
                }
            }
            resultModel.setList(blogs);
        }catch (Exception ex){
            throw new RuntimeException("查询博客失败:"+ex);
        }
        return resultModel;
    }

    /**
     * 更新博客，数据库索引库一起更新
     * 抛出异常将手动回滚
     * @param blog
     */
    @Transactional
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "updateBlog", title = "博客更新", description = "更新博客",roleType = ApiMapping.RoleType.LOGIN)
    public void updateBlog(Blog blog) {
        try{
            blog.decode();
            blog.setType(0);
            blog.deUnicode();
            blogDao.insertOrUpdateBlog(blog);
            //删除缓存
            redisUtils.del(blog2RedisBlogId+blog.getId());
            //删除对应博客中间表数据
            blogDao.deleteBlogClassesById(blog.getId());
            //为中间表添加数据
            blogDao.insertBlogMidClassesById(blog.classList(),blog.getId());

            Document document = new Document();
            document.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
            document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
            document.add(new TextField("description", blog.getDescription(), Field.Store.YES));
            document.add(new TextField("text", blog.getText(), Field.Store.YES));
            document.add(new IntPoint("like", blog.getBlogLike()));
            document.add(new StoredField("like", blog.getBlogLike()));
            document.add(new NumericDocValuesField("like", blog.getBlogLike()));
            document.add(new IntPoint("collect", blog.getCollect()));
            document.add(new StoredField("collect", blog.getCollect()));
            document.add(new NumericDocValuesField("collect", blog.getCollect()));
            document.add(new StoredField("image", blog.getImage()));
            document.add(new StoredField("isCreateSelf", blog.getIsCreateSelf()));
            document.add(new StringField("classId", blog.getBlogClass(), Field.Store.YES));
            document.add(new StringField("authorName", blog.getAuthorName(), Field.Store.YES));
            document.add(new LongPoint("time", blog.getUpdateTime().getTime()));
            document.add(new StoredField("time", blog.getUpdateTime().getTime()));
            document.add(new NumericDocValuesField("time", blog.getUpdateTime().getTime()));
            document.add(new TextField("className", blog.getBlogClassName(), Field.Store.YES));
            Analyzer analyzer = new IKAnalyzer();
            Directory  dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.updateDocument(new Term("id", String.valueOf(blog.getId())), document);
            indexWriter.close();
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("更新博客失败:"+ex);
        }
    }

    /**
     * 删除博客，数据库索引库都会删除
     * 抛出异常将手动回滚
     * @param id
     */
    @Transactional
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "deleteBlog", title = "博客删除", description = "删除博客",roleType = ApiMapping.RoleType.LOGIN)
    public void deleteBlog(long id) {
        try{
            blogDao.deleteBlog(id);
            blogDao.deleteBlogClassesById(id);
            redisUtils.del(blog2RedisBlogId+id);
            Analyzer analyzer = new IKAnalyzer();
            Directory  dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.deleteDocuments(new Term("id", String.valueOf(id)));
            indexWriter.close();
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("删除博客失败:"+ex);
        }
    }

    /**
     * 爬虫，注意：该接口具有三层缓存，springCache+网关缓存（redis）
     * springCache已弃用，因为redis只会缓存5分钟并且不会缓存报错的响应
     * springCache会连报错都缓存，这个报错不是这个方法报错，而是由rpc调用远程爬虫服务时，由爬虫服务抛出的异常
     * @param csdnUrl
     * @return
     */
    //@Cacheable(value = "csdnBlog", key = "#csdnUrl")
    @ApiCache
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "csdnBlog", title = "csdn博客爬取", description = "需要调用爬虫服务",roleType = ApiMapping.RoleType.LOGIN)
    public JSONObject csdnBlog(String csdnUrl,int type) {
        UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
        CSDNService csdnService= ServiceProxyFactory.createProxy(CSDNService.class,zqServiceWebSpiderAddr,upp);
        return csdnService.getCSDNBlogMessage(csdnUrl,type);
    }

    /**
     * 保存失效的csdn链接
     * @param csdnUrl
     */
    @ApiMapping(value = "errorCSDNLink", title = "失效的csdn链接",roleType = ApiMapping.RoleType.LOGIN)
    public void errorCSDNLink(String csdnUrl) {
        JSONObject user= (JSONObject)LoginUser.getUserHold();
        blogDao.insertErrorLinkCSDN(csdnUrl,user.get("id").toString());
    }

    /**
     * 将所有博客存入索引库
     * 先清空索引库再新增，相当于把数据库内所有博客重新刷入索引库
     * 管理员权限可操作（暂时注释掉了）
     */
    //@Timeout(timeout = 500)
    //@ApiMapping(value = "createBlogIndex", title = "重新生成博客索引库",roleType = ApiMapping.RoleType.ADMIN)
    @ApiMapping(value = "createBlogIndex", title = "重新生成博客索引库",description = "需要管理员权限，先清空索引库数据在新增")
    public void createBlogIndex(){
        BlogVo blogVo=new BlogVo();
        blogVo.setType(0);
        blogVo.setPageIndex(0);//不分页
        try {
            //只查已完成的博客
            List<Blog> blogList= blogDao.queryBlog(blogVo);
            List<Document> docList = new ArrayList<>();
            for (Blog blog : blogList) {
                Document document = new Document();
                //创建域对象并且放入文档对象中
                //给标题，描述，喜欢数，收藏数，内容创建索引
                //使用Int/Long/DoublePoint来表示数值型字段的,默认不存储,不排序,也不支持加权
                /**
                 * 三个参数分别的意思是：
                 * 是否分词: 否, 因为主键分词后无意义
                 * 是否索引: 是, 如果根据id主键查询, 就必须索引
                 * 是否存储: 是, 因为主键id比较特殊, 可以确定唯一的一条数据, 在业务上一般有重要所用, 所以存储
                 *      存储后, 才可以获取到id具体的内容
                 */
                document.add(new StringField("id", String.valueOf(blog.getId()),Field.Store.YES));
                document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
                document.add(new TextField("description", blog.getDescription(), Field.Store.YES));
                document.add(new TextField("text", blog.getText(), Field.Store.YES));
                document.add(new IntPoint("like", blog.getBlogLike()));
                document.add(new StoredField("like", blog.getBlogLike()));
                document.add(new NumericDocValuesField("like", blog.getBlogLike()));
                document.add(new IntPoint("collect", blog.getCollect()));
                document.add(new StoredField("collect", blog.getCollect()));
                document.add(new NumericDocValuesField("collect", blog.getCollect()));
                document.add(new StoredField("image", blog.getImage()));
                document.add(new StoredField("isCreateSelf", blog.getIsCreateSelf()));
                document.add(new StringField("classId", blog.getBlogClass(), Field.Store.YES));
                document.add(new StringField("authorName", blog.getAuthorName(), Field.Store.YES));

                //用于对时间排序
                //document.add(new TextField("updateDate", DateUtil.formatDate(blog.getUpdateTime()), Field.Store.YES));
                //添加排序支持
                //document.add(new SortedDocValuesField("updateDate", new BytesRef(DateUtil.formatDate(blog.getUpdateTime()))));

                //大小,数字类型使用point添加到索引中,同时如果需要存储,由于没有Store,所以需要再创建一个StoredField进行存储
                document.add(new LongPoint("time", blog.getUpdateTime().getTime()));
                //存储数值类型
                document.add(new StoredField("time", blog.getUpdateTime().getTime()));
                //同时添加排序支持
                document.add(new NumericDocValuesField("time", blog.getUpdateTime().getTime()));
                document.add(new TextField("className", blog.getBlogClassName(), Field.Store.YES));
                docList.add(document);
            }
            //创建分词器, IK分词器,
            Analyzer analyzer = new IKAnalyzer();
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.deleteAll();//先清空索引库
            for (Document doc : docList) {
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
        }catch (Exception ex){
            throw new RuntimeException("将所有博客存入索引库:"+ex);
        }
    }

    /**
     * 获取热搜,可以根据时间查询即某天前的热搜
     * 也可以获取用户的搜索，用于生成用户画像
     * @return
     */
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "hotSearchKey", title = "获取热搜")
    public List<Map> hotSearchKey() {
        Map map=new HashMap();//待添加查询条件
        return blogDao.queryHotKey(map);
    }

    /**
     * 测试接口，表单提交
     * @param formInfos
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogFile", title = "博客测试表单提交", description = "博客测试表单提交")
    public JSONArray blogFile(List<LinkedHashMap> formInfos) throws IOException, ClassNotFoundException{
        ObjectMapper mapper=new ObjectMapper();
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<formInfos.size();i++){
            JSONObject jsonObject=new JSONObject();
            FormInfo formInfo = mapper.convertValue(formInfos.get(i), FormInfo.class);
            jsonObject.put("name",formInfo.getInputStream());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject1=new JSONObject();
        jsonArray.add(jsonObject1);
        return jsonArray;
    }

    /**
     * 测试接口，当参数有二进制文件的
     * @param formInfos
     * @param blogType
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "blogImgUpload", title = "博客图片上传", description = "博客图片上传",type = ApiMapping.Type.POST)
    public JSONObject blogImgUpload(List<LinkedHashMap> formInfos,String blogType) throws IOException, ClassNotFoundException{
        ObjectMapper mapper=new ObjectMapper();
        String fileName="";
        for(int i=0;i<formInfos.size();i++){
            FormInfo formInfo = mapper.convertValue(formInfos.get(i), FormInfo.class);
            //该参数里，null是字符串，表示不是文件流
            if(!"null".equals(formInfo.getContentType())){
                InputStream inputStream= Base64Util.baseToInputStream(formInfo.getInputStream());
                fileName=formInfo.getSubmittedFileName();
                FileUtil.saveFile(inputStream,fileName,"D:\\projectFile\\markdown");
            }else{

            }
        }
        JSONObject jsonObject=new JSONObject();
        if("markdown".equals(blogType)){
            jsonObject.put("success",1);
            jsonObject.put("message","上传成功");
            jsonObject.put("url","http://127.0.0.1:8809/markdown/"+fileName);
        }else if("fwb".equals(blogType)){
            JSONArray jsonArray=new JSONArray();
            jsonArray.add("http://127.0.0.1:8809/markdown/"+fileName);
            jsonObject.put("errno",0);
            jsonObject.put("data",jsonArray);
        }
        return jsonObject;
    }
}
