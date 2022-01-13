//公用对象——一些不高兴的狗 by HOPPIN&HAZZ ~ZQ
var __zqBlog = {
    user:null,//当前登录人，取该值为null表示没有获取到
    ipConfig: {
        ip: "127.0.0.1",
        ip_: "http://127.0.0.1",
        blogPort:"8809",
        fileIP:"150.158.28.40",
        fileProxyServer: "150.158.28.40:9000",
        fileProxyServer_: "http://150.158.28.40:9000",
        //fileServer_:"http://150.158.28.40:8090",
        //fileServer_:"https://hoppinzq.com:8090",
        //fileServer_: "http://150.158.28.40:8090",
        fileServer_: "http://150.158.28.40:8090",
        //fileServer_:"http://hoppinzq.com/file_server",//代理至150.158.28.40:8090
        errorImagePath:"http://150.158.28.40:9000/404/nopic.jpg",
    },
    isDebugger: true,//是否调试模式
    isCookie: true,//是否支持cookie
    isMobile: false,//是否是移动端
    isPdfView: true,//是否支持PDF预览
    isOnLine: true,//是否在线/脱机
    isWebSocket: true,//是否支持webSocket
    isStorage: true,//是否支持Storage
    isIndexedDB: true,//是否支持indexedDB
    isWifi: true,//使用的是否是流量
    //通过json文件配置的页面，json文件的路径
    json: {
        classJsonPath1: "/static/json/classJSON.json",
        classJsonPath2: "/static/json/barLinkJSON.json",
        mainBarJsonPath1:"/static/json/mainJSON1.json",
        classSwiperJsonPath1:"/static/json/swiperJSON1.json",
        todayRecommendBlogJsonPath1:"/static/json/todayRecommendJSON1.json",
    },
    /**
     * 调试模式，当配置项的isDebugger为true时将开启调试模式
     * @param sMessage 内部返回调试信息
     * @param bError 调试级别是否是错误
     * @private
     */
    _debug: function (sMessage, bError) {
        if (!this.isDebugger) return;
        if (bError) {
            console.error(sMessage);
            return;
        }
        console.log(sMessage);
    },
    /**
     * 是否是undefined
     * @param {Object} obj
     */
    isUndefined: function (obj) {
        if (typeof (obj) == "undefined") {
            return true;
        } else {
            return false;
        }
    },
    /**
     * 是否是null
     * @param {Object} obj
     */
    isNull: function (obj) {
        if (!obj && typeof (obj) != "undefined" && obj != 0) {
            return true;
        } else {
            return false;
        }
    },
    /**
     * 该方法将返回元素类型
     * @param {Object} obj
     */
    elementType: function (obj) {
        return Object.prototype.toString.call(obj).replace(/^\[object (.+)\]$/, '$1').toLowerCase();
    },
    /**
     * 加载图片,并创建图片对象到dom内，请求不到的图片资源使用404图片替换之
     * @param {Object} url
     * @param {Object} dom
     */
    loadImageAddDom: function (url, dom,errorUrl) {
        let me = this;
        let image = new Image();
        image.src = url;
        image.onload = function () {
            //在这里用this指向的是image对象
            if (!me.isUndefined(dom)) {
                $(dom).append(this);
            }
        };
        image.onerror = function (e) {
            image.src = errorUrl||me.ipConfig.errorImagePath;
            if (!me.isUndefined(dom)) {
                $(dom).append(image);
            }
        };
    },
    /**
     * 加载图片，请求不到的图片资源使用404图片替换之
     * @param {Object} url
     * @param {Object} dom
     */
    loadImage: function (url,className,alt,errorUrl,style) {
        let me = this;
        let id=me.uuid(32,62);
        let image = new Image();
        image.src = url;
        if(url.indexOf("127.0.0.1")){
            url.replace("127.0.0.1",me.ipConfig.fileIP);
        }
        $(image).addClass("image-"+id).addClass(className).attr("alt",alt);
        if(style){
           for(let cssKey in style){
               $(image).css(cssKey,style[cssKey]);
           }
        }
        image.onload = function () {};
        image.onerror = function (e) {
            $(".image-"+id).attr("src",errorUrl||me.ipConfig.errorImagePath);
        };
        return image.outerHTML;
    },
    /**
     * 定时器,当达到一定条件可以在callback关闭定时器
     * @param {Object} callback 这个回调函数会有一个id，通过clearInterval(id)关闭定时器
     * @param {Object} time
     */
    zinterval: function (callback, time) {
        let id = setInterval(() => callback(id), time);
    },
    /**
     * 模板
     * @param {Object} html
     * @param {Object} data
     */
    loadTemplete(html, data) {
        let reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
        let source = html.replace(reg, function (node, key) {
            console.log("node:" + node + ",key:" + key)
            return data[key];
        });
        $("body").append(source);
    },
    /**
     * 控制方法执行顺序
     * @param {Object} n
     * 该方法返回promise对象，也可以链式追加then
     */
    timeout: function (n = 0) {
        return new Promise(function (resolve) {
            setTimeout(resolve, n);
        });
    },
    /**
     * js方法代理增强
     * @param originFun 要代理的方法
     * @param before 在方法执行前执行的方法
     * @param after 在方法执行后执行的方法
     * @returns {_class}
     */
    constructorJS: function (originFun, before, after) {
        function _class() {
            before.apply(this, arguments);
            originFun.apply(this, arguments);
            after.apply(this, arguments);
        }

        return _class;
    },
    /**
     * 生成uuid
     * @param len
     * @param radix
     * @returns {string}
     */
    uuid: function (len, radix) {
        let chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        let uuid = [], i;
        radix = radix || chars.length;
        if (len) {
            for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
        } else {
            let r;
            uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
            uuid[14] = '4';
            for (i = 0; i < 36; i++) {
                if (!uuid[i]) {
                    r = 0 | Math.random() * 16;
                    uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
                }
            }
        }
        return uuid.join('');
    },

    /**
     * 解析url
     * @param variable
     * @returns {string|null}
     */
    getWebURLKey: function (variable) {
        let query = window.location.search.substring(1);
        let vars = query.split("&");
        for (let i = 0; i < vars.length; i++) {
            let pair = vars[i].split("=");
            if (pair[0] == variable) {
                return this.reomveJing(pair[1]);
            }
        }
        return null;
    },
    /**
     * 去掉#
     * @param str
     */
    reomveJing:function (str) {
        return str.lastIndexOf("#")==str.length-1?str.substring(0,str.length-1):str;
    },
    /**
     * 设置只允许单播放源，一个媒体标签播放则暂停其他媒体标签播放
     */
    soundControl: function () {
        let audios = document.getElementsByTagName("audio");

        // 暂停函数
        function pauseAll() {
            let self = this;
            [].forEach.call(audios, function (i) {
                // 将audios中其他的audio全部暂停
                i !== self && i.pause();
            })
        }

        // 给play事件绑定暂停函数
        [].forEach.call(audios, function (i) {
            i.addEventListener("play", pauseAll.bind(i));
        });
    },
    /**
     * 补零
     * @param {Object} num
     */
    addZero: function (num) {
        if (parseInt(num) < 10) {
            num = '0' + num;
        }
        return num;
    },

    /**
     * 时间戳转日期
     * @param {Object} str
     */
    getRealDate: function (str) {
        let oDate = new Date(str);
        if (str === undefined) {
            oDate = new Date();
        }
        let oYear = oDate.getFullYear(),
            oMonth = oDate.getMonth() + 1,
            oDay = oDate.getDate(),
            oHour = oDate.getHours(),
            oMin = oDate.getMinutes(),
            oSen = oDate.getSeconds(),
            oTime = oYear + '-' + this.addZero(oMonth) + '-' + this.addZero(oDay) + ' ' + this.addZero(oHour) + ':' +
                this.addZero(oMin) + ':' + this.addZero(oSen);
        return oTime;
    },
    /**
     * 获取当前登录人，返回null表示没有获取到当前登录人
     */
    getUser:function () {
        $.ajax({
            url:__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/getUser",
            async:false,
            xhrFields:{
              withCredentials:true
            },
            success:function (data) {
                if(data.code!=500){
                    return data;
                }else{
                    return null;
                }
            },
            error:function (){
                _zqError("获取用户信息失败");
                return null;
            }
        })
    },
    //关闭遮罩
    stopLoading:function (d=2000,f=1000) {
        $(".preloader").delay(d).fadeOut(f);
    },
    fileSizeFormat:function (size) {
        if(size){
            let units = 'B';
            if (size / 1024 > 1) {
                size = size / 1024;
                units = 'KB';
            }
            if (size / 1024 > 1) {
                size = size / 1024;
                units = 'MB';
            }
            if (size / 1024 > 1) {
                size = size / 1024;
                units = 'GB';
            }
            return size.toFixed(1) + units;
        }else{
            return "未知的文件大小";
        }
    }

}

