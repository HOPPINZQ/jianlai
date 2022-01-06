package com.hoppinzq.service.bean;

import java.util.Date;

/**
 * @author:ZhangQi
 **/
public class BlogVo extends Page{
    private String id;
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
    private String author;//作者ID
    private Date create_time;//创建时间
    private Date update_time;//最后一次修改时间
    private String file;//附件id
    private int is_comment;//0允许评论，1不允许评论
    private String _class;//分类，格式 大类ID||小类ID1|小类ID2|小类ID3
    private String _class_name;//分类名称
    private int is_create_self;//0原创，1转载
    private String music_file;//背景音乐id
    private String image;//博客封面图片
    private String html;//博客内容html
    private String copy_link;//转载链接

    private int blogReturn=0;//不是1表示查询所有字段



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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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
}
