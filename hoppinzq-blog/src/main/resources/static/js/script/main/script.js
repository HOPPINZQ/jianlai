let ipconfig=__zqBlog.ipConfig;
let ip=ipconfig.ip_;
let blogPort=ipconfig.blogPort;
let hoppinzq = ["H", "O", "P", "P", "I", "N", "Z", "Q", ":)"];
let hoppinzqColor = ["#eb4747", "#ebc247", "#99eb47", "#47eb70", "#47ebeb", "#4770eb", "#9947eb", "#eb47c2", "#eb4747"];

$(function () {
    let storageData=localStorage.getItem("zq:blog:main");
    if(storageData==null){
        $(".preloader").delay(3200).fadeOut(0,function () {
            let $preloader = $(this);
            $(this).html("")
            setTimeout(function (){
                $preloader.remove();
                $(".wiper").show(1000)
            },3200+4000);
            $preloader.removeClass("preloader_image");

            $(this).show();
            let $criterion = $("<div class='criterion'></div>");
            setTimeout(function (){
                $criterion.addClass("loader");
            },3200+5000);
            for (let i = 0; i < 9; i++) {
                $preloader.append($("<div class='background'></div>").css({
                    "left": i  * 12.5 + "%",
                    "height": "100vh",
                    "background-color": hoppinzqColor[i]
                }));
                $criterion.append($("<div class='text'>" + hoppinzq[i] + "</div>").addClass("text" + i).delay(7500)
                    .fadeIn(0,function () {
                        if(i<8){
                            $(this).after($("<span class='text'>"+hoppinzq[i]+"</span>").addClass("text" + i));
                        }
                    }));
                $criterion.append($("<div class='frame'></div>").addClass("frame" + i));
                for (let j = 0; j < 12; j++) {
                    $criterion.append($("<div class='particle'></div>").addClass("particle" + String(i) + String(j)));
                }
            }
            $preloader.append($criterion);
        });
    }else{
        __zqBlog.stopLoading();
    }

    $.ajax({
        url:ip+":"+blogPort+"/hoppinzq?method=mainBlog&params={}",
        beforeSend:function (xhr) {
            let storageData=localStorage.getItem("zq:blog:main");
            let storageDataTime=localStorage.getItem("zq:blog:main:time");
            if(storageDataTime==null) return true;
            if(storageDataTime!=null&&storageData==null)return true;
            if(storageData!=null&&storageDataTime!=null){
                let now=new Date().getTime();
                if(now-parseInt(storageDataTime)>1000*60*60*24) return true;
                loadMain(JSON.parse(storageData));
                xhr.abort();
                return false;
            }
        },
        success:function (data){
            let json=JSON.parse(data);
            if(json.code==200){
                let blogShowData=json.data;
                localStorage.setItem("zq:blog:main",JSON.stringify(blogShowData));
                localStorage.setItem("zq:blog:main:time",new Date().getTime());
                loadMain(blogShowData);
            }else{
                //首页展示失败
            }
        },
        error:function () {

        }
    });

    //今日推荐
    new Swiper(".deal-carousel-one .swiper-container", {
        loop: false,
        speed: 800,
        slidesPerView: 2,
        spaceBetween: 20,
        pagination: false,
        navigation: {
            nextEl: ".deal-carousel-one .swiper-button-next",
            prevEl: ".deal-carousel-one .swiper-button-prev",
        },
        observer: true,
        observeParents: true,

        breakpoints: {
            0: {
                slidesPerView: 1,
            },

            992: {
                slidesPerView: 1,
            },

            1200: {
                slidesPerView: 2,
            },
        },
    });

    //待补充
    new Swiper(".brand-carousel .swiper-container", {
        loop: true,
        speed: 800,
        slidesPerView: 5,
        spaceBetween: 10,
        pagination: false,
        navigation: {
            nextEl: ".brand-carousel .swiper-button-next",
            prevEl: ".brand-carousel .swiper-button-prev",
        },
        breakpoints: {
            0: {
                slidesPerView: 1,
            },

            480: {
                slidesPerView: 2,
            },

            768: {
                slidesPerView: 3,
            },
            992: {
                slidesPerView: 4,
            },
            1200: {
                slidesPerView: 5,
            },
        },
    });
})
function loadMain(blogShowData){
    setTimeout(function () {
        //发表最近的
        let recentBlogs=blogShowData.recentBlogs;
        loadRecentBlog(recentBlogs);
    },1);
    setTimeout(function () {
        //喜欢数最多的
        let likeBlogs=blogShowData.likeBlogs;
        loadLikeBlog(likeBlogs);
    },1);
    setTimeout(function () {
        //收藏最多的
        let collectBlog=blogShowData.collectBlogs;
        loadCollectBlog(collectBlog);
    },1);
    setTimeout(function () {
        //展示最多的
        let showBlog=blogShowData.showBlogs;
        loadShowBlog(showBlog);
    },1);
    setTimeout(function () {
        //Java
        let javaBlog=blogShowData.javaBlogs;
        loadJavaBlog(javaBlog);
    },1);
    setTimeout(function () {
        //Spring
        let springBlog=blogShowData.springBlogs;
        loadSpringBlog(springBlog);
    },1);
    setTimeout(function () {
        //sql
        let sqlBlog=blogShowData.sqlBlogs;
        loadSqlBlog(sqlBlog);
    },1);
}
function loadSqlBlog(sqlBlog){
    let $swiperSilde=$(`<div class="categories-carousel-item swiper-slide">`);
    for(let i=0;i<sqlBlog.length;i++){
        let blog=sqlBlog[i];
        $(`<div class="categories-card-list">
            <div class="categories-card">
                <div class="categories-thumb-nail">
                    <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
            "width":"100px",
            "height":"100px",
            "object-fit":"cover"
        })} 
                    </a>
                    <div class="quick-view-btn-wrap">
                        <button class="quick-view-btn" data-bs-toggle="modal"
                                data-bs-target="#product-modal">
                            <i class="las la-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="categories-content">
                    <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>
                    <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                    <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>
                </div>
            </div>
        </div>`).appendTo($swiperSilde);
        if(i==sqlBlog.length-1){
            $(".sql-blog").append($swiperSilde);
            break;
        }
        if((i+1)%3!=0)continue;
        $(".sql-blog").append($swiperSilde);
        $swiperSilde=$(`<div class="categories-carousel-item swiper-slide">`);
    }
    new Swiper(
        ".sql-blog-list .swiper-container",
        {
            loop: true,
            speed: 800,
            slidesPerView: 1,
            spaceBetween: 0,
            pagination: false,
            navigation: {
                nextEl: ".sql-blog-list .swiper-button-next",
                prevEl: ".sql-blog-list .swiper-button-prev",
            },
            breakpoints: {
                0: {
                    slidesPerView: 1,
                },
                576: {
                    slidesPerView: 2,
                },
                992: {
                    slidesPerView: 1,
                },
            },
        }
    );
}
function loadSpringBlog(springBlog){
    let $swiperSilde=$(`<div class="categories-carousel-item swiper-slide">`);
    for(let i=0;i<springBlog.length;i++){
        let blog=springBlog[i];
        $(`<div class="categories-card-list">
            <div class="categories-card">
                <div class="categories-thumb-nail">
                    <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                    ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                "width":"100px",
                "height":"100px",
                "object-fit":"cover"
            })} 
                    </a>
                    <div class="quick-view-btn-wrap">
                        <button class="quick-view-btn" data-bs-toggle="modal"
                                data-bs-target="#product-modal">
                            <i class="las la-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="categories-content">
                    <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>
                    <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                    <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>
                </div>
            </div>
        </div>`).appendTo($swiperSilde);
        if(i==springBlog.length-1){
            $(".spring-blog").append($swiperSilde);
            break;
        }
        if((i+1)%3!=0)continue;
        $(".spring-blog").append($swiperSilde);
        $swiperSilde=$(`<div class="categories-carousel-item swiper-slide">`);
    }
    new Swiper(
        ".spring-blog-list .swiper-container",
        {
            loop: true,
            speed: 800,
            slidesPerView: 1,
            spaceBetween: 0,
            pagination: false,
            navigation: {
                nextEl: ".spring-blog-list .swiper-button-next",
                prevEl: ".spring-blog-list .swiper-button-prev",
            },
            breakpoints: {
                0: {
                    slidesPerView: 1,
                },
                576: {
                    slidesPerView: 2,
                },
                992: {
                    slidesPerView: 1,
                },
            },
        }
    );
}
function loadJavaBlog(javaBlog){
    let $swiperSilde=$(`<div class="categories-carousel-item swiper-slide">`);
    for(let i=0;i<javaBlog.length;i++){
        let blog=javaBlog[i];
        $(`<div class="categories-card-list">
            <div class="categories-card">
                <div class="categories-thumb-nail">
                    <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                    "width":"100px",
                    "height":"100px",
                    "object-fit":"cover"
                })} 
                    </a>
                    <div class="quick-view-btn-wrap">
                        <button class="quick-view-btn" data-bs-toggle="modal"
                                data-bs-target="#product-modal">
                            <i class="las la-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="categories-content">
                    <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>
                    <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                    <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>
                </div>
            </div>
        </div>`).appendTo($swiperSilde);
        if(i==javaBlog.length-1){
            $(".java-blog").append($swiperSilde);
            break;
        }
        if((i+1)%3!=0)continue;
        $(".java-blog").append($swiperSilde);
        $swiperSilde=$(`<div class="categories-carousel-item swiper-slide">`);
    }
     new Swiper(
        ".java-blog-list .swiper-container",
        {
            loop: true,
            speed: 800,
            slidesPerView: 1,
            spaceBetween: 0,
            pagination: false,
            navigation: {
                nextEl: ".java-blog-list .swiper-button-next",
                prevEl: ".java-blog-list .swiper-button-prev",
            },
            breakpoints: {
                0: {
                    slidesPerView: 1,
                },
                576: {
                    slidesPerView: 2,
                },
                992: {
                    slidesPerView: 1,
                },
            },
        }
    );
}