//该js是核心脚本，各个页面公用的js
$(function () {
    "use strict";
    //为window对象原型添加方法用以打印日志
    $.extend(window, {
        _zqLog: console.log.bind(console),
        _zqError: console.error.bind(console),
        _zqDir: console.dir.bind(console),
    });

    //为JQuery拓展append方法，当dom元素填充完毕触发回调
    $.fn.append2 = function(html, callback) {
        let originalHtmlLength = $("body").html().length;
        this.append(html);
        let nums = 1;
        let timer1 = setInterval(function() {
            nums++;
            let clearIntervalfun = function() {
                clearInterval(timer1)
                callback();
            }
            let flag = originalHtmlLength != $("body").html().length || nums > 1000;
            flag && clearIntervalfun()
        }, 1)
    };

    //为JQuery拓展prepend方法，当dom元素填充完毕触发回调
    $.fn.prepend2 = function(html, callback) {
        let originalHtmlLength = $("body").html().length;
        this.prepend(html);
        let nums = 1;
        let timer1 = setInterval(function() {
            nums++;
            let clearIntervalfun = function() {
                clearInterval(timer1)
                callback();
            }
            let flag = originalHtmlLength != $("body").html().length || nums > 1000;
            flag && clearIntervalfun()
        }, 1)
    };

    _zqLog("\n %c hoppinzq开源 %c https://gitee.com/hoppin \n\n", "background: #35495e; padding: 1px; border-radius: 3px 0 0 3px; color: #fff", "background: #fadfa3; padding: 1px; border-radius: 0 3px 3px 0; color: #fff");

    if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        //是否是移动端
        __zqBlog.isMobile = true;
    } else {
        __zqBlog.isMobile = false;
    }
    //是否支持cookie
    if (navigator.cookieEnabled) {
        __zqBlog.isCookie = true;
    } else {
        __zqBlog.isCookie = false;
    }
    //是否联网/脱机
    if (navigator.onLine) {
        __zqBlog.isOnLine = true;
    } else {
        __zqBlog.isOnLine = false;
    }
    //是否支持pdf在线预览
    if (navigator.pdfViewerEnabled) {
        __zqBlog.isPdfView = true;
    } else {
        __zqBlog.isPdfView = false;
    }

    //window对象三种方式哦
    //是否支持WebSocket
    if ('WebSocket' in window) {
        __zqBlog.isWebSocket = true;
    } else {
        __zqBlog.isWebSocket = false;
    }
    //是否支持storage存储或者是否开启了隐私模式之类的
    if (typeof (Storage) !== "undefined") {
        __zqBlog.isStorage = true;
    } else {
        __zqBlog.isStorage = false;
    }
    //是否支持indexedDB存储
    if (!window.indexedDB) {
        __zqBlog.isIndexedDB = true;
    } else {
        __zqBlog.isIndexedDB = true;
    }

    //判断当前使用的是否是4G流量
    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection || {
        tyep: 'unknown'
    };
    if (connection.effectiveType === '3G' || connection.effectiveType === '4G') {
        __zqBlog.isWifi = false;
    } else {
        __zqBlog.isWifi = true;
    }

    //获取当前用户，获取不到为null
    $.ajax({
        url:__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/getUser",
        async:false,
        xhrFields: {
            withCredentials: true
        },
        success:function (data) {
            if(data.code!=500){
                __zqBlog.user=data;
            }else{
                __zqBlog.user=null;
            }
            initUser();
        }
    });

    initMainWapper();
    // ajax统一针对响应码处理数据记录日志
    // $.setZjaxSettings({
    //     statusCode: {
    //         404: function () {
    //             //没有服务
    //             //__zqBlog._debug("没有服务!");
    //         },
    //         500: function () {
    //             //服务端失败
    //             //__zqBlog._debug("服务端失败!");
    //         },
    //         200: function () {
    //             //成功
    //             //__zqBlog._debug("成功!");
    //         },
    //         302: function (data) {
    //             //重定向
    //             //__zqBlog._debug("重定向!");
    //         },
    //         304: function (data) {
    //             //缓存
    //             //__zqBlog._debug("缓存!");
    //         },
    //         0: function (data) {
    //             //请求被意外终止
    //             //__zqBlog._debug("请求被意外终止!");
    //         }
    //     }
    // });

    //主题，目前就日间模式夜间模式，夜间模式很简陋，在dark.css加样式就行了
    let user_style = localStorage.getItem("zqblog_user_style");
    if (user_style != null) {
        $('#theme-style').attr('href', "/static/css/themes/" + user_style + ".css");
        if (user_style == "white") {
            $(".setup-show-theme").html(`<i class="icon las la-sun"></i>日间模式`);
            $(".setup-show-theme-pc").html(`<i class="icon las la-sun"></i><span>日间模式</span> <i class="ion-chevron-down"></i>`)
        } else if (user_style == "dark") {
            $(".setup-show-theme").html(`<i class="icon las la-moon"></i>夜间模式`);
            $(".setup-show-theme-pc").html(`<i class="icon las la-moon"></i><span>夜间模式</span> <i class="ion-chevron-down"></i>`)
        }
    }

    //主题切换
    $(".setup-theme").click(function () {
        let $me = $(this);
        let theme = $me.data("theme");//white跟dark
        if (theme == "white") {
            $(".setup-show-theme").html(`<i class="icon las la-sun"></i>日间模式`);
            $(".setup-show-theme-pc").html(`<i class="icon las la-sun"></i><span>日间模式</span> <i class="ion-chevron-down"></i>`)
        } else if (theme == "dark") {
            $(".setup-show-theme").html(`<i class="icon las la-moon"></i>夜间模式`);
            $(".setup-show-theme-pc").html(`<i class="icon las la-moon"></i><span>夜间模式</span> <i class="ion-chevron-down"></i>`)
        }
        localStorage.setItem("zqblog_user_style", theme);
        $('#theme-style').attr('href', "assets/css/themes/" + theme + ".css");
    });

    //
    var $sToggle = $(".search-btn-mobile");
    var $searchBody = $(".search-body");
    $sToggle.on("click", function (e) {
        e.preventDefault();
        $searchBody.slideToggle();
    });

    //下拉
    var activeSticky = $("#active-sticky"),
        winDow = $(window);
    winDow.on("scroll", function () {
        var scroll = $(window).scrollTop(),
            isSticky = activeSticky;
        $("#active-sticky2").removeClass("is-sticky-mobile");
        if (scroll < 1) {
            isSticky.removeClass("is-sticky");
        } else {
            isSticky.addClass("is-sticky");
            $("#active-sticky2").addClass("is-sticky-mobile");
        }
    });

    //移动端设置
    var $offcanvasMenu2 = $("#offcanvas-menu2 li a");
    $offcanvasMenu2.on("click", function (e) {
        // e.preventDefault();
        $(this).closest("li").toggleClass("active");
        $(this).closest("li").siblings().removeClass("active");
        $(this).closest("li").siblings().children(".category-sub-menu").slideUp();
        $(this)
            .closest("li")
            .siblings()
            .children(".category-sub-menu")
            .children("li")
            .toggleClass("active");
        $(this)
            .closest("li")
            .siblings()
            .children(".category-sub-menu")
            .children("li")
            .removeClass("active");
        $(this).parent().children(".category-sub-menu").slideToggle();
    });

    var $offcanvasMenu3 = $("#offcanvas-menu3 li a");
    $offcanvasMenu3.on("click", function (e) {
        // e.preventDefault();
        $(this).closest("li").toggleClass("active");
        $(this).closest("li").siblings().removeClass("active");
        $(this).closest("li").siblings().children(".category-sub-menu").slideUp();
        $(this)
            .closest("li")
            .siblings()
            .children(".category-sub-menu")
            .children("li")
            .toggleClass("active");
        $(this)
            .closest("li")
            .siblings()
            .children(".category-sub-menu")
            .children("li")
            .removeClass("active");
        $(this).parent().children(".category-sub-menu").slideToggle();
    });
    /*-----------------------------
     # Category more toggle
  -------------------------------*/

    $(".category-menu li.hidden").hide();
    $("#more-btn").on("click", function (e) {
        e.preventDefault();
        $(".category-menu li.hidden").toggle(500);
        var htmlAfter =
            '<i class="ion-ios-minus-empty" aria-hidden="true"></i> 收起';
        var htmlBefore =
            '<i class="ion-ios-plus-empty" aria-hidden="true"></i> 更多分类';

        if ($(this).html() == htmlAfter) {
            $(this).html(htmlBefore);
        } else {
            $(this).html(htmlAfter);
        }
    });

    /*----------------------------
          All Category toggle
       ------------------------------*/

    $(".more-btn").on("click", function (e) {
        $(".category-menu").slideToggle(300);
    });
    $(".menu-item-has-children-1").on("click", function (e) {
        $(".category-mega-menu-1").slideToggle("slow");
    });
    $(".menu-item-has-children-2").on("click", function (e) {
        $(".category-mega-menu-2").slideToggle("slow");
    });
    $(".menu-item-has-children-3").on("click", function (e) {
        $(".category-mega-menu-3").slideToggle("slow");
    });
    $(".menu-item-has-children-4").on("click", function (e) {
        $(".category-mega-menu-4").slideToggle("slow");
    });
    $(".menu-item-has-children-5").on("click", function (e) {
        $(".category-mega-menu-5").slideToggle("slow");
    });
    $(".menu-item-has-children-6").on("click", function () {
        $(".category-mega-menu-6").slideToggle("slow");
    });
    /*-----------------------------
                  Category more toggle
            -------------------------------*/

    var tooltipTriggerList = [].slice.call(
        document.querySelectorAll('[data-bs-toggle="tooltip"]')
    );
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    /*-----------------------------------
    # brand-carousel
    ------------------------------ */

    var brandCarousel = new Swiper(".brand-carousel .swiper-container", {
        loop: true,
        speed: 800,
        slidesPerView: 5,
        spaceBetween: 10,
        pagination: false,
        navigation: {
            nextEl: ".brand-carousel .swiper-button-next",
            prevEl: ".brand-carousel .swiper-button-prev",
        },
        // Responsive breakpoints
        breakpoints: {
            // when window width is >= 320px
            0: {
                slidesPerView: 1,
            },

            // when window width is >= 480px
            480: {
                slidesPerView: 2,
            },

            768: {
                slidesPerView: 3,
            },

            // when window width is >= 640px
            992: {
                slidesPerView: 4,
            },
            1200: {
                slidesPerView: 5,
            },
        },
    });

    /*-----------------------------------
    # food-category-carousel
    ------------------------------ */

    // var foodCategoryCarousel = new Swiper(
    //     ".food-category-carousel .swiper-container",
    //     {
    //         loop: false,
    //         speed: 800,
    //         slidesPerView: 6,
    //         spaceBetween: 10,
    //         navigation: {
    //             nextEl: ".food-category-carousel .swiper-button-next",
    //             prevEl: ".food-category-carousel .swiper-button-prev",
    //         },
    //         // Responsive breakpoints
    //         breakpoints: {
    //             // when window width is >= 320px
    //             0: {
    //                 slidesPerView: 1,
    //             },
    //
    //             480: {
    //                 slidesPerView: 2,
    //             },
    //             // when window width is >= 640px
    //             768: {
    //                 slidesPerView: 3,
    //             },
    //             992: {
    //                 slidesPerView: 4,
    //             },
    //
    //             // when window width is >= 640px
    //             1200: {
    //                 slidesPerView: 6,
    //             },
    //         },
    //     }
    // );

    /*-----------------------------------
    # food carousel four items
    ------------------------------ */

    var foodCarouselFourItems = new Swiper(
        ".food-carousel-four-items .swiper-container",
        {
            loop: false,
            speed: 800,
            slidesPerView: 4,
            spaceBetween: 20,
            navigation: {
                nextEl: ".food-carousel-four-items .swiper-button-next",
                prevEl: ".food-carousel-four-items .swiper-button-prev",
            },
            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
                0: {
                    slidesPerView: 1,
                },

                480: {
                    slidesPerView: 2,
                },
                992: {
                    slidesPerView: 3,
                },

                // when window width is >= 640px
                1200: {
                    slidesPerView: 4,
                },
            },
        }
    );

    /*-----------------------------------
    # food carousel five items
    ------------------------------ */

    var foodCarouselFiveItems = new Swiper(
        ".food-carousel-five-items .swiper-container",
        {
            loop: false,
            speed: 800,
            slidesPerView: 5,
            spaceBetween: 20,
            navigation: {
                nextEl: ".food-carousel-five-items .swiper-button-next",
                prevEl: ".food-carousel-five-items .swiper-button-prev",
            },
            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
                0: {
                    slidesPerView: 1,
                },

                480: {
                    slidesPerView: 2,
                },
                992: {
                    slidesPerView: 3,
                },

                // when window width is >= 640px
                1200: {
                    slidesPerView: 4,
                },
                1500: {
                    slidesPerView: 5,
                },
            },
        }
    );

    /*-----------------------------------
    # food carousel six items
    ------------------------------ */

    var foodCarouselSixItems = new Swiper(
        ".food-carousel-six-items .swiper-container",
        {
            loop: false,
            speed: 800,
            slidesPerView: 6,
            spaceBetween: 20,
            navigation: {
                nextEl: ".food-carousel-six-items .swiper-button-next",
                prevEl: ".food-carousel-six-items .swiper-button-prev",
            },
            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
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

                // when window width is >= 640px
                1200: {
                    slidesPerView: 5,
                },
                1500: {
                    slidesPerView: 6,
                },
            },
        }
    );

    /*-----------------------------------
    # tab carousel
    ------------------------------ */

    var tabCarousel = new Swiper(".tab-carousel .swiper-container", {
        loop: false,
        speed: 800,
        slidesPerView: 3,
        spaceBetween: 10,
        pagination: false,
        navigation: {
            nextEl: ".tab-carousel .swiper-button-next",
            prevEl: ".tab-carousel .swiper-button-prev",
        },
        observer: true,
        observeParents: true,
        // Responsive breakpoints
        breakpoints: {
            // when window width is >= 320px
            0: {
                slidesPerView: 1,
            },
            // when window width is >= 640px
            768: {
                slidesPerView: 2,
            },

            // when window width is >= 640px
            1200: {
                slidesPerView: 3,
            },
        },
    });

    /*-----------------------------------
    # dealCarouselOne
    ------------------------------ */

    var dealCarouselOne = new Swiper(".deal-carousel-one .swiper-container", {
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

    /*-----------------------------------
    # dealCarouseltwo
    ------------------------------ */

    var dealCarouseltwo = new Swiper(".deal-carousel-two .swiper-container", {
        loop: false,
        speed: 800,
        slidesPerView: 1,
        spaceBetween: 20,
        pagination: false,
        navigation: {
            nextEl: ".deal-carousel-two .swiper-button-next",
            prevEl: ".deal-carousel-two .swiper-button-prev",
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
                slidesPerView: 1,
            },
        },
    });

    /*-----------------------------------
    # featured Carousel
    ------------------------------ */

    var featuredCarousel = new Swiper(".featured-carousel .swiper-container", {
        loop: false,
        speed: 800,
        slidesPerView: 6,
        spaceBetween: 10,
        pagination: false,
        navigation: {
            nextEl: ".featured-carousel .swiper-button-next",
            prevEl: ".featured-carousel .swiper-button-prev",
        },
        observer: true,
        observeParents: true,
        // Responsive breakpoints
        breakpoints: {
            // when window width is >= 320px
            0: {
                slidesPerView: 1,
                loop: true,
                autoplay: {
                    delay: 2000,
                },
                speed: 1000,
            },
            // when window width is >= 480px
            480: {
                slidesPerView: 2,
                loop: true,
                speed: 1000,
            },
            // when window width is >= 640px
            657: {
                slidesPerView: 3,
            },
            992: {
                slidesPerView: 4,
            },

            // when window width is >= 640px
            1200: {
                slidesPerView: 6,
            },
        },
    });
    /*-----------------------------------
    # featured Carousel
    ------------------------------ */

    var newArrivalCarousel = new Swiper(
        ".new-arrival-carousel .swiper-container",
        {
            loop: false,
            speed: 800,
            slidesPerView: 5,
            spaceBetween: 10,
            pagination: false,
            navigation: {
                nextEl: ".new-arrival-carousel .swiper-button-next",
                prevEl: ".new-arrival-carousel .swiper-button-prev",
            },
            observer: true,
            observeParents: true,
            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
                0: {
                    slidesPerView: 1,
                    loop: true,
                    autoplay: {
                        delay: 2000,
                    },
                    speed: 1000,
                },
                // when window width is >= 480px
                480: {
                    slidesPerView: 2,
                    loop: true,
                    speed: 1000,
                },
                // when window width is >= 640px
                657: {
                    slidesPerView: 3,
                },
                992: {
                    slidesPerView: 4,
                },

                // when window width is >= 640px
                1200: {
                    slidesPerView: 5,
                },
            },
        }
    );

    /*-----------------------------------
    # categories carousel
    ------------------------------ */

    var categoriesCarousel = new Swiper(
        ".categories-carousel .swiper-container",
        {
            loop: true,
            speed: 800,
            slidesPerView: 1,
            spaceBetween: 0,
            pagination: false,
            navigation: {
                nextEl: ".categories-carousel .swiper-button-next",
                prevEl: ".categories-carousel .swiper-button-prev",
            },
            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
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
    /*-----------------------------------
    # categories carousel2
    ------------------------------ */

    var categoriesCarousel2 = new Swiper(
        ".categories-carousel2 .swiper-container",
        {
            loop: true,
            speed: 800,
            slidesPerView: 1,
            spaceBetween: 0,
            pagination: false,
            navigation: {
                nextEl: ".categories-carousel2 .swiper-button-next",
                prevEl: ".categories-carousel2 .swiper-button-prev",
            },

            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
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
    /*-----------------------------------
    # categories carousel3
    ------------------------------ */

    var categoriesCarousel3 = new Swiper(
        ".categories-carousel3 .swiper-container",
        {
            loop: true,
            speed: 800,
            slidesPerView: 1,
            spaceBetween: 0,
            pagination: false,
            navigation: {
                nextEl: ".categories-carousel3 .swiper-button-next",
                prevEl: ".categories-carousel3 .swiper-button-prev",
            },

            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
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

    /*-----------------------------------
    # categories carousel4
    ------------------------------ */

    var categoriesCarousel3 = new Swiper(
        ".categories-carousel4 .swiper-container",
        {
            loop: true,
            speed: 800,
            slidesPerView: 1,
            spaceBetween: 0,
            pagination: false,
            navigation: {
                nextEl: ".categories-carousel4 .swiper-button-next",
                prevEl: ".categories-carousel4 .swiper-button-prev",
            },

            // Responsive breakpoints
            breakpoints: {
                // when window width is >= 320px
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

    // swiper thumb gallery

    var galleryThumbs = new Swiper(
        ".product-modal-gallery-thumbs .swiper-container",
        {
            spaceBetween: 0,
            slidesPerView: 4,
            loop: false,
            watchSlidesVisibility: true,
            watchSlidesProgress: true,
        }
    );

    var galleryTop = new Swiper(".product-modal-gallery .swiper-container", {
        //spaceBetween: 1,
        spaceBetween: 0,
        loop: false,
        navigation: {
            nextEl: ".product-modal-gallery .swiper-button-next",
            prevEl: ".product-modal-gallery .swiper-button-prev",
        },
        thumbs: {
            swiper: galleryThumbs,
        },
    });
    // swiper thumb gallery

    var Thumbs = new Swiper(".gallery-thumbs .swiper-container", {
        spaceBetween: 0,
        slidesPerView: 4,
        loop: false,
        watchSlidesVisibility: true,
        watchSlidesProgress: true,
    });

    var galleryTop2 = new Swiper(".gallery .swiper-container", {
        //spaceBetween: 1,
        spaceBetween: 0,
        loop: false,
        navigation: {
            nextEl: ".gallery .swiper-button-next",
            prevEl: ".gallery .swiper-button-prev",
        },
        thumbs: {
            swiper: Thumbs,
        },
    });

    /*----------------------------------------*/

    /*  Countdown
  /*----------------------------------------*/
    function makeTimer($endDate, $this, $format) {
        var today = new Date();
        var BigDay = new Date($endDate),
            msPerDay = 24 * 60 * 60 * 1000,
            timeLeft = BigDay.getTime() - today.getTime(),
            e_daysLeft = timeLeft / msPerDay,
            daysLeft = Math.floor(e_daysLeft),
            e_hrsLeft = (e_daysLeft - daysLeft) * 24,
            hrsLeft = Math.floor(e_hrsLeft),
            e_minsLeft = (e_hrsLeft - hrsLeft) * 60,
            minsLeft = Math.floor((e_hrsLeft - hrsLeft) * 60),
            e_secsLeft = (e_minsLeft - minsLeft) * 60,
            secsLeft = Math.floor((e_minsLeft - minsLeft) * 60);

        var yearsLeft = 0;
        var monthsLeft = 0;
        var weeksLeft = 0;

        if ($format != "short") {
            if (daysLeft > 365) {
                yearsLeft = Math.floor(daysLeft / 365);
                daysLeft = daysLeft % 365;
            }

            if (daysLeft > 30) {
                monthsLeft = Math.floor(daysLeft / 30);
                daysLeft = daysLeft % 30;
            }
            if (daysLeft > 7) {
                weeksLeft = Math.floor(daysLeft / 7);
                daysLeft = daysLeft % 7;
            }
        }

        var yearsLeft = yearsLeft < 10 ? "0" + yearsLeft : yearsLeft,
            monthsLeft = monthsLeft < 10 ? "0" + monthsLeft : monthsLeft,
            weeksLeft = weeksLeft < 10 ? "0" + weeksLeft : weeksLeft,
            daysLeft = daysLeft < 10 ? "0" + daysLeft : daysLeft,
            hrsLeft = hrsLeft < 10 ? "0" + hrsLeft : hrsLeft,
            minsLeft = minsLeft < 10 ? "0" + minsLeft : minsLeft,
            secsLeft = secsLeft < 10 ? "0" + secsLeft : secsLeft,
            yearsText = yearsLeft > 1 ? "years" : "year",
            monthsText = monthsLeft > 1 ? "months" : "month",
            weeksText = weeksLeft > 1 ? "weeks" : "week",
            daysText = daysLeft > 1 ? "days" : "day",
            hourText = hrsLeft > 1 ? "hrs" : "hr",
            minsText = minsLeft > 1 ? "mins" : "min",
            secText = secsLeft > 1 ? "secs" : "sec";

        var $markup = {
            wrapper: $this.find(".countdown__item"),
            year: $this.find(".yearsLeft"),
            month: $this.find(".monthsLeft"),
            week: $this.find(".weeksLeft"),
            day: $this.find(".daysLeft"),
            hour: $this.find(".hoursLeft"),
            minute: $this.find(".minsLeft"),
            second: $this.find(".secsLeft"),
            yearTxt: $this.find(".yearsText"),
            monthTxt: $this.find(".monthsText"),
            weekTxt: $this.find(".weeksText"),
            dayTxt: $this.find(".daysText"),
            hourTxt: $this.find(".hoursText"),
            minTxt: $this.find(".minsText"),
            secTxt: $this.find(".secsText"),
        };

        var elNumber = $markup.wrapper.length;
        $this.addClass("item-" + elNumber);
        $($markup.year).html(yearsLeft);
        $($markup.yearTxt).html(yearsText);
        $($markup.month).html(monthsLeft);
        $($markup.monthTxt).html(monthsText);
        $($markup.week).html(weeksLeft);
        $($markup.weekTxt).html(weeksText);
        $($markup.day).html(daysLeft);
        $($markup.dayTxt).html(daysText);
        $($markup.hour).html(hrsLeft);
        $($markup.hourTxt).html(hourText);
        $($markup.minute).html(minsLeft);
        $($markup.minTxt).html(minsText);
        $($markup.second).html(secsLeft);
        $($markup.secTxt).html(secText);
    }

    $(".countdown").each(function () {
        var $this = $(this);
        var $endDate = $(this).data("countdown");
        var $format = $(this).data("format");
        setInterval(function () {
            makeTimer($endDate, $this, $format);
        }, 0);
    });

    /**
     * 喜欢滑块
     */
    $("#slider-range").slider({
        range: true,
        min: 0,
        max: 100,
        values: [0, 100],
        slide: function slide(event, ui) {
            $("#amount-like").val("" + ui.values[0] + " - " + ui.values[1]);
        },
    });
    $("#amount-like").val(
        "" +
        $("#slider-range").slider("values", 0) +
        " - " +
        $("#slider-range").slider("values", 1)
    );

    /*--------------------------
  # counter
  -------------------------- */

    $(".count").each(function () {
        var count = $(this),
            input = count.find('input[type="number"]'),
            increament = count.find(".increment"),
            decreament = count.find(".decrement"),
            minValue = input.attr("min"),
            maxValue = input.attr("max");
        increament.on("click", function () {
            var oldValue = parseFloat(input.val());

            if (oldValue >= maxValue) {
                var newVal = oldValue;
            } else {
                var newVal = oldValue + 1;
            }

            count.find("input").val(newVal);
            count.find("input").trigger("change");
        });
        decreament.on("click", function () {
            var oldValue = parseFloat(input.val());

            if (oldValue <= minValue) {
                var newVal = oldValue;
            } else {
                var newVal = oldValue - 1;
            }

            count.find("input").val(newVal);
            count.find("input").trigger("change");
        });
    });

    /*-------------------------
      Create an account toggle
      --------------------------*/

    $(".checkout-toggle2").on("click", function () {
        $(".open-toggle2").slideToggle(1000);
    });
    $(".checkout-toggle").on("click", function () {
        $(".open-toggle").slideToggle(1000);
    });

    /*----------------------------
    # Mail Chip Ajax active
    ------------------------------*/
    var mCForm = $("#mc-form");
    mCForm.ajaxChimp({
        callback: mailchimpCallback,
        //Replace this with your own mailchimp post URL. Don't remove the "". Just paste the url inside "".
        url:
            "http://devitems.us11.list-manage.com/subscribe/post?u=6bbb9b6f5827bd842d9640c82&id=05d85f18ef",
    });

    function mailchimpCallback(resp) {
        if (resp.result === "success") {
            alert(resp.msg);
        } else if (resp.result === "error") {
            alert(resp.msg);
        }
        return false;
    }

    /*------------------------------------
    # Contact Form Validation Settings
    --------------------------------------*/
    var contactForm = $("#contactForm");
    if ($("#contactForm").length) {
        contactForm.validate({
            onfocusout: false,
            onkeyup: false,
            rules: {
                name: "required",
                number: "required",
                email: {
                    required: true,
                    email: true,
                },
            },
            errorPlacement: function (error, element) {
                error.insertBefore(element);
            },
            messages: {
                name: "Enter your name?",
                email: {
                    required: "Enter your email?",
                    email: "Please, enter a valid email",
                },
            },

            highlight: function (element) {
                $(element).text("").addClass("error");
            },

            success: function (element) {
                element.text("").addClass("valid");
            },
        });
    }

    /*----------------------------
    # Contact Form Script
    -------------------------------*/

    if ($("#formSubmit").length) {
        var formSubmit = $("#formSubmit");
        CTForm.submit(function () {
            // submit the form
            if ($(this).valid()) {
                formSubmit.button("loading");
                var action = $(this).attr("action");
                $.ajax({
                    url: action,
                    type: "POST",
                    data: {
                        contactname: $("#name").val(),
                        contactemail: $("#email").val(),
                        contactmessage: $("#massage").val(),
                    },
                    success: function () {
                        formSubmit.button("reset");
                        formSubmit.button("complete");
                    },
                    error: function () {
                        formSubmit.button("reset");
                        formSubmit.button("error");
                    },
                });
                // return false to prevent normal browser submit and page navigation
            } else {
                formSubmit.button("reset");
            }
            return false;
        });
    }

    /*----------------------------
    #  Copy Right Year Update
    -------------------------------*/

    $("#currentYear").text(new Date().getFullYear());
    /*----------------------------
    #  scrollUp
    -------------------------------*/
    $.scrollUp({
        scrollName: "scrollUp",
        // Element ID
        scrollDistance: 400,
        // Distance from top/bottom before showing element (px)
        scrollFrom: "top",
        // 'top' or 'bottom'
        scrollSpeed: 400,
        // Speed back to top (ms)
        easingType: "linear",
        // Scroll to top easing (see http://easings.net/)
        animation: "fade",
        // Fade, slide, none
        animationSpeed: 200,
        // Animation speed (ms)
        scrollTrigger: false,
        // Set a custom triggering element. Can be an HTML string or jQuery object
        scrollTarget: false,
        // Set a custom target element for scrolling to. Can be element or number
        scrollText: '<i class="las la-arrow-up"></i>',
        // Text for element, can contain HTML
        scrollTitle: false,
        // Set a custom <a> title if required.
        scrollImg: false,
        // Set true to use image
        activeOverlay: false,
        // Set CSS color to display scrollUp active point, e.g '#00FFFF'
        zIndex: 214, // Z-Index for the overlay
    });

    //开启调试模式
    $(".openDebugger").off("click").on("click", function () {
        alert("已开启，请注意屏幕上的悬浮按钮，点击进入调试页面。在调试模式下，将允许js打印开发时内部日志。此功能是专门为移动端而设计。")
        eruda.init();
        __zqBlog.isDebugger = true;
        $(this).off("click");
    });

    //禁用cookie
    $(".not-allow-cookie").click(function () {
        alert("已禁用，您的登录状态，搜索关键字将不会被记录，但是你可能并不知道我是否真的自觉的禁用了:)，毕竟你们对cookie是完全无感知的。")
    });
});

function initMainWapper(){
    /**
     * 头顶
     */
    $.getJSON(__zqBlog.json.mainBarJsonPath1,function (json) {
        let topRightBarIcon=json.topRightBarIcon;
        if(topRightBarIcon!=undefined&&topRightBarIcon.length){
            let $socialLink=$(".social-links");
            $.each(topRightBarIcon,function (topRightBarIconIndex,topRightBarIconData) {
                $(`<a class="social-link ${topRightBarIconData.class}" href="${topRightBarIconData.href}" title="${topRightBarIconData.title}"><i class="${topRightBarIconData.iconClass}"></i></a>`).appendTo($socialLink);
            })
        }
    });

    /**
     * 动态加载滑动的类别
     * 仅有主页面有，因此只有在首页加载
     */
    if($(".blog-swiper-main-class").length){
        $.getJSON(__zqBlog.json.classSwiperJsonPath1,function (json) {
            if(json.length){
                let $blogSwiper=$(".blog-swiper-main-class");
                $.each(json,function (blogSwiperIndex,blogSwiperData) {
                    $(`<div class="food-category-item swiper-slide">
                                <a href="${blogSwiperData.href}" class="food-catery-thumb">
                                    <img src="${blogSwiperData.img}" alt="${blogSwiperData.alt}"/>
                                </a>
                                <h3 class="food-catery-title">
                                    <a href="${blogSwiperData.href}">${blogSwiperData.text}</a>
                                </h3>
                            </div>`).appendTo($blogSwiper);
                });
                new Swiper(".blog-category-swiper .swiper-container",
                    {
                        loop: false,
                        speed: 800,
                        slidesPerView: 6,
                        spaceBetween: 10,
                        navigation: {
                            nextEl: ".blog-category-swiper .swiper-button-next",
                            prevEl: ".blog-category-swiper .swiper-button-prev",
                        },
                        //声明下面分辨率下展示几个
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
                                slidesPerView: 6,
                            },
                        },
                    }
                );
            }
        });
    }
    //
    
    /**
     * 动态加载引导栏链接（json文件里的）,
     * ul 的class为blog-list-show-bar即可，移动端的class为blog-list-show-bar-mobile
     */
    $.getJSON(__zqBlog.json.classJsonPath2, function (json) {
        let $blogListShowBar = $(".blog-list-show-bar");
        let $blogListShowBarMobile = $(".blog-list-show-bar-mobile");
        $.each(json, function (index, data) {
            if (__zqBlog.isMobile) {
                let $mainMenuItemMobileLi = $("<li></li>");
                let $mainMenuLinkMobileA = $(`<a href="${data.menuLink || '#'}">${data.menuTitle}</a>`);
                $mainMenuItemMobileLi.append($mainMenuLinkMobileA);
                if (data.menu != undefined && data.menu.length) {
                    let $childMenuMobileUl = $("<ul></ul>");
                    $.each(data.menu, function (index_, data_) {
                        if (data_.menu != undefined && data_.menu.length) {
                            if (data_.menuType != "image") {
                                let $subSubMobileLiA = $(`<li><a href="${data_.menuLink || '#'}">${data_.menuName}</a></li>`);
                                let $subSubMobileLiUl = $("<ul></ul>");
                                $.each(data_.menu, function (index___, data___) {
                                    if (data___.menuClass == undefined) {
                                        $(`<li><a href="${data___.menuLink || '#'}">${data___.menuName}</a></li>`).appendTo($subSubMobileLiUl);
                                    }
                                })
                                $subSubMobileLiA.append($subSubMobileLiUl);
                                $subSubMobileLiA.appendTo($childMenuMobileUl);
                            }
                        } else {
                            $childMenuMobileUl.append($(`<li><a href="${data_.menuLink || '#'}">${data_.menuName}</a></li>`));
                        }
                    });
                    $mainMenuItemMobileLi.append($childMenuMobileUl);
                }
                $blogListShowBarMobile.append($mainMenuItemMobileLi);
            } else {
                let $mainMenuItemLi = $(`<li class="main-menu-item ${data.menuPosition || 'position-relative'}"></li>`);
                let $mainMenuLinkA = $(`<a class="main-menu-link" href="${data.menuLink || '#'}">${data.menuTitle}</a>`);
                $mainMenuItemLi.append($mainMenuLinkA);
                if (data.menu != undefined && data.menu.length) {
                    let $childMenuUl = $(`<ul class="${data.menuClass}"></ul>`);
                    $mainMenuLinkA.append($(`<i class="ion-ios-arrow-down"></i>`));
                    if (data.menuClass != undefined) {
                        $.each(data.menu, function (index_, data_) {
                            if (data_.menuClass == undefined) {
                                let $subMenuLinkLi = $(`<li><a class="sub-menu-link" href="${data_.menuLink || '#'}">${data_.menuName}</a></li>`);
                                if (data_.menu != undefined && data_.menu.length) {
                                    let $subSubMenuUl = $(`<ul class="sub-menu"></ul>`);
                                    $.each(data_.menu, function (index___, data___) {
                                        $(`<li><a class="sub-menu-link" href="${data___.menuLink || '#'}">${data___.menuName}</a></li>`).appendTo($subSubMenuUl);
                                    })
                                    $subMenuLinkLi.append($subSubMenuUl);
                                }
                                $childMenuUl.append($subMenuLinkLi);
                            } else {
                                let $menuListLi = $(`<li class="${data_.menuClass}"></li>`);
                                if (data_.menuType == "list") {
                                    let $menuListUl = $("<ul></ul>");
                                    $.each(data_.menu, function (index__, data__) {
                                        $(`<li class="${data__.menuClass}"><a href="${data__.menuLink || '#'}">${data__.menuName}</a></li>`)
                                            .appendTo($menuListUl);
                                    })
                                    $menuListLi.append($menuListUl);
                                } else if (data_.menuType == "image") {
                                    $menuListLi.append(`<a href="${data_.menu[0].menuLink || '#'}" class="${data_.menu[0].menuClass}"><img src="${data_.menu[0].menuImage.imageSrc}" alt="${data_.menu[0].menuImage.imageAlt}"/></a>`)
                                }
                                $childMenuUl.append($menuListLi)
                            }
                            $mainMenuItemLi.append($childMenuUl);
                        });
                    }
                }
                $blogListShowBar.append($mainMenuItemLi);
            }
        })
        if (__zqBlog.isMobile) {
            //移动端下拉
            let $offcanvasNav = $("#offcanvasNav a");
            $offcanvasNav.on("click", function () {
                let link = $(this);
                let closestUl = link.closest("ul");
                let activeLinks = closestUl.find(".active");
                let closestLi = link.closest("li");
                let linkStatus = closestLi.hasClass("active");
                let count = 0;

                closestUl.find("ul").slideUp(function () {
                    if (++count == closestUl.find("ul").length)
                        activeLinks.removeClass("active");
                });

                if (!linkStatus) {
                    closestLi.children("ul").slideDown();
                    closestLi.addClass("active");
                }
            });
        }
    })
//.vertical-menu .vmenu-content li .verticale-mega-menu li {
//   width: 25%;
// }
    /**
     * 动态加载类别（json文件里的）,
     * ul 的class为blog-class-ul即可
     */
    $.getJSON(__zqBlog.json.classJsonPath1, function (json) {
        let $me = $(".blog-class-ul");
        $.each(json, function (index, data) {
            let $blogClassLi = $(`<li class="menu-item blog-class-li"></li>`);
            let $blogClassBigA = $(`<a href="javascript:void(0)" class="blog-class-big" >${data.className}</a>`);
            $("#autoSizingSelect").append(`<option value="${data.classId}">-- ${data.className}</option>`)
            $blogClassLi.append($blogClassBigA);
            if (data.class.length) {
                let $verticalMegaMenu = $(`<ul class="verticale-mega-menu flex-wrap"></ul>`);
                let $menuItemWidth=(100/data.class.length).toFixed(1);
                $.each(data.class, function (index_, data_) {
                    let $menuItem = $(`<li class="menu-item"></li>`).append(`<a class="blog-class-small-title" href="javascript:void(0)"><span><strong>${data_.smallClassTitle}</strong></span></a>`);
                    $menuItem.css("width",$menuItemWidth+"%");
                    let $submenuItem = $(`<ul class="submenu-item"></ul>`);
                    if (data_.class.length) {
                        $.each(data_.class, function (index__, data__) {
                            $("#autoSizingSelect").append(`<option value="${data__.classId}">-- -- ${data__.className}</option>`)
                            $(`<li><a href="${data__.classLink||'#'}">${data__.className}</a></li>`).appendTo($submenuItem);
                        })
                    }
                    $menuItem.append($submenuItem);
                    $verticalMegaMenu.append($menuItem);
                });
                $blogClassBigA.append(`<i class="ion-ios-arrow-right"></i>`);
                $blogClassLi.append($verticalMegaMenu);
            }
            $me.append($blogClassLi);
        });
        //动态绑定事件
        let $btnMenu = $(".menu-btn");
        let $vmenuContent = $(".vmenu-content");
        $btnMenu.on("click", function (event) {
            $vmenuContent.slideToggle(500);
        });

        $vmenuContent.each(function () {
            let $ul = $(this),
                $lis = $ul.find(".menu-item:gt(14)"),
                isExpanded = $ul.hasClass("expanded");
            $lis[isExpanded ? "show" : "hide"]();

            if ($lis.length > 0) {
                $ul.append(
                    $(
                        '<li class="expand">' +
                        (isExpanded
                            ? '<a href="javascript:void(0);"><span><i class="ion-android-remove"></i>收起</span></a>'
                            : '<a href="javascript:void(0);"><span><i class="ion-android-add"></i>更多分类</span></a>') +
                        "</li>"
                    ).on("click", function (event) {
                        let isExpanded = $ul.hasClass("expanded");
                        event.preventDefault();
                        $(this).html(
                            isExpanded
                                ? '<a href="javascript:void(0);"><span><i class="ion-android-add"></i>更多分类</span></a>'
                                : '<a href="javascript:void(0);"><span><i class="ion-android-remove"></i>收起</span></a>'
                        );
                        $ul.toggleClass("expanded");
                        $lis.toggle(300);
                    })
                );
            }
        });
        //动态改变下拉选中类别框长度
        $("#autoSizingSelect").off("change").on("change",function () {
            $(this).width(100);
            let fontNum=$("#autoSizingSelect option:selected").text().length;
            if(fontNum>13){
                $(this).width(100+15*(fontNum-13));
            }
        });
        //搜索博客
        $(".search-blog").click(function () {
            let search=$(this).prev("input").val();
            let blogClass=$("#autoSizingSelect option:selected").val();
            let blogClassName=$("#autoSizingSelect option:selected").text();
            window.location.href=__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/bloglist.html?s="+search+"&c="+blogClass+"&cn="+blogClassName;
        })
    });

    /**
     * 今日推荐
     */
    if($(".recommend-blog-main").length){
        $.getJSON(__zqBlog.json.todayRecommendBlogJsonPath1,function (json) {
            $.each(json,function (index,data) {
                $("<div class='hero-slide-item slider-height1 swiper-slide animate-style1 slide-bg'></div>")
                    .css("background-image",`url(${data.img})`).append(`<div class="container">
                        <div class="row">
                            <div class="col-12">
                                <div class="hero-slide-content">
                                    <h2 class="title text-white delay1 animated">
                                        ${data.delay1}
                                    </h2>
                                    <br/>
                                    <h2 class="title text-white delay2 animated">
                                        ${data.delay2}
                                    </h2>
                                    <br/>
                                    <p class="text text-white animated">
                                        ${data.text}
                                    </p>
                                    <br/>
                                    <a href="${data.link}" class="btn animated btn-light">看一看</a>
                                </div>
                            </div>
                        </div>
                    </div>`).appendTo($(".recommend-blog-main"));
            });
            new Swiper(".hero-slider .swiper-container", {
                loop: true,
                speed: 600,
                autoplay: true,
                lazy: true,
                fadeEffect: {
                    crossFade: true,
                },
                pagination: {
                    el: ".hero-slider .swiper-pagination",
                    clickable: true,
                },

                navigation: {
                    nextEl: ".hero-slider .swiper-button-next",
                    prevEl: ".hero-slider .swiper-button-prev",
                },
            });
        })
    }
}

function initUser() {
    let user=__zqBlog.user;
    if(user==null){
        $(".user-mobile-bar").append(`<li class="quick-link-item d-inline-flex">
                        <span class="quick-link-icon flex-shrink-0">
                          <a href="#" class="quick-link">
                             <i class="las la-user-circle"></i
                          </a>
                        </span>
                        <span class="flex-grow-1">
                          <a href="myaccount.html" class="my-account">无用户</a>
                           <a href="login.html" class="sign-in">登录</a>
                        </span>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="#" class="quick-link" style="pointer-events: none;">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="las la-sync"></i>
                                    <span class="badge rounded-pill bg-success">0</span>
                                </span>
                        </a>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="#" class="quick-link " style="pointer-events: none;">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="lar la-heart"></i>
                                    <span class="badge rounded-pill bg-success">0</span>
                                </span>
                        </a>
                    </li>`);

        $(".user-bar").append(`<li class="quick-link-item d-none d-md-inline-flex">
                                <span class="quick-link-icon flex-shrink-0">
                                   <a href="#" class="quick-link">
                                       <i class="las la-user-circle"></i>
                                   </a>
                                </span>
                                <span class="flex-grow-1">
                                  <a href="myaccount.html" class="my-account">无用户</a>
                                  <a href="login.html" class="sign-in">登录</a>
                                </span>
                        </li>
                        <li class="quick-link-item d-none d-sm-flex">
                            <a href="compare.html" class="quick-link" style="pointer-events: none;">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="las la-sync"></i>
                                        <span class="badge rounded-pill bg-success">0</span>
                                    </span>
                            </a>
                        </li>
                        <li class="quick-link-item d-none d-sm-flex">
                            <a href="cart.html" class="quick-link" style="pointer-events: none;">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="lar la-heart"></i>
                                        <span class="badge rounded-pill bg-success">0</span>
                                    </span>
                            </a>
                        </li>
                        <li class="quick-link-item d-lg-none">
                            <button class="toggle-menu" data-bs-toggle="modal" data-bs-target="#exampleModal">
                                <i class="las la-bars"></i>
                            </button>
                        </li>`);
    }else{
        $(".user-mobile-bar").append(`<li class="quick-link-item d-inline-flex">
                        <span class="quick-link-icon flex-shrink-0">
                          <a href="#" class="quick-link">
                              <img class="rounded-circle" src="${user.image}" width="34" height="34">
                          </a>
                        </span>
                        <span class="flex-grow-1">
                           <a href="myaccount.html" class="my-account">${user.username}</a>
                           <a class="sign-out">登出</a>
                        </span>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="#" class="quick-link">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="las la-sync"></i>
                                    <span class="badge rounded-pill bg-success">2</span>
                                </span>
                        </a>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="#" class="quick-link">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="lar la-heart"></i>
                                    <span class="badge rounded-pill bg-success">3</span>
                                </span>
                        </a>
                    </li>
                    `);
        $(".user-bar").append(`<li class="quick-link-item d-none d-md-inline-flex">
                                <span class="quick-link-icon flex-shrink-0">
                                   <a href="#" class="quick-link">
                                    <img class="rounded-circle" src="${user.image}" width="34" height="34">
                                  </a>
                                </span>
                                <span class="flex-grow-1">
                                  <a href="myaccount.html" class="my-account">${user.username}</a>
                                  <a class="sign-out">登出</a>
                                </span>
                        </li>
                        <li class="quick-link-item d-none d-sm-flex">
                            <a href="compare.html" class="quick-link">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="las la-sync"></i>
                                        <span class="badge rounded-pill bg-success">2</span>
                                    </span>
                            </a>
                        </li>
                        <li class="quick-link-item d-none d-sm-flex">
                            <a href="cart.html" class="quick-link">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="lar la-heart"></i>
                                        <span class="badge rounded-pill bg-success">3</span>
                                    </span>
                            </a>
                        </li><li class="quick-link-item">
                            <a href="#" class="quick-link">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="las la-shopping-bag"></i>
                                        <span class="badge rounded-pill bg-success">4</span>
                                    </span>
                            </a>
                            <div class="checkout-cart">
                                <ul class="checkout-scroll">
                                    <li class="checkout-cart-list">
                                        <div class="checkout-img">
                                            <img class="product-image" src="../static/images/mini-cart/1.jpg" alt="img"/>
                                        </div>
                                        <div class="checkout-block">
                                            <a class="product-name" href="#">Warburtons 9 Crumpets</a>
                                            <span class="product-price">€24.33</span>
                                            <a class="remove-cart" href="#">
                                                <i class="las la-times"></i>
                                            </a>
                                            <div class="product-size">
                                                <span>Size: S</span>
                                            </div>
                                        </div>
                                    </li>
                                    <li class="checkout-cart-list">
                                        <div class="checkout-img">
                                            <img class="product-image" src="../static/images/mini-cart/2.jpg" alt="img"/>
                                        </div>
                                        <div class="checkout-block">
                                            <a href="#" class="product-name">Snacking Essentials Cashew</a>
                                            <span class="product-price">€23.33</span>
                                            <a class="remove-cart" href="#">
                                                <i class="las la-times"></i>
                                            </a>
                                            <div class="product-size">
                                                <span>Size: S</span>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                                <div class="checkout-action">
                                    <a href="checkout.html" class="btn btn-lg btn-dark d-block">全部移除</a>
                                </div>
                            </div>
                        </li>
                        <li class="quick-link-item d-lg-none">
                            <button class="toggle-menu" data-bs-toggle="modal" data-bs-target="#exampleModal">
                                <i class="las la-bars"></i>
                            </button>
                        </li>`);

        $(".sign-out,.logout").on("click",function () {
            $.get(__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/logout",function (data) {
                alert("登出成功");
                window.location.reload();
            })
        })
    }
}
