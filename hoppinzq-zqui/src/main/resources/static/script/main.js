//公用对象——一些不高兴的狗 by HOPPIN&HAZZ ~ZQ
var zq = {
    user:null,//当前登录人，取该值为null表示没有获取到或者没登陆
    ipConfig: {
        ip: "150.158.28.40", //127.0.0.1
        ip_: "http://150.158.28.40", //http://127.0.0.1
        port:"8811",
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
        classJsonPath1: "/static/json/classJSON.json",
        classJsonPath2: "/static/json/barLinkJSON.json",
        mainBarJsonPath1:"/static/json/mainJSON1.json",
        classSwiperJsonPath1:"/static/json/swiperJSON1.json",
        todayRecommendBlogJsonPath1:"/static/json/todayRecommendJSON1.json",
        footerJsonPath1:"/static/json/footerJSON.json",
        adJsonPath:"/static/json/adJSON.json",
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
     * @Author zq
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
            url:zq.ipConfig.ip_+":"+zq.ipConfig.port+"/getUser",
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
        let strDateArrayEnd = zq.getRealDate(new Date()).split(strSeparator);
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

    _zqLog("\n %c hoppinzq-zui开源 %c https://gitee.com/hoppin/hoppinzq/tree/master/hoppinzq-zqui \n\n", "background: #35495e; padding: 1px; border-radius: 3px 0 0 3px; color: #fff", "background: #fadfa3; padding: 1px; border-radius: 0 3px 3px 0; color: #fff");

    if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        //是否是移动端
        zq.isMobile = true;
    } else {
        zq.isMobile = false;
    }
    //是否支持cookie
    if (navigator.cookieEnabled) {
        zq.isCookie = true;
    } else {
        zq.isCookie = false;
    }
    //是否联网/脱机
    if (navigator.onLine) {
        zq.isOnLine = true;
    } else {
        zq.isOnLine = false;
    }
    //是否支持pdf在线预览
    if (navigator.pdfViewerEnabled) {
        zq.isPdfView = true;
    } else {
        zq.isPdfView = false;
    }

    //是否支持WebSocket
    if ('WebSocket' in window) {
        zq.isWebSocket = true;
    } else {
        zq.isWebSocket = false;
    }
    //是否支持storage存储或者是否开启了隐私模式之类的
    if (typeof (Storage) !== "undefined") {
        zq.isStorage = true;
    } else {
        zq.isStorage = false;
    }
    //是否支持indexedDB存储
    if (!window.indexedDB) {
        zq.isIndexedDB = true;
    } else {
        zq.isIndexedDB = true;
    }

    //判断当前使用的是否是4G流量
    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection || {
        tyep: 'unknown'
    };
    if (connection.effectiveType === '3G' || connection.effectiveType === '4G') {
        zq.isWifi = false;
    } else {
        zq.isWifi = true;
    }
    if(window.screen.width<1440) $("#wrapper-silder-bar").remove();
    if(localStorage.getItem("zq:isCloseTopBar")=="1") $("#wrapper-silder-bar").remove();

    //获取当前用户(先从缓存)，尝试10s，获取不到先继续执行后面的js代码，之后会异步尝试获取当前用户，直到接收到响应。
    $.ajax({
        url:zq.ipConfig.ip_+":"+zq.ipConfig.port+"/getUser",
        timeout:30000,
        xhrFields: {
            withCredentials: true
        },
        beforeSend:function (xhr){
            let token=zq.getCookie("ZQ_TOKEN");
            let user=localStorage.getItem("zq:blog:user");
            if(token!=null&&user!=null){
                zq.user=JSON.parse(user);
                initUser();
                xhr.abort();//return false都可以阻塞ajax请求
                return false;
            }
        },
        success:function (data) {
            if(data.code!=500){
                zq.user=data;
                localStorage.setItem("zq:blog:user",JSON.stringify(data));
            }else{
                zq.user=null;
                localStorage.removeItem("zq:blog:user");
            }
            initUser();
        },
        error:function (a,b) {
            //
            localStorage.removeItem("zq:blog:user");
        }
    });
    $.ajax({
        url:"/static/json/zuiMenu.json",
        // beforeSend:function (xhr) {
        //     let data=localStorage.getItem("zui:menu");
        //     if(data!=null){
        //         loadMenu(JSON.parse(data));
        //         xhr.abort();
        //     }
        // },
        success:function (data){
            loadMenu(data);
        }
    });
    zq.getResource("/static/json/zuiMenuFooterSidebar.json",function (data) {
        if(data&&data.length){
            $(".sidebar-footer").html("");
            $.each(data,function (index,json) {
                $(".sidebar-footer").append(`<a href="${json.href||'javaScript:void(0)'}" class="${json.class}" 
            data-toggle="${json.dataToggle}" title="" data-original-title="${json.dataOriginalTitle}"
               ${json.dataOriginalTitle?'aria-describedby='+json.dataOriginalTitle:''}"><span class="${json.icon}">
               ${json.type=='path2'?'<span class="path1"></span><span class="path2"></span>':''}</span></a>`);
            })
        }
    })
    zq.getResource("/static/json/footer.json",function (data) {
        if(data){
            $(".main-footer").html("");
            let $footer=$(".main-footer");
            let $divLinks=$(`<div class="pull-right d-none d-sm-inline-block"></div>`);
            let $ulLinks=$(`<ul class="nav nav-primary nav-dotted nav-dot-separated justify-content-center justify-content-md-end"></ul>`);
            $.each(data.links,function (index,json) {
                $ulLinks.append(`<li class="nav-item">
                    <a class="nav-link" href="${json.href||'javascript:void(0)'}">${json.title}</a>
                </li>`);
            });
            $divLinks.append($ulLinks);
            $footer.append($divLinks);
            $footer.append(data.copyRight);
        }
    })

});