function loadShowBlog(showBlogs){
    let $swiperSilde=$(`<div class="tab-carousel-item swiper-slide">`);
    for(let i=0;i<showBlogs.length;i++){
        let blog=showBlogs[i];
        $(`<div class="product-tab-card-list">
            <div class="product-tab-card">
                <div class="product-tab-thumb-nail">
                    <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                                                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                            "width":"150px",
                            "height":"150px",
                            "object-fit":"cover"
                        })} 
                                            </a>
                    <div class="quick-view-btn-wrap">
                        <button class="quick-view-btn" data-bs-toggle="modal"
                                data-bs-target="#product-modal">
                            <i class="las la-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="product-tab-content">
                    <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>
                    <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                    <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>
                    <div class="product-price-wrapp">
                        展示数：
                        <span class="product-price-on-sale">${blog.show}</span>
                    </div>
                </div>
            </div>
        </div>`).appendTo($swiperSilde);
        if(i==showBlogs.length-1){
            $(".more-show-blog").append($swiperSilde);
            break;
        }
        if(i%2==0)continue;
        $(".more-show-blog").append($swiperSilde);
        $swiperSilde=$(`<div class="tab-carousel-item swiper-slide">`);
    }
    new Swiper(".tab-blog-more-show .swiper-container", {
        loop: false,
        speed: 800,
        slidesPerView: 3,
        spaceBetween: 10,
        pagination: false,
        navigation: {
            nextEl: ".tab-blog-more-show .swiper-button-next",
            prevEl: ".tab-blog-more-show .swiper-button-prev",
        },
        observer: true,
        observeParents: true,
        breakpoints: {
            0: {
                slidesPerView: 1,
            },
            768: {
                slidesPerView: 2,
            },
            1200: {
                slidesPerView: 3,
            },
        },
    });
}

