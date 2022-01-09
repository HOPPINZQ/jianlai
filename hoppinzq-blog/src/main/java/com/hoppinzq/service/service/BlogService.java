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
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.util.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@ApiServiceMapping(title = "博客服务", description = "博客服务，已加入网关",roleType = ApiServiceMapping.RoleType.RIGHT)
public class BlogService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    BlogLogDao logDao;
    @Autowired
    private RedisUtils redisUtils;
    private BlogService blogService;
    @Autowired
    private RPCPropertyBean rpcPropertyBean;

    @Value("${zqServiceWebSpider.addr:http:127.0.0.1:8806/service}")
    private String zqServiceWebSpiderAddr;

    @Value("${zqClient.authAddr:http:127.0.0.1:8804/service}")
    private String authServiceAddr;

    @Value("${lucene.indexPath:D:\\index}")
    private String indexPath;

    public final static Integer PAGE_SIZE = 20;

    @Self
    public void setSelf(BlogService blogService) {
        this.blogService = blogService;
    }

    private static final String blog2RedisKeyPrefix="BLOG:";
    private static final String blog2RedisBlogId=blog2RedisKeyPrefix+"BLOG_ID:";
    private static final String blog2RedisBlogClass=blog2RedisKeyPrefix+"BLOG_CLASS:";
    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

    /**
     * 草稿保存进redis
     * 流程，前端每30s调用一次该接口，从blog的text取到前端处理好的数据，然后清空一些内容，减少对数据库的压力
     * 1、第一次进入该接口，redis肯定没有该id的草稿,异步将草稿存入数据库内
     * 2、若redis有该id的草稿，覆盖之，返回ID
     * @return
     */
    @ApiMapping(value = "saveBlog2Redis", title = "保存草稿", description = "每1min会调用一次接口保存博客内容进redis",roleType = ApiMapping.RoleType.LOGIN)
    public JSONObject saveBlog2Redis(Blog blog){
        String blogId=blog.getId();
        blog.decode();
        blog.deUnicode();
        JSONObject returnJSON = JSONUtil.createJSONObject("blogId",blogId);
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
     * 获取博客类别，从redis获取，兜底从数据库获取并放入redis中
     * 该项目启动后会先预热缓存，因此其实一开始redis就有类别了
     * @return
     */
    //@ServiceLimit(limitType = ServiceLimit.LimitType.IP)
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
     * 抛出异常将手动回滚事务
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
            if(blog.getId()==null){
                blog.setId(UUIDUtil.getUUID());
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
            document.add(new StringField("id", blog.getId(), Field.Store.YES));
            document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
            document.add(new TextField("description", blog.getDescription(), Field.Store.YES));
            document.add(new TextField("text", blog.getText(), Field.Store.YES));
            document.add(new IntPoint("like", blog.getBlogLike()));
            document.add(new StoredField("like", blog.getBlogLike()));
            document.add(new IntPoint("collect", blog.getCollect()));
            document.add(new StoredField("collect", blog.getCollect()));
            document.add(new StoredField("image", blog.getImage()));
            document.add(new StringField("time", DateUtil.formatDate(blog.getUpdateTime()), Field.Store.YES));
            document.add(new StringField("classId", blog.getBlogClass(), Field.Store.YES));
            document.add(new TextField("className", blog.getBlogClassName(), Field.Store.YES));
            Analyzer analyzer = new IKAnalyzer();
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.deleteAll();
            indexWriter.addDocument(document);
            indexWriter.close();
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("新增博客失败:"+ex);
        }
    }

    @Cacheable(value = "blogClass")
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "insertBlogClass", title = "博客类别新增",roleType = ApiMapping.RoleType.LOGIN)
    public List<BlogClass> insertBlogClass(String blogName,String parentId) {
        User user= (User)LoginUser.getUserHold();
        String[] blogNames=blogName.split(",");
        List<BlogClass> blogClasses=new ArrayList<>();
        for(String name:blogNames){
            blogClasses.add(new BlogClass(UUIDUtil.getUUID(),parentId,name, user.getId()));
        }
        blogDao.insertBlogClasses(blogClasses);
        redisUtils.del(blog2RedisBlogClass+"blogClass");
        return blogClasses;
    }

    /**
     * 查询博客
     * 特殊传参：searchType为0表示走数据库，searchType为1表示走索引库
     * 传参有id的情况只走数据库，无论searchType是否是1
     * pageIndex为0表示不分页
     * blogReturn为1表示只返回部分字段（因为有时候展示博客列表并不需要博客所有字段，这会导致响应体很大）
     * blogDetail为1表示查询非常完整的博客详情（blogReturn必须不为1）,此时若blogReturn为1也只会返回部分字段
     * search为走索引库的关键字，这个关键字会从以下字段匹配。👇
     * 走索引库会根据权值进行排序，title>authorName>description>className>text
     * 喜欢 跟 收藏 字段如果传范围必须为 x~y 格式 (x<y)
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
        resultModel.setCurPage(page);
        try{
            //若查询参数传入id将强行走数据库
            if(StringUtil.isNotEmpty(blogVo.getId())){
                blogs=blogDao.queryBlog(blogVo);
                if(blogs.get(0).getIsComment()==0){
                    CommentVo commentVo=new CommentVo();
                    commentVo.setComment_search_type(2);
                    commentVo.setComment_blogId(blogVo.getId());
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
                    Integer start =0;
                    Integer end = 0;
                    if(pageIndex!=0){
                        start = (blogVo.getPageIndex() - 1) * PAGE_SIZE;
                        end = blogVo.getPageIndex() * PAGE_SIZE;
                    }
                    Analyzer analyzer = new IKAnalyzer();
                    BooleanQuery.Builder query = new BooleanQuery.Builder();
                    if(StringUtil.isNotEmpty(blogVo.getSearch())){
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
                    //end是分页
                    topDocs = indexSearcher.search(query.build(), end);
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
                                blog=new Blog(doc.get("id"),doc.get("title"),doc.get("description"),doc.get("text"),
                                        Integer.parseInt(doc.get("like")),Integer.parseInt(doc.get("collect")),doc.get("time"),
                                        doc.get("authorName"),doc.get("classId"),doc.get("className"),doc.get("image"));
                            }else{
                                blog=new Blog(doc.get("id"),doc.get("title"),doc.get("description"),
                                        Integer.parseInt(doc.get("like")),Integer.parseInt(doc.get("collect")),doc.get("time"),
                                        doc.get("authorName"),doc.get("classId"),doc.get("className"),doc.get("image"));
                            }
                            blogs.add(blog);
                        }
                        int pageCount = (int)(topDocs.totalHits % PAGE_SIZE > 0 ? (topDocs.totalHits/PAGE_SIZE) + 1 : topDocs.totalHits/PAGE_SIZE);
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
            blogDao.updateBlog(blog);
            //删除对应博客中间表数据
            blogDao.deleteBlogClassesById(blog.getId());
            //为中间表添加数据
            blogDao.insertBlogMidClassesById(blog.classList(),blog.getId());

            Document document = new Document();
            document.add(new StringField("id", blog.getId(), Field.Store.YES));
            document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
            document.add(new TextField("description", blog.getDescription(), Field.Store.YES));
            document.add(new TextField("text", blog.getText(), Field.Store.YES));
            document.add(new IntPoint("like", blog.getBlogLike()));
            document.add(new StoredField("like", blog.getBlogLike()));
            document.add(new IntPoint("collect", blog.getCollect()));
            document.add(new StoredField("collect", blog.getCollect()));
            document.add(new StoredField("image", blog.getImage()));
            document.add(new StringField("time", DateUtil.formatDate(blog.getUpdateTime()), Field.Store.YES));
            document.add(new StringField("authorName", blog.getAuthorName(), Field.Store.YES));
            document.add(new StringField("classId", blog.getBlogClass(), Field.Store.YES));
            document.add(new TextField("className", blog.getBlogClassName(), Field.Store.YES));
            Analyzer analyzer = new IKAnalyzer();
            Directory  dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.updateDocument(new Term("id", blog.getId()), document);
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
    public void deleteBlog(String id) {
        try{
            blogDao.deleteBlog(id);
            blogDao.deleteBlogClassesById(id);

            Analyzer analyzer = new IKAnalyzer();
            Directory  dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.deleteDocuments(new Term("id", id));
            indexWriter.close();
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("删除博客失败:"+ex);
        }
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
     * 爬虫，注意：该接口具有双层缓存，springCache+redis
     * springCache已弃用，因为redis只会缓存5分钟并且不会缓存报错的响应
     * springCache会连报错都缓存，且得单独管理时效，故弃用
     * @param csdnUrl
     * @return
     */
    //@Cacheable(value = "csdnBlog", key = "#csdnUrl")
    @ApiCache
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP,number = 1)
    @ApiMapping(value = "csdnBlog", title = "csdn博客爬取", description = "需要调用爬虫服务",roleType = ApiMapping.RoleType.LOGIN)
    public JSONObject csdnBlog(String csdnUrl) {
        UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
        CSDNService csdnService= ServiceProxyFactory.createProxy(CSDNService.class,zqServiceWebSpiderAddr,upp);
        return csdnService.getCSDNBlogMessage(csdnUrl);
    }

    /**
     * 保存失效的csdn链接
     * @param csdnUrl
     */
    @ApiMapping(value = "errorCSDNLink", title = "失效的csdn链接",roleType = ApiMapping.RoleType.LOGIN)
    public void errorCSDNLink(String csdnUrl) {
        User user= (User)LoginUser.getUserHold();
        blogDao.insertErrorLinkCSDN(csdnUrl,user.getId());
    }

    /**
     * 将所有博客存入索引库
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
                //返回xxx
                /**
                 * 是否分词: 否, 因为主键分词后无意义
                 * 是否索引: 是, 如果根据id主键查询, 就必须索引
                 * 是否存储: 是, 因为主键id比较特殊, 可以确定唯一的一条数据, 在业务上一般有重要所用, 所以存储
                 *      存储后, 才可以获取到id具体的内容
                 */
                document.add(new StringField("id", blog.getId(), Field.Store.YES));
                document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
                document.add(new TextField("description", blog.getDescription(), Field.Store.YES));
                document.add(new TextField("text", blog.getText(), Field.Store.YES));
                document.add(new IntPoint("like", blog.getBlogLike()));
                document.add(new StoredField("like", blog.getBlogLike()));
                document.add(new IntPoint("collect", blog.getCollect()));
                document.add(new StoredField("collect", blog.getCollect()));
                document.add(new StoredField("image", blog.getImage()));
                document.add(new StringField("classId", blog.getBlogClass(), Field.Store.YES));
                document.add(new StringField("authorName", blog.getAuthorName(), Field.Store.YES));
                document.add(new StringField("time", DateUtil.formatDate(blog.getUpdateTime()), Field.Store.YES));
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
