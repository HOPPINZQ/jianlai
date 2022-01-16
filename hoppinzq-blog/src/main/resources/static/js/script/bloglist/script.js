let ipconfig=__zqBlog.ipConfig;
let ip=ipconfig.ip_;
let blogPort=ipconfig.blogPort;
let searchParams={
    "searchType":1,
    "blogReturn":1,
    "pageSize":16
};
$(function () {
    //解析url，赋值
    let search=__zqBlog.getWebURLKey("s");
    let search_class=__zqBlog.getWebURLKey("c");
    let search_class_name=__zqBlog.getWebURLKey("cn");
    let page_index=__zqBlog.getWebURLKey("p");
    if(search!=null){
        $(".search-form input").val(window.decodeURIComponent(search));
        searchParams.search=window.decodeURIComponent(search)
    }
    if(page_index==null){
        searchParams.pageIndex=1
    }else{
        searchParams.pageIndex=page_index
    }
    //由于类别是在脚本填充的，所以要轮询，当填充好后选中url上的类别
    if(search_class!=null){
        searchParams._class=window.decodeURIComponent(search_class);
        let search_intervel_id=__zqBlog.zinterval(function (search_intervel_id) {
            if($("#autoSizingSelect").find("option").length>1){
                clearInterval(search_intervel_id);
                $("#autoSizingSelect").find("option[value='"+search_class+"']").attr("selected",true);
            }
        },500);
    }
    if(search_class_name!=null&&search_class!=null&&search_class!=0){
        search_class_name=window.decodeURIComponent(search_class_name);
        search_class_name=search_class_name.replace("--","").replace("--","").trim();
        searchParams._class_name=search_class_name;
    }

    loadBlogList(searchParams);

    //该页面重写main.js的点击搜索事件，因为只要部分刷新就好了，不需要重定向了
    $(".search-blog-ex").off("click").on("click",function () {
        let search=$(this).prev("input").val();
        //将历史搜索放入storage中
        if(search!=""){
            let storageSearchKey=localStorage.getItem("searchKey");
            if(storageSearchKey==null){
                localStorage.setItem("searchKey",JSON.stringify([{
                    "value":search,
                    "timestamp":new Date().getTime()
                }]))
            }else{
                let searchList=JSON.parse(storageSearchKey);
                let isExist=false;
                $.each(searchList,function (index,searchKey){
                    if(searchKey.value==search){
                        searchKey.timestamp=new Date().getTime();
                        isExist=true;
                        return;//跳出循环
                    }
                })
                if(!isExist){
                    searchList.push({
                        "value":search,
                        "timestamp":new Date().getTime()
                    });
                }
                searchList.sort(function(a, b) {
                    return b.timestamp - a.timestamp;
                })
                let strSearchList=JSON.stringify(searchList);
                localStorage.setItem("searchKey",strSearchList);
            }
        }
        searchParams.search=search;
        searchParams._class=$("#autoSizingSelect option:selected").val();
        searchParams._class_name=window.decodeURIComponent($("#autoSizingSelect option:selected").text())
            .replace("--","").replace("--","").trim();
        loadBlogList(searchParams,startLoadingBlogList());
    });

    //排序搜索
    $(".select-bloglist-order").change(function () {
        let order=$(this).val();
        searchParams.order=order;
        loadBlogList(searchParams,startLoadingBlogList());
    })

})

function startLoadingBlogList() {
    //__zqBlog.startLoading();
    $("#list-grid1").html("");
    $("#list-grid2").html("");
    $(".total-products").html("");
    $(".product-class-wrapper").html("");
    if ($("#blog-list").find(".preloader").length) {
        $(".list-grid-loading").fadeIn();
    } else {
        $("#blog-list-row").after($(`<div class="preloader center">
    <span>H</span><span>O</span><span>P</span><span>P</span><span>I</span><span>N</span><span>Z</span><span>Q</span>
</div>`).css({
            "position": "absolute",
            "width": $("#blog-list-row").width(),
            "height": window.screen.height - ($("header").height() + $(".breadcrumb-section").height() + parseInt($(".breadcrumb-section").css("margin-bottom"))),
            "left": document.querySelector(".container").getBoundingClientRect().left,
            "top": $("header").height() + $(".breadcrumb-section").height() + parseInt($(".breadcrumb-section").css("margin-bottom"))
        }).fadeIn());
    }
}

