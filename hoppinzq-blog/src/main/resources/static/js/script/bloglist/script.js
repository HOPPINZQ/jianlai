let ipconfig=__zqBlog.ipConfig;
let ip=ipconfig.ip_;
let blogPort=ipconfig.blogPort;

$(function () {
    let searchParams="'searchType':1,'blogReturn':1";
    //解析url，赋值
    let search=__zqBlog.getWebURLKey("s");
    let search_class=__zqBlog.getWebURLKey("c");
    let search_class_name=__zqBlog.getWebURLKey("cn");
    let page_index=__zqBlog.getWebURLKey("p");
    if(search!=null){
        $(".search-form input").val(window.decodeURIComponent(search));
        searchParams+=",'search':'"+window.decodeURIComponent(search)+"'";
    }
    if(page_index==null){
        searchParams+=",'pageIndex':1";
    }else{
        searchParams+=",'pageIndex':"+page_index;
    }
    //由于类别是在脚本填充的，所以要轮询，当填充好后选中url上的类别
    if(search_class!=null){
        searchParams+=",'_class':'"+window.decodeURIComponent(search_class)+"'";
        let search_intervel_id=__zqBlog.zinterval(function () {
            if($("#autoSizingSelect").find("option").length>1){
                clearInterval(search_intervel_id);
                $("#autoSizingSelect").find("option[value='"+search_class+"']").attr("selected",true);
            }
        },500);
    }
    if(search_class_name!=null&&search_class!=null&&search_class!=0){
        search_class_name=window.decodeURIComponent(search_class_name);
        search_class_name=search_class_name.replace("--","").replace("--","").trim();
        searchParams+=",'_class_name':'"+search_class_name+"'";
    }
    $.ajax({
        url:ip+":"+blogPort+"/hoppinzq?method=queryBlog&params={'blogVo':{"+searchParams+"}}",
        success:function (json) {
            let data=JSON.parse(json);
            let blogList=data.data.list;
            if(blogList.length!=0){
                $(".total-products").append(`共找到了${data.data.recordCount}篇博客`)
                $.each(blogList,function (index,blog) {
                    $("#list-grid1").append(`<div class="col-sm-6 col-md-4 col-lg-4 col-xl-3 mb-7">
                                    <div class="product-card">
                                        <span class="badge bg-success product-badge">new</span>
                                        <div class="product-thumb-nail">
                                            <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                                                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath)} 
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
                                            <h4 class="product-sub-title"><a href="#">by ${blog.authorName}</a></h4>    
                                            <div class="product-price-wrapp blog-class-grid2-bar" id="blog-class-grid1-${blog.id}"></div>                  
                                        </div>
                                    </div>
                                </div>`);

                    $("#list-grid2").append(`<div class="col-12 mb-7">
                                        <div class="product-card">
                                            <span class="badge bg-success product-badge">new</span>
                                            <div class="product-thumb-nail">
                                                <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">
                                                    ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath)} 
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
                                                <h4 class="product-sub-title blog-author-grid2"><a href="#">by ${blog.authorName}</a></h4>
                                                <div class="product-price-wrapp blog-class-grid2-bar" id="blog-class-grid2-${blog.id}"></div>
                                                <p class="blog-description-grid2">${blog.description}</p>
                                            </div>
                                        </div>
                                    </div>`);
                    let classReg = /[| ||]+/g;
                    let blogClassId = blog.blogClass.split(classReg);
                    let blogClassName = blog.blogClassName.split(classReg);
                    if (blogClassId.length > 0 && blogClassName.length > 0) {
                        $.each(blogClassId, function (index__, data__) {
                            if (blogClassName[index__] != undefined) {
                                $(`#blog-class-grid1-${blog.id}`).append($(`<span class="badge bg-success" data-id="${data__}">${blogClassName[index__]}</span>`).css("margin-right","6px"));
                                $(`#blog-class-grid2-${blog.id}`).append($(`<span class="badge bg-success" data-id="${data__}">${blogClassName[index__]}</span>`).css("margin-right","6px"));
                            }
                        })
                    }
                });
                $("#list-grid1").append(`<div class="col-12 mt-7 mb-7">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item active left-page">
                                                <a class="page-link" href="#">
                                                    <span class="ion-ios-arrow-left"></span>
                                                </a>
                                            </li>
                                            <li class="page-item">
                                                <a class="page-link" href="#">
                                                    <span class="ion-ios-arrow-right"></span>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>`);
                $("#list-grid2").append(`<div class="col-12 mb-7">
                                        <nav aria-label="Page navigation">
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item active left-page">
                                                    <a class="page-link" href="#">
                                                        <span class="ion-ios-arrow-left"></span>
                                                    </a>
                                                </li>
                                                <li class="page-item">
                                                    <a class="page-link" href="#">
                                                        <span class="ion-ios-arrow-right"></span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </div>`);
                let pageCount=data.data.pageCount;
                for(let pageNum=1;pageNum<=pageCount;pageNum++){
                    $(".left-page").after(`<li class="page-item"><a class="page-link" href="#">${pageNum}</a></li>`);
                }
            }
            console.log(data)
        }

    })


})