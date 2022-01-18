package com.hoppinzq.service.bean;

import com.hoppinzq.service.util.Base64Util;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Blog {

    private long id;//博客主键
    private String title;//标题
    private String description;//描述
    private int buildType;//构建类型：0简单富文本，1富文本，2markdown，3csdn
    private String csdnLink;//csdn链接
    private String text;//博客内容
    private int blogLike;//喜欢数
    private int star;//评分
    private int collect;//收藏数
    private String author;//作者ID
    private String authorName;//作者名字
    private Date createTime;//创建时间
    private Date updateTime;//最后一次修改时间
    private String filePath;//附件路径
    private String fileId;//附件id
    private int isComment;//0允许评论，1不允许评论
    private String blogClass;//分类，格式 大类ID||小类ID1|小类ID2|小类ID3
    private String blogClassName;//分类名称
    private int isCreateSelf;//0原创，1转载
    private String musicFile;//背景音乐id
    private String image;//博客封面图片
    private String html;//博客内容html
    private String copyLink;//转载链接
    private int type;//博客类型0草稿1博客
    private FileBean fileFj;//附件
    private User user;
    private int show;//访问次数
    private List<Comment> blogComment;//评论

    public Blog() {
    }

    public Blog(long id, String title, String description, String text, int blogLike, int collect, Long updateTime, String authorName,String blogClass, String blogClassName, String image,int isCreateSelf) throws ParseException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.text = text;
        this.blogLike = blogLike;
        this.collect = collect;
        this.updateTime = new Date(updateTime);
        this.authorName=authorName;
        this.blogClass = blogClass;
        this.blogClassName = blogClassName;
        this.image = image;
        this.isCreateSelf=isCreateSelf;
    }

    public Blog(long id, String title, String description, int blogLike, int collect, Long updateTime, String authorName,String blogClass, String blogClassName, String image,int isCreateSelf) throws ParseException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.blogLike = blogLike;
        this.collect = collect;
        this.updateTime = new Date(updateTime);
        this.authorName=authorName;
        this.blogClass = blogClass;
        this.blogClassName = blogClassName;
        this.image = image;
        this.isCreateSelf=isCreateSelf;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }
    public void addShow() {
        this.show++;
    }

    public List<Comment> getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(List<Comment> blogComment) {
        this.blogComment = blogComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileBean getFileFj() {
        return fileFj;
    }

    public void setFileFj(FileBean fileFj) {
        this.fileFj = fileFj;
    }

    public List<String> classList(){
        List<String> myClass=new ArrayList<>();
        String[] blog_class=this.blogClass.split("\\|\\|");
        myClass.add(blog_class[0]);
        if(blog_class[1].indexOf("|")!=-1){
            String[] smallClass=blog_class[1].split("\\|");
            myClass.addAll(Arrays.asList(smallClass));
        }
        return myClass;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getBuildType() {
        return buildType;
    }

    public void setBuildType(int buildType) {
        this.buildType = buildType;
    }

    public String getCsdnLink() {
        return csdnLink;
    }

    public void setCsdnLink(String csdnLink) {
        this.csdnLink = csdnLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBlogLike() {
        return blogLike;
    }

    public void setBlogLike(int blogLike) {
        this.blogLike = blogLike;
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

    public Date getCreateTime() {
        return createTime==null?new Date():createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime==null?new Date():updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }

    public String getBlogClass() {
        return blogClass;
    }

    public void setBlogClass(String blogClass) {
        this.blogClass = blogClass;
    }

    public String getBlogClassName() {
        return blogClassName;
    }

    public void setBlogClassName(String blogClassName) {
        this.blogClassName = blogClassName;
    }

    public int getIsCreateSelf() {
        return isCreateSelf;
    }

    public void setIsCreateSelf(int isCreateSelf) {
        this.isCreateSelf = isCreateSelf;
    }

    public String getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(String musicFile) {
        this.musicFile = musicFile;
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

    public String getCopyLink() {
        return copyLink;
    }

    public void setCopyLink(String copyLink) {
        this.copyLink = copyLink;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 解码，将text与html解码，如果解码报错，不予处理，返回原数据
     */
    public void decode() {
        try{
            this.html=Base64Util.base64DecodePlus(this.html);
        }catch (Exception ex){
            //不处理
        }
        try{
            this.text=Base64Util.base64DecodePlus(this.text);
        }catch (Exception ex){
            //不处理
        }

    }

    public void deUnicode(){
        try{
            this.html=Base64Util.encodeDecode(this.html);
        }catch (Exception ex){
            //不处理
        }
        try{
            this.text=Base64Util.encodeDecode(this.text);
        }catch (Exception ex){
            //不处理
        }
    }

}