function loadBlogList(searchParams,before=function () {},complete=function () {}){
    $.ajax({
        url:ip+":"+blogPort+"/hoppinzq?method=queryBlog&params={'blogVo':"+JSON.stringify(searchParams)+"}",
        xhrFields:{
            withCredentials:true
        },
        beforeSend:function () {
            before();
        },
        success:function (json) {
            let data=JSON.parse(json);
            if(data.code==200){
                let blogList=data.data.list;
                if(blogList.length!=0){
                    $(".total-products").append(`共找到了${data.data.recordCount}篇博客`);
                    let classReg = /[| ||]+/g;
                    $.each(blogList,function (index,blog) {
                        let blogClassId = blog.blogClass.split(classReg);
                        let blogClassName = blog.blogClassName.split(classReg);
                        $("#list-grid1").append2(`<div class="col-sm-6 col-md-4 col-lg-4 col-xl-3 mb-7">
                                    <div class="product-card">
                                        <div class="product-badge">
                                            ${blog.blogLike>100?"<span class='badge bg-primary'>精华</span>":""}
                                            <span class="badge ${blog.isCreateSelf==0?"bg-success":"bg-warning"} ">${blog.isCreateSelf==0?"原创":"转载"}</span>
                                            ${__zqBlog.getDateCha(__zqBlog.getRealDate(blog.updateTime))<7?"<span class='badge bg-success'>New</span>":""}
                                        </div>
                                        <div class="product-thumb-nail">
                                            <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                                                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                                                    "width":"182px",
                                                    "height":"182px",
                                                    "object-fit":"cover"
                                                })} 
                                            </a>
                                            <ul class="actions">
                                                <li class="action whish-list">
                                                    <button data-bs-toggle="modal" data-bs-target="#product-modal-wishlist"><i class="lar la-heart"></i></button>
                                                </li>
                                                <li class="action compare">
                                                    <button data-bs-toggle="modal" data-bs-target="#product-modal-compare"><i class="las la-sync"></i></button>
                                                </li>
                                                <li class="action quick-view">
                                                    <button data-bs-toggle="modal" data-bs-target="#product-modal"><i class="las la-eye"></i></button>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="product-content">
                                            <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>       
                                            <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>    
                                            <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>    
                                            <h4 class="product-description">${blog.description}</h4>       
                                            <div class="product-class-wrapper blog-class-grid1-bar" id="blog-class-grid1-${blog.id}"></div>                  
                                        </div>
                                    </div>
                                </div>`,function () {
                            if (blogClassId.length > 0 && blogClassName.length > 0) {
                                $.each(blogClassId, function (index__, data__) {
                                    if (blogClassName[index__] != undefined) {
                                        $(`#blog-class-grid1-${blog.id}`).append($(`<span class="badge bg-success" data-id="${data__}">${blogClassName[index__]}</span>`).css("margin-right","6px"));
                                    }
                                })
                            }
                        });

                        $("#list-grid2").append2(`<div class="col-12 mb-7">
                                            <div class="product-card">
                                                <div class="product-badge">
                                                ${blog.blogLike>100?"<span class='badge bg-primary'>精华</span>":""}
                                                <span class="badge ${blog.isCreateSelf==0?"bg-success":"bg-warning"} ">${blog.isCreateSelf==0?"原创":"转载"}</span>
                                                ${__zqBlog.getDateCha(__zqBlog.getRealDate(blog.updateTime))<7?"<span class='badge bg-success'>New</span>":""}
                                            </div>
                                            <div class="product-thumb-nail">
                                                <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">
                                                    ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                                                        "width":"182px",
                                                        "height":"182px",
                                                        "object-fit":"cover"
                                                    })} 
                                                </a>
                                                <ul class="actions">
                                                    <li class="action whish-list">
                                                        <button data-bs-toggle="modal" data-bs-target="#product-modal-wishlist"><i class="lar la-heart"></i></button>
                                                    </li>
                                                    <li class="action compare">
                                                        <button data-bs-toggle="modal" data-bs-target="#product-modal-compare"><i class="las la-sync"></i></button>
                                                    </li>
                                                    <li class="action quick-view">
                                                        <button data-bs-toggle="modal" data-bs-target="#product-modal"><i class="las la-eye"></i></button>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="product-content">
                                                <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                                                <h4 class="product-sub-title blog-author-grid2"><a>发布时间：${__zqBlog.getRealDate(blog.updateTime)}&nbsp;&nbsp;&nbsp;&nbsp;</a><a href="javaScript:void(0)">by ${blog.authorName}</a>
                                                <a href="javaScript:void(0)">&nbsp;&nbsp;&nbsp;&nbsp;喜欢：${blog.blogLike}</a><a href="javaScript:void(0)">&nbsp;&nbsp;收藏：${blog.collect}</a></h4>
                                                <div class="product-class-wrapper blog-class-grid2-bar" id="blog-class-grid2-${blog.id}"></div>
                                                <p class="blog-description-grid2">${blog.description}</p>
                                            </div>
                                        </div>
                                    </div>`,function () {
                                        if (blogClassId.length > 0 && blogClassName.length > 0) {
                                            $.each(blogClassId, function (index__, data__) {
                                                if (blogClassName[index__] != undefined) {
                                                    $(`#blog-class-grid2-${blog.id}`).append($(`<span class="badge bg-success" data-id="${data__}">${blogClassName[index__]}</span>`).css("margin-right","6px"));
                                                }
                                            })
                                        }
                        });

                    });
                    $("#list-grid1").append(`<div class="col-12 mt-7 mb-7">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item-left left-page">
                                                <a class="page-link" href="javascript:void(0)">
                                                    <span class="ion-ios-arrow-left"></span>
                                                </a>
                                            </li>
                                            <li class="page-item-right">
                                                <a class="page-link" href="javascript:void(0)">
                                                    <span class="ion-ios-arrow-right"></span>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>`);
                    $("#list-grid2").append(`<div class="col-12 mb-7">
                                        <nav aria-label="Page navigation">
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item-left left-page">
                                                    <a class="page-link" href="javascript:void(0)">
                                                        <span class="ion-ios-arrow-left"></span>
                                                    </a>
                                                </li>
                                                <li class="page-item-right right-page">
                                                    <a class="page-link" href="javascript:void(0)">
                                                        <span class="ion-ios-arrow-right"></span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </div>`);
                    let pageCount=data.data.pageCount;
                    let curPage=data.data.curPage;
                    for(let pageNum=pageCount;pageNum>0;pageNum--){
                        $(".left-page").after($(`<li class="page-item ${pageNum==curPage?"active-page":""}"><a class="page-link" data-page="${pageNum}">${pageNum}</a></li>`).off("click").on("click",function () {
                            //当前页禁用点击，并加上样式，其他去掉样式
                            $(this).addClass("active-page").siblings().removeClass("active-page");
                            searchParams.pageIndex=$(this).children("a").data("page");
                            loadBlogList(searchParams,startLoadingBlogList());
                        }));
                    }
                    // if(__zqBlog.isMobile){
                    //     $(".page-item").each(function (index_page,element_page) {
                    //         let $me=$(element_page);
                    //         if(index_page>5){
                    //             $me.addClass("display-none")
                    //         }
                    //     })
                    // }
                    $(".page-item").css("pointer-events", "all");
                    $(".active-page").css("pointer-events", "none");
                    //总页数大于1，可以左右
                    if(pageCount>1){
                        $(".page-item-left").css("pointer-events", "all");
                        $(".page-item-right").css("pointer-events", "all");
                    }
                    $(".page-item-left").on("click",function () {
                        if($(".active-page").children("a").data("page")!=1){
                            searchParams.pageIndex=$(".active-page").children("a").data("page")-1;
                            loadBlogList(searchParams,startLoadingBlogList());
                        }
                    });
                    $(".page-item-right").on("click",function () {
                        if($(".active-page").children("a").data("page")!=pageCount){
                            searchParams.pageIndex=$(".active-page").children("a").data("page")+1;
                            loadBlogList(searchParams,startLoadingBlogList());
                        }
                    })
                    __zqBlog.stopLoading(0,500);
                }else{
                    //没有博客被找到
                    alert("没有博客被找到")
                    __zqBlog.stopLoading(0,500);
                }
            }else{
                //没有博客被找到
                alert("没有博客被找到")
                __zqBlog.stopLoading(0,500);
            }
        },
        error:function () {
            __zqBlog.stopLoading(0,500);
        },
        complete:function () {
            complete();
        },
    })
}