function loadCollectBlog(collectBlogs){
    let $swiperSilde=$(`<div class="tab-carousel-item swiper-slide">`);
    for(let i=0;i<collectBlogs.length;i++){
        let blog=collectBlogs[i];
        $(`<div class="product-tab-card-list">
            <div class="product-tab-card">
                <div class="product-tab-thumb-nail">
                    <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                                                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
            "width":"150px",
            "height":"150px",
            "object-fit":"cover"
        })} 
                                            </a>
                    <div class="quick-view-btn-wrap">
                        <button class="quick-view-btn" data-bs-toggle="modal"
                                data-bs-target="#product-modal">
                            <i class="las la-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="product-tab-content">
                    <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>
                    <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                    <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>
                    <div class="product-price-wrapp">
                        收藏数：
                        <span class="product-price-on-sale">${blog.collect}</span>
                    </div>
                </div>
            </div>
        </div>`).appendTo($swiperSilde);

        if(i==collectBlogs.length-1){
            $(".more-collect-blog").append($swiperSilde);
            break;
        }
        if(i%2==0)continue;
        $(".more-collect-blog").append($swiperSilde);
        $swiperSilde=$(`<div class="tab-carousel-item swiper-slide">`);
    }
    new Swiper(".tab-blog-more-collect .swiper-container", {
        loop: false,
        speed: 800,
        slidesPerView: 3,
        spaceBetween: 10,
        pagination: false,
        navigation: {
            nextEl: ".tab-blog-more-collect .swiper-button-next",
            prevEl: ".tab-blog-more-collect .swiper-button-prev",
        },
        observer: true,
        observeParents: true,
        breakpoints: {
            0: {
                slidesPerView: 1,
            },
            768: {
                slidesPerView: 2,
            },
            1200: {
                slidesPerView: 3,
            },
        },
    });
}

