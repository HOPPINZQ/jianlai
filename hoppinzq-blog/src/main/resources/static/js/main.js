//公用对象——一些不高兴的狗 by HOPPIN&HAZZ ~ZQ
var __zqBlog = {
    user:null,//当前登录人，取该值为null表示没有获取到或者没登陆
    ipConfig: {
        ip: "1.15.232.156", //127.0.0.1
        ip_: "http://1.15.232.156", //http://127.0.0.1
        blogPort:"80",
        authServer:"http://150.158.28.40:8804/login.html",
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
    isWifi: true,//使用的是否是流量还是wifi
    //通过json文件配置的页面，json文件的路径
    json: {
        classJsonPath1: "http://1.15.232.156:9001/classJSON.json",
        classJsonPath2: "http://1.15.232.156:9001/barLinkJSON.json",
        mainBarJsonPath1:"http://1.15.232.156:9001/mainJSON1.json",
        classSwiperJsonPath1:"http://1.15.232.156:9001/swiperJSON1.json",
        todayRecommendBlogJsonPath1:"http://1.15.232.156:9001/todayRecommendJSON1.json",
        footerJsonPath1:"http://1.15.232.156:9001/footerJSON.json",
        adJsonPath:"http://1.15.232.156:9001/adJSON.json",
    },

    //全局方法
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
     * 原生ajax简单封装，get请求==$.get(url,callback)
     * @param url
     * @param callback
     */
    getResource: function(url, callback) {
        let time=1;
        let xhr = new XMLHttpRequest();
        xhr.open("GET", url);
        xhr.send();
        xhr.onload = xhr.onreadystatechange = function() {
            if (this.status == 200) {
                if(xhr.readyState==4&&time){
                    time--;
                    callback(eval("(" + this.response + ")"));
                }
            } else {
                throw new Error("加载资源失败");
            }
        }
    },
    /**
     * 加载图片,并创建图片对象到dom内，请求不到的图片资源使用404图片替换之
     * @param url
     * @param dom
     * @param errorUrl
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
     * 会动态创建img标签，创建img标签不会跨域
     * 第一次获取图片会签名并缓存，之后直接获取缓存图片的blob对象转URL对象
     * 获取图片会跨域，需要服务端支持跨域
     * @param url img的路径
     * @param className img的class
     * @param alt img的alt 用于seo
     * @param errorUrl 当img没有获取到时，使用一张其他url或者404的图片替换
     * @param style 自定义样式
     * @returns {string} 会马上返回装配完成的img的html，稍后会自动装配
     */
    loadImage: function (url,className,alt,errorUrl,style) {
        let me = this;
        let id=me.uuid(32,62);
        let encodeUrl=window.btoa(url);
        localforage.getItem(encodeUrl, function(err, value_) {
            if (value_ != null) {
                url = window.URL.createObjectURL(value_);
            }else{
                let xhr = new XMLHttpRequest();
                let blob;
                xhr.open('GET', url, true);
                xhr.responseType = 'blob';
                xhr.onload = function() {
                    let data = xhr.response;
                    blob = new Blob([data]);
                    localforage.setItem(encodeUrl, blob);
                };
                xhr.send();
            }
        });
        let image = new Image();
        image.src = url;
        // if(url==undefined||url){
        //     image.src = errorUrl;
        // }
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
     * html模板装配，匹配[]
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
     * 控制方法执行顺序,使之同步执行
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
    /**
     * 开启遮罩 $dom为空开启全局遮罩
     * @param $dom 开启指定遮罩
     */
    startLoading:function ($dom) {
        if($dom&&$dom.length){
            $dom.fadeIn();
        }else{
            $(".preloader").fadeIn();
        }
    },
    /**
     * 关闭遮罩
     * @param d 延迟/ms关闭
     * @param f 渐隐时间/ms
     */
    stopLoading:function (d=2000,f=1000) {
        $(".preloader").delay(d).fadeOut(f);
    },
    /**
     * 获取文件大小
     * @param size
     * @returns {string}
     */
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
    },
    /**
     * 时间差
     * @param str
     * @returns {string}
     */
    getDateCha:function (str) {
        let strSeparator = "-"; //日期分隔符
        let strDateArrayStart =str.split(strSeparator);
        let strDateArrayEnd = __zqBlog.getRealDate(new Date()).split(strSeparator);
        let strDateS = new Date(strDateArrayStart[0] + "/" + strDateArrayStart[1] + "/" + strDateArrayStart[2]);
        let strDateE = new Date(strDateArrayEnd[0] + "/" + strDateArrayEnd[1] + "/" + strDateArrayEnd[2]);
        let intDay = (strDateE-strDateS)/(1000*3600*24);
        return intDay.toFixed(2);
    },
    /**
     * jsonp封装 = $.getJson(url,callback)
     * @param url
     * @param params
     * @param callback
     * @returns {Promise<unknown>}
     */
    jsonp: function({ url, params, callback }) {
        return new Promise((resolve, reject) => {
            // 创建一个临时的 script 标签用于发起请求
            const script = document.createElement('script');
            // 将回调函数临时绑定到 window 对象，回调函数执行完成后，移除 script 标签
            window[callback] = data => {
                resolve(data);
                document.body.removeChild(script);
            };
            // 构造 GET 请求参数，key=value&callback=callback
            const formatParams = { ...params, callback };
            const requestParams = Object.keys(formatParams)
                .reduce((acc, cur) => {
                    return acc.concat([`${cur}=${formatParams[cur]}`]);
                }, [])
                .join('&');
            // 构造 GET 请求的 url 地址
            const src = `${url}?${requestParams}`;
            script.setAttribute('src', src);
            document.body.appendChild(script);
        });
    },
    /**
     * 获取cookie，返回”“表示没有
     * @param cname
     * @returns {string}
     */
    getCookie:function(cname) {
        let name = cname + "=";
        let ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i].trim();
            if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
        }
        return "";
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
    //拓展Array原型链,用以判断一个元素是否在集合内
    Array.prototype.contains = function (arr){
        //this指向真正调用这个方法的对象
        for(let i=0;i<this.length;i++){
            if(this[i] == arr){
                return true;
            }
        }
        return false;
    }
    //删除数组元素
    Array.prototype.remove=function(dx)
    {
        if(isNaN(dx)||dx>this.length){return false;}
        for(let i=0,n=0;i<this.length;i++)
        {
            if(this[i]!=this[dx])
            {
                this[n++]=this[i]
            }
        }
        this.length-=1
    }

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

    //获取当前用户(先从缓存)，尝试10s，获取不到先继续执行后面的js代码，之后会异步尝试获取当前用户，直到接收到响应。
    $.ajax({
        url:__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/getUser",
        timeout:30000,
        xhrFields: {
            withCredentials: true
        },
        beforeSend:function (xhr){
            let token=__zqBlog.getCookie("ZQ_TOKEN");
            let user=localStorage.getItem("zq:blog:user");
            if(token!=null&&user!=null){
                __zqBlog.user=JSON.parse(user);
                initUser();
                xhr.abort();//return false都可以阻塞ajax请求
                return false;
            }
        },
        success:function (data) {
            if(data.code!=500){
                __zqBlog.user=data;
                localStorage.setItem("zq:blog:user",JSON.stringify(data));
            }else{
                __zqBlog.user=null;
                localStorage.removeItem("zq:blog:user");
            }
            initUser();
        },
        error:function (a,b) {
            //
            localStorage.removeItem("zq:blog:user");
        }
    });

    //logo跳首页
    $(".logo,.main-page").attr("href",__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort);
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

    var tooltipTriggerList = [].slice.call(
        document.querySelectorAll('[data-bs-toggle="tooltip"]')
    );
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
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

    $(".checkout-toggle2").on("click", function () {
        $(".open-toggle2").slideToggle(1000);
    });
    $(".checkout-toggle").on("click", function () {
        $(".open-toggle").slideToggle(1000);
    });

    $("#currentYear").text(new Date().getFullYear());

    $.scrollUp({
        scrollName: "scrollUp",
        scrollDistance: 400,
        scrollFrom: "top",
        scrollSpeed: 400,
        easingType: "linear",
        animation: "fade",
        animationSpeed: 200,
        scrollTrigger: false,
        scrollTarget: false,
        scrollText: '<i class="las la-arrow-up"></i>',
        scrollTitle: false,
        scrollImg: false,
        activeOverlay: false,
        zIndex: 214, 
    });
    
    //缓存
    $(".clear-cache").off("click").on("click",function () {
        localStorage.clear();
        sessionStorage.clear();
        alert("已清空！刷新页面生效")
    })

    //开启调试模式
    $(".openDebugger").off("click").on("click", function () {
        alert("已开启，请注意屏幕上的悬浮按钮，点击进入调试页面。在调试模式下，将允许js打印开发时内部日志。此功能是专门为移动端而设计。")
        eruda.init();
        __zqBlog.isDebugger = true;
        $(this).off("click");
    });

    //禁用cookie（仅在当前页禁用，禁用是不可能禁用的，超喜欢用的）
    $(".not-allow-cookie").click(function () {
        __zqBlog.isCookie=false;
        alert("已禁用，但是你可能并不知道我是否真的自觉的禁用了:)，毕竟你们对cookie是完全无感知的。")
    });
});

