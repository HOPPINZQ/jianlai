package com.hoppinzq.service.bean;

import com.hoppinzq.service.util.CombineBeans;

import java.util.Date;

/**
 * @author:ZhangQi
 * 设计了一个人类高质量构建器
 **/
public class BlogVo extends Page{
    private long id;
    private String search;//搜索关键词
    private int type;//博客类型0草稿1博客
    private int searchType=0;
    private String title;//标题
    private String description;//描述
    private int build_type;//构建类型：0简单富文本，1富文本，2markdown，3csdn
    private String csdn_link;//csdn链接
    private String text;//博客内容
    private int blog_like;//喜欢数
    private String blog_likes;//喜欢数范围
    private int star;//评分
    private int collect;//收藏数
    private String collects;//喜欢数范围
    private long author;//作者ID
    private Date create_time;//创建时间
    private Date update_time;//最后一次修改时间
    private String file;//附件id
    private int is_comment=0;//0允许评论，1不允许评论
    private String _class;//分类，格式 大类ID||小类ID1|小类ID2|小类ID3
    private String _class_name;//分类名称
    private int is_create_self;//0原创，1转载
    private String music_file;//背景音乐id
    private String image;//博客封面图片
    private String html;//博客内容html
    private String copy_link;//转载链接
    private int show;//访问次数
    private int order;//排序规则

    private int blogReturn=0;//不是1表示查询所有字段
    private int blogDetail=0;//是1表示尽可能查询博客及关联详情,是2表示尽可能查询博客及关联详情但是不查询评论

    public BlogVo() {
    }

    public BlogVo(long id, String search, int type, int searchType, String title, String description, int build_type, String csdn_link, String text, int blog_like, String blog_likes, int star, int collect, String collects, long author, Date create_time, Date update_time, String file, int is_comment, String _class, String _class_name, int is_create_self, String music_file, String image, String html, String copy_link, int order, int blogReturn, int blogDetail,int show,int pageSize,int pageIndex) {
        this.id = id;
        this.search = search;
        this.type = type;
        this.searchType = searchType;
        this.title = title;
        this.description = description;
        this.build_type = build_type;
        this.csdn_link = csdn_link;
        this.text = text;
        this.blog_like = blog_like;
        this.blog_likes = blog_likes;
        this.star = star;
        this.collect = collect;
        this.collects = collects;
        this.author = author;
        this.create_time = create_time;
        this.update_time = update_time;
        this.file = file;
        this.is_comment = is_comment;
        this._class = _class;
        this._class_name = _class_name;
        this.is_create_self = is_create_self;
        this.music_file = music_file;
        this.image = image;
        this.html = html;
        this.copy_link = copy_link;
        this.order = order;
        this.show=show;
        this.blogReturn = blogReturn;
        this.blogDetail = blogDetail;
        super.setPageIndex(pageIndex);
        super.setPageSize(pageSize);
    }

    public BlogVo(int searchType, int order, int blogReturn, int pageSize) {
        this.setPageSize(pageSize);
        this.searchType = searchType;
        this.order = order;
        this.blogReturn = blogReturn;
    }