function loadLikeBlog(likeBlogs){
    let $swiperSilde=$(`<div class="tab-carousel-item swiper-slide">`);
    for(let i=0;i<likeBlogs.length;i++){
        let blog=likeBlogs[i];
        $(`<div class="product-tab-card-list">
            <div class="product-tab-card">
                <div class="product-tab-thumb-nail">
                    <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                                                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                            "width":"150px",
                            "height":"150px",
                            "object-fit":"cover"
                        })} 
                                            </a>
                    <div class="quick-view-btn-wrap">
                        <button class="quick-view-btn" data-bs-toggle="modal"
                                data-bs-target="#product-modal">
                            <i class="las la-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="product-tab-content">
                    <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>
                    <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                    <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>
                    <div class="product-price-wrapp">
                        喜欢数：
                        <span class="product-price-on-sale">${blog.blogLike}</span>
                    </div>
                </div>
            </div>
        </div>`).appendTo($swiperSilde);
        if(i==likeBlogs.length-1){
            $(".more-like-blog").append($swiperSilde);
            break;
        }
        if(i%2==0)continue;
        $(".more-like-blog").append($swiperSilde);
        $swiperSilde=$(`<div class="tab-carousel-item swiper-slide">`);
    }
    new Swiper(".tab-blog-more-like .swiper-container", {
        loop: false,
        speed: 800,
        slidesPerView: 3,
        spaceBetween: 10,
        pagination: false,
        navigation: {
            nextEl: ".tab-blog-more-like .swiper-button-next",
            prevEl: ".tab-blog-more-like .swiper-button-prev",
        },
        observer: true,
        observeParents: true,
        breakpoints: {
            0: {
                slidesPerView: 1,
            },
            768: {
                slidesPerView: 2,
            },
            1200: {
                slidesPerView: 3,
            },
        },
    });
}

function loadRecentBlog(recentBlogs){
    let $swiperSilde=$(`<div class="product-carousel-item swiper-slide">`);
    for(let i=0;i<recentBlogs.length;i++){
        let blog=recentBlogs[i];
        $(`<div class="product-card-list" style="max-height: 333px;">
                        <div class="product-card">
                            <div class="product-thumb-nail">
                                <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}" style="padding-left: 8%">
                                                ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
            "width":"176px",
            "height":"176px",
            "object-fit":"cover"
        })} 
                                            </a>
                                <ul class="actions">
                                    <li class="action whish-list">
                                        <button data-bs-toggle="modal"
                                                data-bs-target="#product-modal-wishlist"><i
                                                class="lar la-heart"></i></button>
                                    </li>
                                    <li class="action compare">
                                        <button data-bs-toggle="modal"
                                                data-bs-target="#product-modal-compare"><i
                                                class="las la-sync"></i></button>
                                    </li>
                                    <li class="action quick-view">
                                        <button data-bs-toggle="modal"
                                                data-bs-target="#product-modal"><i
                                                class="las la-eye"></i></button>
                                    </li>
                                </ul>
                            </div>
                            <div class="product-content">
                                <h4 class="product-sub-title"><a href="javaScript:void(0)">by ${blog.authorName}</a></h4>
                                <h3 class="product-title"><a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/blog/"+blog.id}">${blog.title}</a></h3>
                                <h4 class="product-sub-title">发布时间：${__zqBlog.getRealDate(blog.updateTime)}</h4>
                                <div class="product-cart-btn-wrap">
                                    <button data-bs-toggle="modal"
                                            data-bs-target="#addto-cart-modal"
                                            class="btn btn-dark add-to-cart-btn">未实现
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>`).appendTo($swiperSilde);
        if(i==recentBlogs.length-1){
            $(".blog-recent-list").append($swiperSilde);
            break;
        }
        if(i%2==0)continue;
        $(".blog-recent-list").append($swiperSilde);
        $swiperSilde=$(`<div class="product-carousel-item swiper-slide">`);
    }
    new Swiper(".food-carousel-five-items .swiper-container",
        {
            loop: false,
            speed: 800,
            slidesPerView: 5,
            spaceBetween: 20,
            navigation: {
                nextEl: ".food-carousel-five-items .swiper-button-next",
                prevEl: ".food-carousel-five-items .swiper-button-prev",
            },
            breakpoints: {
                0: {
                    slidesPerView: 1,
                },
                480: {
                    slidesPerView: 2,
                },
                992: {
                    slidesPerView: 3,
                },
                1200: {
                    slidesPerView: 4,
                },
                1500: {
                    slidesPerView: 5,
                },
            },
        }
    );
}