/**
 * 读取JSON包装页面
 */
function initMainWapper(){
    /**
     * 头顶
     */
    __zqBlog.getResource(__zqBlog.json.mainBarJsonPath1,function (json) {
        let topRightBarIcon=json.topRightBarIcon;
        if(topRightBarIcon!=undefined&&topRightBarIcon.length){
            let $socialLink=$(".social-links");
            let $socialMobile=$(".offcanvas-social");
            let $socialMobileUl=$socialMobile.children("ul");
            if(!$socialLink.hasClass("no-links")){//有些不用读取图标
                $.each(topRightBarIcon,function (topRightBarIconIndex,topRightBarIconData) {
                    $(`<a class="social-link ${topRightBarIconData.class}" href="${topRightBarIconData.href}" title="${topRightBarIconData.title}"><i class="${topRightBarIconData.iconClass}"></i></a>`).appendTo($socialLink);
                    $(`<li><a href="${topRightBarIconData.href}"><i class="${topRightBarIconData.iconClass}" style="font-size: 20px;"></i></a></li>`).appendTo($socialMobileUl);
                })
            }

        }
    });

    /**
     * 动态加载滑动的类别
     * 仅有主页面有，因此只有在首页加载
     */
    if($(".blog-swiper-main-class").length){
        __zqBlog.getResource(__zqBlog.json.classSwiperJsonPath1,function (json) {
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
    __zqBlog.getResource(__zqBlog.json.classJsonPath2, function (json) {
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
    __zqBlog.getResource(__zqBlog.json.classJsonPath1, function (json) {
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
                            return;
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
            let blogClass=$("#autoSizingSelect option:selected").val();
            let blogClassName=$("#autoSizingSelect option:selected").text();
            if(__zqBlog.isMobile){
                window.location.href=__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/bloglist.html?s="+search;
            }else{
                window.location.href=__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/bloglist.html?s="+search+"&c="+blogClass+"&cn="+blogClassName;
            }
        })
    });

    /**
     * 脚部链接
     */
    __zqBlog.getResource(__zqBlog.json.footerJsonPath1,function (json) {
        $.each(json,function (index,footerData) {
            let $footer_ul=$(`<ul class="footer-des"></ul>`);
            $.each(footerData.footers,function (index_,footer_) {
                $(`<li class="footer-menu-items">${footer_.icon?"<i class='"+footer_.icon+"'></i>":""}<a class="footer-menu-link" ${footer_.target?"target='_blank'":""} href="${footer_.link||'#'}">${footer_.text}</a></li>`)
                    .appendTo($footer_ul);
            })
            let titleFooter=`footer-title-${index}`;
            $(`<div class="col-lg-3 col-sm-6 mb-7">
                    <div class="footer-widget">
                        <h4 class="title ${titleFooter}">${footerData.title}</h4>
                    </div>
                </div>`).appendTo($(".footer-list"));
            $("."+titleFooter).after($footer_ul);
        })
    });


    //广告
    if($(".ad-main,.ad-left,.ad-right").length){
        __zqBlog.getResource(__zqBlog.json.adJsonPath,function (json) {
            let main=json.main;
            $.each(main,function (index,admain) {
                $(`<div class="${admain.adRes}">
                <a href="${admain.adSrc}" class="${admain.adClass}">
                    <img src="${admain.adImage.src}" class="${admain.adImage.class}" alt="${admain.adImage.alt}"></a>
            </div>`).appendTo($(".ad-main"));
            });
            let left=json.left;
            $.each(left,function (index,adleft) {
                $(` <a href="${adleft.adSrc}" class="${adleft.adClass}">
                    <img src="${adleft.adImage.src}" class="${adleft.adImage.class}" alt="${adleft.adImage.alt}"></a>`).appendTo($(".ad-left"));
            });
            let right=json.right;
            $.each(right,function (index,adright) {
                $(` <a href="${adright.adSrc}" class="${adright.adClass}">
                    <img src="${adright.adImage.src}" class="${adright.adImage.class}" alt="${adright.adImage.alt}"></a>`).appendTo($(".ad-right"));
            });
        })
    }

}

/**
 * 初始化用户信息
 * 这个方法太长了，就单独搞出来了
 */
function initUser() {
    let user=__zqBlog.user;
    if(user==null){
        if(__zqBlog.isMobile){
            $(".user-mobile-bar").append(`<li class="quick-link-item d-inline-flex">
                        <span class="quick-link-icon flex-shrink-0">
                          <a href="javaScript:void(0)" class="quick-link">
                             <i class="las la-user-circle"></i
                          </a>
                        </span>
                        <span class="flex-grow-1">
                           <a class="my-account">无用户</a>
                           <a href="${__zqBlog.ipConfig.authServer}?redirect=${window.location.href}" class="sign-in">登录</a>
                        </span>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/writeblog.html"}" class="quick-link" style="pointer-events: none;">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="las la-pen"></i>
                                    <span class="badge rounded-pill bg-success">0</span>
                                </span>
                        </a>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="javaScript:void(0)" class="quick-link " style="pointer-events: none;">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="lar la-heart"></i>
                                    <span class="badge rounded-pill bg-success">0</span>
                                </span>
                        </a>
                    </li>`);
        }
        $(".user-bar").append(`<li class="quick-link-item d-none d-md-inline-flex">
                                <span class="quick-link-icon flex-shrink-0">
                                   <a href="javaScript:void(0)" class="quick-link">
                                       <i class="las la-user-circle"></i>
                                   </a>
                                </span>
                                <span class="flex-grow-1">
                                  <a class="my-account">无用户</a>
                                  <a href="${__zqBlog.ipConfig.authServer}?redirect=${window.location.href}" class="sign-in">登录</a>
                                </span>
                        </li>
                        <li class="quick-link-item d-none d-sm-flex">
                            <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/writeblog.html"}" class="quick-link" style="pointer-events: none;">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="las la-pen"></i>
                                        <span class="badge rounded-pill bg-success blog-cg">0</span>
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
        if(__zqBlog.isMobile){
            $(".user-mobile-bar").append2(`<li class="quick-link-item d-inline-flex">
                        <span class="quick-link-icon flex-shrink-0">
                          <a href="javaScript:void(0)" class="quick-link">
                              <img class="rounded-circle" src="${user.image}" width="34" height="34">
                          </a>
                        </span>
                        <span class="flex-grow-1">
                           <a href="javaScript:void(0)" class="my-account">${user.username}</a>
                           <a class="sign-out">登出</a>
                        </span>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/writeblog.html"}" class="quick-link">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="las la-pen"></i>
                                    <span class="badge rounded-pill bg-success blog-cg">0</span>
                                </span>
                        </a>
                    </li>
                    <li class="quick-link-item d-inline-flex">
                        <a href="javaScript:void(0)" class="quick-link">
                                <span class="quick-link-icon flex-shrink-0">
                                    <i class="lar la-heart"></i>
                                    <span class="badge rounded-pill bg-success">3</span>
                                </span>
                        </a>
                    </li>`);
        }

        $(".user-bar").append2(`<li class="quick-link-item d-none d-md-inline-flex">
                                <span class="quick-link-icon flex-shrink-0">
                                   <a href="javaScript:void(0)" class="quick-link">
                                    <img class="rounded-circle" src="${user.image}" width="34" height="34">
                                  </a>
                                </span>
                                <span class="flex-grow-1">
                                  <a href="javaScript:void(0)" class="my-account">${user.username}</a>
                                  <a class="sign-out">登出</a>
                                </span>
                        </li>
                        <li class="quick-link-item d-none d-sm-flex">
                            <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/writeblog.html"}" class="quick-link">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="las la-pen"></i>
                                        <span class="badge rounded-pill bg-success blog-cg">0</span>
                                    </span>
                            </a>
                        </li>
                        <li class="quick-link-item d-none d-sm-flex">
                            <a href="javaScript:void(0)" class="quick-link">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="lar la-heart"></i>
                                        <span class="badge rounded-pill bg-success">3</span>
                                    </span>
                            </a>
                        </li><li class="quick-link-item">
                            <a href="javaScript:void(0)" class="quick-link">
                                    <span class="quick-link-icon flex-shrink-0">
                                        <i class="las la-eye"></i>
                                        <span class="badge rounded-pill bg-success blog-later-see">0</span>
                                    </span>
                            </a>
                            <div class="checkout-cart">
                                <ul class="checkout-scroll later-blog-list"></ul>
                                <div class="checkout-action">
                                    <a href="javaScript:void(0)" class="btn btn-lg btn-dark d-block all-local-later-blog">全部移除</a>
                                </div>
                            </div>
                        </li>
                        <li class="quick-link-item d-lg-none">
                            <button class="toggle-menu" data-bs-toggle="modal" data-bs-target="#exampleModal">
                                <i class="las la-bars"></i>
                            </button>
                        </li>`,function () {
                        let blog_laters=window.localStorage.getItem("later:blog");
                        if(blog_laters==null){
                            $(".blog-later-see").text(0)
                        }else{
                            let blog_later_ids=blog_laters.split(",");
                            $(".blog-later-see").text(blog_later_ids.length);
                            $.each(blog_later_ids,function (index,blog_id){
                                let blog_later=localStorage.getItem("blog:id:"+blog_id);
                                if(blog_later!=null){
                                    blog_later=JSON.parse(blog_later)
                                    $(".later-blog-list").append2(`<li class="checkout-cart-list" id="blog-later-${blog_later.id}">
                                        <div class="checkout-img">
                                            ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog_later.image,"product-image","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                                        "width":"98px",
                                        "height":"98px",
                                        "object-fit":"cover"
                                    })} 
                                        </div>
                                        <div class="checkout-block">
                                            <a href="${__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort}/blog/${blog_later.id}" class="product-name">${blog_later.title}</a>
                                            <a class="remove-cart" data-id="${blog_later.id}" href="javaScript:void(0)">
                                                <i class="las la-times"></i>
                                            </a>
                                            <span class="product-price">创建时间：${blog_later.date}</span>
                                        </div>
                                    </li>`,function () {
                                        $(".remove-cart").off("click").on("click",function () {
                                            let blog_id=$(this).data("id");
                                            let blog_loId="blog:id:"+blog_id;
                                            localStorage.removeItem(blog_loId);
                                            $("#blog-later-"+blog_loId).remove();
                                            let blog_laters=localStorage.getItem("later:blog");
                                            blog_later_ids=blog_laters.split(",")
                                            blog_later_ids = blog_later_ids.filter(function(item) {
                                                return item != blog_id
                                            });
                                            localStorage.setItem("later:blog",blog_later_ids);
                                        });
                                        $(".all-local-later-blog").on("click",function () {
                                            let blog_laters=localStorage.getItem("later:blog");
                                            blog_later_ids=blog_laters.split(",");
                                            $.each(blog_later_ids,function (index,blog_id) {
                                                localStorage.removeItem("blog:id:"+blog_id);
                                            })
                                            localStorage.removeItem("later:blog");
                                        })
                                    })
                                }
                            })
                        }

            $.ajax({
                url:__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/hoppinzq?method=queryUserBlogExtra&params={\"auth_id\":"+user.id+"}",
                success:function (data) {
                    let json=JSON.parse(data);
                    if(json.code==200){
                        let blogExtra=json.data;
                        $(".blog-cg").text(blogExtra.cgNum);
                    }
                }
            })
        });
        new jBox('Tooltip', {
            attach: '.blog-cg',
            width: 280,
            closeOnMouseleave: true,
            animation: 'zoomIn',
            content: '写博客'
        });
        $(".sign-out,.logout").on("click",function () {
            $.get(__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/logout",function (data) {
                alert("登出成功");
                window.localStorage.removeItem("zq:blog:user");
                window.location.reload();
            })
        });
    }

    //搜索框事件
    $("#main_search").click(function(){
        $("#hotwords").show();
    });
    $("#hotwords").hover('',function(){
        $("#hotwords").hide();
    });
    $("#main_search").keydown(function(){
        $("#hotwords").hide();
    });

    //加载搜索记录
    let searchKeys=window.localStorage.getItem("searchKey");
    if(searchKeys!=null){
        let searchKeyJson=JSON.parse(searchKeys);
        if(searchKeyJson.length>0){
            $.each(searchKeyJson,function (searchKey_index,searchKey) {
                $("#history-search").append($(`<li><a href='javascript:void(0)'><h1>${searchKey.value}</h1></a></li>`).on("click",function () {
                    let text = $(this).find('h1').text();
                    $("#main_search").val(text);
                    $("#hotwords").hide();
                }))
            })
        }
    }
    //清除搜索记录
    $(".clear-history").click(function () {
        window.localStorage.removeItem("searchKey");
        $("#history-search").html("");
    });
    //加载热搜
    $.ajax({
        url:__zqBlog.ipConfig.ip_+":"+__zqBlog.ipConfig.blogPort+"/hoppinzq?method=hotSearchKey&params=%7B%7D",
        success:function (data) {
            let json=JSON.parse(data);
            if(json.code==200){
                let hots=json.data;
                for(let i=hots.length-1;i>=0;i--){
                    let hot=hots[i];
                    $(".hottop10").after($(`<li><a href='javascript:void(0)'><h1>${hot.search}</h1><span>${hot.num}次搜索</span></a></li>`).on("click",function () {
                        let text = $(this).find('h1').text();
                        $("#main_search").val(text);
                        $("#hotwords").hide();
                    }))
                }
            }else{
                $(".hottop10").text("热搜加载失败！");
            }
        },
        error:function () {
            $(".hottop10").text("热搜加载失败！");
        },
        complete:function () {

        }
    });
}
//加载用户信息完毕