function loadMenu(data){
    if(data&&data.length){
        let url=window.location.href;
        let local=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("?")==-1?url.length:url.lastIndexOf("?"));
        if(local.length==0) local="main.html";
        $(".sidebar-menu").html("");
        //window.localStorage.setItem("zui:menu",JSON.stringify(data));
        $.each(data,function (index,json) {
            if(!json.isSubMenu){
                $(".sidebar-menu").append(`<li class="${json.class}">${json.title}</li>`);
            }else{
                let $li1=$(`<li class="${json.class}"></li>`);
                let localPage=false;
                let $a1=$(`<a href="javaScript:void(0)">
                                <i class="${json.iconClass}">
                                    <span class="path1" style="left: 10px;position: relative;"></span>
                                    <span class="path2"></span>
                                </i>
                                <span>${json.title}</span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-right pull-right"></i>
                                 </span>
                            </a>`);
                $li1.append($a1);
                let $ulTreeView1=$(`<ul class="treeview-menu"></ul>`);
                $.each(json.tree,function (index_,json_){
                    if(local==json_.href) localPage=true;
                    if(!json_.isSubMenu){
                        let href_='javaScript:void(0)';
                        if(json_.href){
                            if(json_.href.indexOf("http")==0){
                                href_=json_.href;
                            }else{
                                href_=zq.ipConfig.ip_+':'+zq.ipConfig.port+'/'+json_.href;
                            }
                        }
                        $ulTreeView1.append(`<li class="${json_.class} ${local==json_.href?'active':''}">
                            <a href="${href_}"><i class="${json_.iconClass}"><span class="path1"></span>
                            <span class="path2"></span></i>${json_.title}</a></li>`);
                    }else{
                        let $li11=$(`<li class="${json_.class}"></li>`);
                        let $a11=$(`<a href="javaScript:void(0)">
                                <i class="${json_.iconClass}">
                                    <span class="path1"></span>
                                    <span class="path2"></span>
                                </i>
                                <span style="top: -1px;left: -7px">${json_.title}</span>
                                <span class="pull-right-container" style="margin-top: -9px;">
                                    <i class="fa fa-angle-right pull-right"></i>
                                 </span>
                            </a>`);
                        $li11.append($a11);
                        let $ulTreeView11=$(`<ul class="treeview-menu"></ul>`);

                        $.each(json_.tree,function (index__,json__){
                            if(local==json__.href){
                                localPage=true;
                                $li11.addClass("active menu-open");
                            }
                            let href__='javaScript:void(0)';
                            if(json__.href){
                                if(json__.href.indexOf("http")==0){
                                    href__=json__.href;
                                }else{
                                    href__=zq.ipConfig.ip_+':'+zq.ipConfig.port+'/'+json__.href;
                                }
                            }
                            $ulTreeView11.append(`<li class="${json__.class} ${local==json__.href?'active':''}"><a href="${href__}"><i
                                                class="${json__.iconClass}"><span
                                                class="path1"></span>
                                                <span class="path2"></span></i>${json__.title}</a></li>`);
                        });
                        $li11.append($ulTreeView11);
                        $ulTreeView1.append($li11);
                    }
                });
                $li1.append($ulTreeView1);
                if(localPage){
                    $li1.addClass("active menu-open")
                }
                $(".sidebar-menu").append($li1);
            }
        })
    }else{
        console.error("加载菜单错误")
    }
}

function initUser(){
    let $userBody=$(".user-body");
    $userBody.html("");
    let $userHead=$(".user-menu a");
    $userHead.html("");
    if(zq.user!=null){
        $userHead.append(`<img class="user-header-image" src="${zq.user.image}">
                            <img class="user-header-image-avila" src="http://hoppinzq.com/zui/static/picture/avila.png" title="avilabel">`);
        $userBody.append2(`<a class="dropdown-item" href="#"><i class="ti-user text-muted mr-2"></i>欢迎您，${zq.user.username}</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#"><i class="ti-user text-muted mr-2"></i> 账户详情</a>
                                <a class="dropdown-item" href="#"><i class="ti-wallet text-muted mr-2"></i> 我的钱包</a>
                                <a class="dropdown-item" href="#"><i class="ti-settings text-muted mr-2"></i> 设置</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item user-logout" href="#"><i class="ti-lock text-muted mr-2"></i> 登出</a>`,
function () {
            $(".user-logout").off("click").on("click",function () {
                $.get(zq.ipConfig.ip_+":"+zq.ipConfig.port+"/logout",function (data) {
                    localStorage.removeItem("zq:blog:user");
                    window.location.reload();
                })
            })
        })
    }else{
        let url1 = window.location.href;
        $userHead.append(`<i class="icon-User"><span class="path1"></span><span class="path2"></span></i>`);
        $userBody.append2(`<a class="dropdown-item" href="http://150.158.28.40:8804/login.html?redirect=${url1}"><i class="ti-lock text-muted mr-2"></i> 请先登录</a>`,function () {

        })
    }
}

function closeSilderBar(){
    if(!confirm("是否永久关闭滚动？")){
        $("#wrapper-silder-bar").remove();
    }else{
        alert("已永久关闭，你可以清理缓存开启。")
        $("#wrapper-silder-bar").remove();
        window.localStorage.setItem("zq:isCloseTopBar","1");
    }
}