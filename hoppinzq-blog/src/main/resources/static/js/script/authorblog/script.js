let ipconfig = __zqBlog.ipConfig;
let requestBlogIp = ipconfig.ip_ + ":" + ipconfig.blogPort;
$(function () {
    __zqBlog.stopLoading();
    function iniciar(){
        $('.follow').on("click", function(){
            $('.follow').css('background-color','#34CF7A');
            $('.follow').html('<div class="icon-ok"></div> 已转粉');
        });
    }
    if(__zqBlog.isMobile){
        $("#calendar").remove();
    }
    $.ajax({
        url:`${requestBlogIp}/hoppinzq?method=queryBlog&params={"blogVo":{"pageIndex":1,"pageSize":6,"searchType":0,"blogReturn":1,"author":1}}`,
        success:function (data) {
            let json=JSON.parse(data);
            if(json.code==200){
                let blogs=json.data.list;
                let pageCount=json.data.pageCount;
                let curPage=json.data.curPage;
                let recordCount=json.data.recordCount;
                if(blogs.length>0){
                    $.each(blogs,function (index,blog){
                        $(".author-blog-list").append(`<div class="col mb-7">
                            <div class="blog-card blog-card-list2">
                                <div class="thumb bg-light p-0 text-center">
                                    <a href="${requestBlogIp}/blog/${blog.id}">
                                        ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                                            "width":"182px",
                                            "height":"182px",
                                            "object-fit":"cover"
                                        })} 
                                    </a>
                                </div>
                                <div class="blog-content">
                                    <h3 class="title">
                                        <a href="${requestBlogIp}/blog/${blog.id}">${blog.title}</a>
                                    </h3>
                                    <p class="blog_link_meta">${__zqBlog.getRealDate(blog.updateTime)}</p>
                                    <p>${blog.description}</p>
                                    <a class="blog-list-link" href="${requestBlogIp}/blog/${blog.id}">阅读</a>
                                </div>
                            </div>
                        </div>`)
                    })
                }
            }
        },
        beforeSend:function () {

        },
        error:function () {

        },
        complete:function () {

        }
    })
    $(".blog-author-search-value").on("input",function () {
        let search=$(this).val();
        if(search.trim().length==0){
            $(".author-blog-list").children(".col").show();
        }
        $(".author-blog-list").find(".col").each(function (index,element){
            if($(element).text().replace(/(^\s*)|(\s*$)/g,"").indexOf(search)==-1){
                $(element).hide();
            }else{
                $(element).show();
            }
        })
    })
})