    @Override
    public String toString() {
        return "BlogVo{" +
                "id=" + id +
                ", search='" + search + '\'' +
                ", type=" + type +
                ", searchType=" + searchType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", build_type=" + build_type +
                ", blog_like=" + blog_like +
                ", blog_likes='" + blog_likes + '\'' +
                ", star=" + star +
                ", collect=" + collect +
                ", collects='" + collects + '\'' +
                ", author='" + author + '\'' +
                ", is_comment=" + is_comment +
                ", _class='" + _class + '\'' +
                ", _class_name='" + _class_name + '\'' +
                ", is_create_self=" + is_create_self +
                ", page_size='" + getPageSize() +
                ", page_index='" + getPageIndex() +
                ", show=" + show +
                ", order=" + order +
                ", blogReturn=" + blogReturn +
                ", blogDetail=" + blogDetail +
                '}';
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getBlogDetail() {
        return blogDetail;
    }

    public void setBlogDetail(int blogDetail) {
        this.blogDetail = blogDetail;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int[] getCollects() {
        if(collects!=null){
            if(collects.indexOf("~")==-1){
                return new int[]{0,0};
            }else{
                return new int[]{Integer.parseInt(collects.split("~")[0]),Integer.parseInt(collects.split("~")[1])};
            }
        }else{
            return null;
        }
    }

    public void setBlog_likes(String blog_likes) {
        this.blog_likes = blog_likes;
    }

    public void setCollects(String collects) {
        this.collects = collects;
    }

    public int[] getBlog_likes() {
        if(blog_likes!=null){
            if(blog_likes.indexOf("~")==-1){
                return new int[]{0,0};
            }else{
                return new int[]{Integer.parseInt(blog_likes.split("~")[0]),Integer.parseInt(blog_likes.split("~")[1])};
            }
        }else{
            return null;
        }
    }


    public int getBlogReturn() {
        return blogReturn;
    }

    public void setBlogReturn(int blogReturn) {
        this.blogReturn = blogReturn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuild_type() {
        return build_type;
    }

    public void setBuild_type(int build_type) {
        this.build_type = build_type;
    }

    public String getCsdn_link() {
        return csdn_link;
    }

    public void setCsdn_link(String csdn_link) {
        this.csdn_link = csdn_link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBlog_like() {
        return blog_like;
    }

    public void setBlog_like(int blog_like) {
        this.blog_like = blog_like;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(int is_comment) {
        this.is_comment = is_comment;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String get_class_name() {
        return _class_name;
    }

    public void set_class_name(String _class_name) {
        this._class_name = _class_name;
    }

    public int getIs_create_self() {
        return is_create_self;
    }

    public void setIs_create_self(int is_create_self) {
        this.is_create_self = is_create_self;
    }

    public String getMusic_file() {
        return music_file;
    }

    public void setMusic_file(String music_file) {
        this.music_file = music_file;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getCopy_link() {
        return copy_link;
    }

    public void setCopy_link(String copy_link) {
        this.copy_link = copy_link;
    }

    public static BuilderBlogVo newBuilder() {
        return new BuilderBlogVo();
    }

    public static class BuilderBlogVo {
        private BlogVo blogVo;
        public BuilderBlogVo blogVo(final BlogVo blogVo){
            this.blogVo=blogVo;
            return this;
        }
        private long id;
        public BuilderBlogVo id(final long id) {
            this.id = id;
            return this;
        }
        private String search;//搜索关键词
        public BuilderBlogVo search(final String search) {
            this.search = search;
            return this;
        }
        private int type;//博客类型0草稿1博客
        public BuilderBlogVo type(final int type) {
            this.type = type;
            return this;
        }
        private int searchType=0;
        public BuilderBlogVo searchType(final int searchType) {
            this.searchType = searchType;
            return this;
        }
        private String title;//标题
        public BuilderBlogVo searchType(final String title) {
            this.title = title;
            return this;
        }
        private String description;//描述
        public BuilderBlogVo description(final String description) {
            this.description = description;
            return this;
        }
        private int build_type;//构建类型：0简单富文本，1富文本，2markdown，3csdn
        public BuilderBlogVo build_type(final int build_type) {
            this.build_type = build_type;
            return this;
        }
        private String csdn_link;//csdn链接
        public BuilderBlogVo csdn_link(final String csdn_link) {
            this.csdn_link = csdn_link;
            return this;
        }
        private String text;//文本
        public BuilderBlogVo text(final String text) {
            this.text = text;
            return this;
        }
        private int blog_like;//喜欢数
        public BuilderBlogVo blog_like(final int blog_like) {
            this.blog_like = blog_like;
            return this;
        }
        private String blog_likes;//喜欢数范围
        public BuilderBlogVo blog_likes(final String blog_likes) {
            this.blog_likes = blog_likes;
            return this;
        }

        private int collect;//收藏数
        public BuilderBlogVo collect(final int collect) {
            this.collect = collect;
            return this;
        }
        private String collects;//喜欢数范围
        public BuilderBlogVo collects(final String collects) {
            this.collects = collects;
            return this;
        }
        private int star;//评分
        public BuilderBlogVo star(final int star) {
            this.star = star;
            return this;
        }
        private long author;//作者ID
        public BuilderBlogVo author(final long author) {
            this.author = author;
            return this;
        }
        private Date create_time;//创建时间
        public BuilderBlogVo create_time(final Date create_time) {
            this.create_time = create_time;
            return this;
        }
        private Date update_time;//最后一次修改时间
        public BuilderBlogVo update_time(final Date update_time) {
            this.update_time = update_time;
            return this;
        }
        private String file;//附件id
        public BuilderBlogVo file(final String file) {
            this.file = file;
            return this;
        }
        private String _class;//分类，格式 大类ID||小类ID1|小类ID2|小类ID3
        public BuilderBlogVo _class(final String _class) {
            this._class = _class;
            return this;
        }
        private String _class_name;//分类名称
        public BuilderBlogVo _class_name(final String _class_name) {
            this._class_name = _class_name;
            return this;
        }
        private int is_comment;//0允许评论，1不允许评论
        public BuilderBlogVo is_comment(final int is_comment) {
            this.is_comment = is_comment;
            return this;
        }
        private int is_create_self;//0原创，1转载
        public BuilderBlogVo is_create_self(final int is_create_self) {
            this.is_create_self = is_create_self;
            return this;
        }
        private String music_file;//背景音乐id
        public BuilderBlogVo music_file(final String music_file) {
            this.music_file = music_file;
            return this;
        }
        private String image;//博客封面图片
        public BuilderBlogVo image(final String image) {
            this.image = image;
            return this;
        }
        private String html;//博客内容html
        public BuilderBlogVo html(final String html) {
            this.html = html;
            return this;
        }
        private String copy_link;//转载链接
        public BuilderBlogVo copy_link(final String copy_link) {
            this.copy_link = copy_link;
            return this;
        }
        private int order;//排序规则
        public BuilderBlogVo order(final int order) {
            this.order = order;
            return this;
        }
        private int blogReturn=0;//不是1表示查询所有字段
        public BuilderBlogVo blogReturn(final int blogReturn) {
            this.blogReturn = blogReturn;
            return this;
        }
        private int blogDetail=0;//是1表示尽可能查询博客及关联详情
        public BuilderBlogVo blogDetail(final int blogDetail) {
            this.blogDetail = blogDetail;
            return this;
        }
        private int show;
        public BuilderBlogVo show(final int show) {
            this.show = show;
            return this;
        }
        private int pageSize;//每页多少条
        public BuilderBlogVo pageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }
        private int pageIndex=1;//第几页
        public BuilderBlogVo pageIndex(final int pageIndex) {
            this.pageIndex = pageIndex;
            return this;
        }

        /**
         * 构建实体类
         * @return
         */
        public final BlogVo bulid() {
            if(blogVo==null){
                return new BlogVo(id,search,type,searchType,title,description,build_type,csdn_link,text,blog_like,blog_likes,
                        star,collect,collects,author,create_time,update_time,file,is_comment,_class,_class_name,is_create_self,
                        music_file,image,html,copy_link,order,blogReturn,blogDetail,show,pageSize,pageIndex);
            }else{
                return (BlogVo) CombineBeans.combineCore(new BlogVo(id,search,type,searchType,title,description,build_type,csdn_link,text,blog_like,blog_likes,
                        star,collect,collects,author,create_time,update_time,file,is_comment,_class,_class_name,is_create_self,
                        music_file,image,html,copy_link,order,blogReturn,blogDetail,show,pageSize,pageIndex),blogVo);
            }
        }

    }
}
