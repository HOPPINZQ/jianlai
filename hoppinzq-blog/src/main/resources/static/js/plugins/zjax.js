/*
 * zjax.js
 * 基于JQuery
 * @author zq
 * @CopyRight by HOPPIN&HAZZ ~zq
 */
;(function () {
    "use strict";

    //为Function原型绑定三个方法，注意，擅自修改js的原型是十分危险且不能被大多数程序员接受，如果您的编码习惯或者公司不允许这样干:
    //1、请使用constructorJS方法或其他代理方法替换之。
    //2、可以写在你们公司JS框架原型,然后通过apply、call等方法借用。
    //3、非常建议写在JQuery原型$里面
    /**
     * 用于改变方法内部this指向
     * @param obj
     * @param args
     * @returns {function(...[*]): *}
     * @private
     */
    Function.prototype.__$zq_fn_bind = function (obj, ...args) {
        let thisFn = this;
        let fn = function (...args2) {
            let thisArgs = this instanceof fn ? this : obj;
            return thisFn.call(thisArgs, ...args, ...args2);
        }
        fn.prototype = Object.create(thisFn); //克隆
        return fn;
    };

    /**
     * 在方法执行前织入代码
     * @param fn
     * @returns {function(): *}
     * @private
     */
    Function.prototype.__$zq_fn_before = function (fn) {
        let me = this;
        return function () {
            fn.call(this, arguments); //修正this值
            return me.apply(this, arguments);
        }
    };

    /**
     * 在方法执行后织入代码
     * @param fn
     * @returns {function(): *}
     * @private
     */
    Function.prototype.__$zq_fn_after = function (fn) {
        let me = this;
        return function () {
            let ret = me.apply(this, arguments); //修正this值，并且执行原函数
            fn.apply(this, arguments);
            return ret;
        }
    };

    /**
     * 定义_Zjax基类
     * @private
     */
    function _Zjax() {
        //zjax队列
        this.zQueue = {
            "default": []
        };
        //内部用缓存，缓存storage的key
        this.cache = {
            "localStorage": [],
            "sessionStorage": [],
            "indexedDB": []
        };
        //zjax请求调度器，支持链请求
        this.requestChain = [];
        this.chainIndex = 0;
        //zjax配置型
        this.config = {
            type: 'GET', //请求类型默认是get请求
            url: null, //(必填项)请求的url
            dataType: null, //数据类型
            data: null, //传参
            contentType: null, //返回数据类型 application/json
            async: true, //同步异步
            timeout: 60000, //超时时长
            heads:{
                "X-Requested-With":"XMLHttpRequest"
            },

            method: null, //拓展：请求携带服务的方法(调用zq框架服务必填项)
            params: null, //拓展：请求携带服务的传参(调用zq框架服务必填项)
            serviceUrlPatten: "hoppinzq", //拓展：进入服务管理的凭证，也就是请求进入网关(调用zq框架服务必填项)
            serviceUrl: null, //拓展：服务地址，该参数会覆盖url(调用zq框架服务可选参数)
            encryptEnable: false, //拓展：参数是否加密(调用zq框架服务可选参数)
            encryptJsFileUrl: null, //拓展：传一个可用于加密的js的路径(调用zq框架服务可选参数)
            encryptFunction: null, //拓展：加密函数，或者执行传入的加密js的一个内部方法,请确保网关能正确解密(调用zq框架服务可选参数)
            isServiceCache: false, //拓展：是否让zq服务开启接口缓存，为false就是在url加入一个时间戳来阻止接口缓存(调用zq框架服务可选参数)

            isDebugger: true, //调试用

            storageCache: false, //拓展：是否开启前端缓存，相同的请求会直接返回Storage的缓存数据，场景：分页,游戏战绩等
            cacheType: "sessionStorage",//拓展：缓存类型，目前只支持localStorage，sessionStorage，indexedDB
            proxyUrl: null, //拓展：代理的url，url请求404后会请求代理的url
            isCookie: false, //拓展：请求是否需要携带cookie
            isLoading: false, //拓展：是否开启遮罩层，如果开启但是没有绑定dom，则默认绑定在body上
            loadingDom: null, //拓展：遮罩层绑定的dom的JQ对象,注意：如果使用dom去调用zjax，则该参数默认绑定该dom对象
            lockRequest: false, //拓展：是否锁定，防止因为没有加遮罩或者遮罩失效，人可以频繁点击导致重复请求，场景：狂点新增，点赞等
            lockTime: 5000, //拓展：锁定时间，锁定时间内的同一请求无效,-1表示只要请求没有接收到响应，就一直被锁定
            roundUrl: null, //拓展，对该接口进行轮询,该参数作为url的优先级为最高,而且会禁用掉遮罩、,而是是以客户端的性能为代价。
            roundTime: 5000, //拓展，轮询间隔，注意，该参数可以为0，这意味着你可以使用该功能攻击接口
            blockRequest: false, //拓展：是否阻塞已丢弃的请求(async必须为true时生效)，防止人频繁切换tab或者分页，同一个请求请求多次会始终被最后一个请求阻塞
            blockName: null, //拓展：可以给若干请求起一个别名，执行这类请求前会将页面上其他别名的并发请求中止
            //场景：1、有一个人进入首页，结果首页还没加载完就切换到tab2，然后又切换到tab3，由于没有发生路由跳转，主页跟tab2仍在请求，这时这些请求应该被关闭，防止tab3加载慢
            //如果用户频繁点击ajax请求，有两个问题：
            // ①、如果连续点击了5个ajax请求，前4个其实是无效的，趁早结束节省资源。
            // ②、更严重的问题是：最后一个发送的请求，响应未必是最后一个，有可能造成混乱。还需要一个队列(zQueue)来维护发送的请求和响应。
            isRedirect:false, //拓展，ajax原本是不支持重定向的，将该参数设为true我将手动帮你重定向，前提是响应头必须包含两个参数（见方法）
            isProgress: false, //拓展：是否开启接口加载监听，场景：文件上传时展示实时进度
            isSerialize: false, //拓展：(未实装，感觉没什么用)参数是否需要序列化，场景：表单提交

            sql: null, //拓展：已弃用，这是旧框架提供的一个可以传sql或者加密的sql的内部方法，
            csrfToken: null, //拓展：已弃用，csrfToken暴露在网页上的位置，场景：针对csrfToken被放到网页的meta标签或者某些隐藏的dom元素里
        };
        this.debugger = this.config.isDebugger;
    }

    /**
     * 只初始化方法
     */
    _Zjax.prototype.initFn=function (config){
        let me=this;
        if (config.beforeSend === undefined) {
            config.beforeSend = function () {
                me._debug("请求开始")
            }
        }
        if (config.complete === undefined) {
            config.complete = function () {
                me._debug("请求结束")
            }
        }
        if (config.error === undefined) {
            config.error = function () {
                me._debug("请求异常,响应码：" + arguments[0].status, true);
                me._debug(arguments[0], true);
            }
        }
        return config;
    },

    /**
     * 初始化配置
     * @param config
     * @returns {{url}|jQuery|boolean}
     */
    _Zjax.prototype.init = function (config) {
        let me = this;
        //合并配置型
        config = $.extend({}, me.config, $.getAjaxSettings, config);
        //url是ajax的必填项
        if (!config.url) {
            me._debug("url为必填项！", true);
            return false;
        }

        //初始化ajax方法
        //注意：尽量不要对ajax的xhr方法初始化。初始化也可以，必须将xhr对象还给JQuery，就像我这么干
        if (config.xhr === undefined) {
            config.xhr = function () {
                return $.ajaxSettings.xhr();
            }
        }
        if (config.beforeSend === undefined) {
            config.beforeSend = function () {
                me._debug("请求开始")
            }
        }
        if (config.complete === undefined) {
            config.complete = function () {
                me._debug("请求结束")
            }
        }
        if (config.error === undefined) {
            config.error = function () {
                me._debug("请求异常,响应码：" + arguments[0].status, true);
                me._debug(arguments[0], true);
            }
        }

        if (config.roundUrl != null) {
            config.storageCache = false;
            config.isLoading = false;
            config.lockRequest = false;
            config.blockRequest = false;
        }
        return config;
    }

    /**
     * ajax携带cookie
     * axios: withCredentials: true
     * Fetch: fetch(url, { credentials: 'include' })
     * @param config
     */
    _Zjax.prototype.setCookie = function (config) {
        if (config.isCookie) {
            config.xhrFields = {
                withCredentials: true
            }
        }
    }

    _Zjax.prototype.setRedirect=function (config) {
        let me=this;
        if(config.isRedirect){
            me.setCookie($.extend(config,{
                isCookie:true
            }));
            config.complete = config.complete.__$zq_fn_after(function (xhr,data) {
                if(xhr.status==302){
                    let url = xhr.getResponseHeader("redirect");
                    if(url==null){
                        me._debug("响应头缺少redirect");
                        return;
                    }
                    let enable = xhr.getResponseHeader("enableRedirect");
                    if(enable==null){
                        me._debug("响应头缺少enableRedirect");
                        return;
                    }
                    let responseBody = xhr.responseText;
                    alert(JSON.parse(responseBody).msg+",将跳转至登录页！")
                    if((enable == "true") && (url != "")){
                        let win = window;
                        while(win != win.top){
                            win = win.top;
                        }
                        win.location.href = url;
                    }
                }
            });
        }
    }

    /**
     * 启用遮罩
     * 这是通用遮罩，可以定制
     * 1、当zjax绑定在dom上时，遮罩会默认绑定在该dom上
     * 2、当启用遮罩但是loadingDom未绑定dom时，遮罩默认绑定在body上
     * 3、启用轮询会使遮罩失效
     * @param config
     */
    _Zjax.prototype.startLoading = function (config) {
        let me = this;
        //通用遮罩
        if (config.isLoading) {
            me._debug("已启用遮罩，开始添加遮罩");
            let $dom = config.loadingDom;
            config.beforeSend = config.beforeSend.__$zq_fn_before(function () {
                //创建遮罩dom，然后把遮罩绑定在dom上前先判断dom是否已经绑定过遮罩了，如果绑定过了，直接打开遮罩；否则将遮罩dom动态添加到dom内
                let $preloader = $('<div></div>').addClass('preloader');
                if ($dom === null) {
                    if ($("body").children('.preloader').length) {
                        $("body").children('.preloader').fadeIn();
                    } else {
                        $("body").prepend($preloader.fadeIn());
                    }
                    $dom = $("body");
                } else {
                    let element = $dom.get(0);
                    if (config.loadingDom != undefined && config.loadingDom != '') {
                        element = config.loadingDom.get(0);
                    }
                    if ($(element).children('.preloader').length) {
                        $(element).children('.preloader').fadeIn();
                    } else {
                        //获取要遮罩内容的位置，大小，滚动视距
                        let {
                            top,
                            left
                        } = element.getBoundingClientRect();
                        let {
                            scrollTop,
                            scrollLeft
                        } = document.body;
                        let dom_width = $dom.width();
                        let dom_height = $dom.height();
                        let $preloader = $('<div></div>').addClass('preloader')
                            .height(dom_height).width(dom_width).offset({
                                top: scrollTop,//scrollTop+top
                                left: scrollLeft //scrollLeft+left
                            })
                        //当遮罩绑定在body上时，是fixed，因为遮罩在body上要保持滚动时依然将整个网页遮罩。在dom上时要absolute，滚动时遮罩随着滚动
                        $dom.prepend($preloader.css("position", "absolute").fadeIn());
                    }
                }
            });
            config.complete = config.complete.__$zq_fn_after(function () {
                $dom.find(".preloader").fadeOut(500);
                me._debug("关闭遮罩");
            });
        }
    }

    /**
     * 锁定zjax请求
     * @param config
     * @returns {boolean}
     */
    _Zjax.prototype.lockRequest = function (config) {
        let me = this;
        if (config.lockRequest) {
            if (config.lockTime > 0) {
                if ((new Date().getTime() - parseInt(sessionStorage.getItem("zjax_lock_" + config.url +
                    "_requestTime"))) < config.lockTime) {
                    me._debug("请求被锁定，本次请求被取消");
                    return false;
                }
                config.beforeSend = config.beforeSend.__$zq_fn_before(function () {
                    me._debug("请求已被锁定" + config.lockTime + "ms，调用日期：" + new Date().toString());
                    sessionStorage.setItem("zjax_lock_" + config.url + "_requestTime", new Date().getTime().toString())
                });
            } else {
                if (sessionStorage.getItem("zjax_lock_" + config.url + "_request") == "0") {
                    me._debug("请求被锁定，本次请求被取消");
                    return false;
                }
                config.beforeSend = config.beforeSend.__$zq_fn_before(function () {
                    me._debug("请求已被锁定,等待执行接收到响应");
                    sessionStorage.setItem("zjax_lock_" + config.url + "_request", "0");//0被锁定
                });
                config.complete = config.complete.__$zq_fn_before(function () {
                    me._debug("请求解锁");
                    sessionStorage.setItem("zjax_lock_" + config.url + "_request", "1");//1解锁
                });
            }
        }
        return true;
    }

    /**
     * 开启缓存,只有响应里code为20x和304才缓存
     * @param config
     * @returns {string|boolean}
     */
    _Zjax.prototype.startCache = function (config) {
        let me = this;
        let mark = config.url + config.type + JSON.stringify(config.data);
        let enMark = window.btoa(mark);//加密url+请求类型+传参
        let cacheKey = "zjax_cache_" + enMark;
        switch (config.cacheType) {
            case "localStorage":
                if (!localStorage.getItem(cacheKey)) {
                    config.success = config.success.__$zq_fn_after(function (rep) {
                        if ((200 <= rep.code && rep.code < 300) || rep.code == 304) {
                            //缓存类型是localStorage的情况，存储返回值的base64码
                            let blob = new Blob([JSON.stringify(rep)]);
                            let reader = new window.FileReader();
                            reader.readAsDataURL(blob);
                            reader.onloadend = function () {
                                localStorage.setItem(cacheKey, reader.result.toString());
                            }
                            me.cache.localStorage.push({
                                "cacheKey": cacheKey,
                                "cacheTime": new Date().getTime().toString()
                            })
                        }

                    });
                    return false;
                } else {
                    return localStorage.getItem(cacheKey);
                }
                break;
            case "sessionStorage":
            default:
                if (!sessionStorage.getItem(cacheKey)) {
                    config.success = config.success.__$zq_fn_after(function (rep) {
                        if ((200 <= rep.code && rep.code < 300) || rep.code == 304) {
                            //缓存类型是sessionStorage的情况，存储返回值的URL对象，这是因为sessionStorage的生命周期是一次会话，而URL对象会随着
                            //用户刷新页面而被垃圾回收，此时sessionStorage也因会话结束被清除
                            let blobUrl = window.URL.createObjectURL(new Blob([JSON.stringify(rep)]));
                            sessionStorage.setItem(cacheKey, blobUrl);
                            me.cache.sessionStorage.push({
                                "cacheKey": cacheKey,
                                "cacheTime": new Date().getTime().toString()
                            })
                        }
                    });
                    return false;
                } else {
                    return sessionStorage.getItem(cacheKey);
                }
                break;
        }

    }

    /**
     * 开启可以自阻塞的请求
     * @param config
     */
    _Zjax.prototype.startBlockRequest = function (config) {
        let me = this;
        if (config.blockRequest && config.async) {
            let queueName = config.blockName;
            if (!queueName || queueName === "") queueName = "default";
            config.beforeSend = config.beforeSend.__$zq_fn_before(function () {
                let xhr = arguments[0][0];
                if (!me.zQueue[queueName]) {
                    me.zQueue[queueName] = [];
                }
                me.zQueue[queueName].push(xhr);
                me._debug("xhr对象： 👇"  + "已经被加入队列: " + queueName + ", 队列长度: " + me.zQueue[queueName].length);
                me._debug(xhr);
            });
            config.complete = config.complete.__$zq_fn_before(function () {
                let xhr = arguments[0][0];
                if (me.zQueue[queueName]) {
                    for (let i = 0; i < me.zQueue[queueName].length; i++) {
                        if (me.zQueue[queueName][i] == xhr) {
                            me.zQueue[queueName].splice(i, 1);
                            me._debug("xhr对象：👇" + " 已经被移除队列: " + queueName);
                            me._debug(xhr);
                            break;
                        }
                    }
                }
            });
            if (me.zQueue[queueName]) {
                me._debug("阻塞所有队列 " + queueName + " 里的请求: , 队列长度: " + me.zQueue[queueName].length);
                while (me.zQueue[queueName].length > 0) {
                    //弹出数组中的第一个元素
                    let xhr = me.zQueue[queueName].shift();
                    if (xhr) xhr.abort();
                }
            }
        }
    }

    /**
     * 开启文件上传监听
     * @param config
     */
    _Zjax.prototype.startFileUpload = function (config) {
        let me = this;
        if (config.isProgress) {
            config.xhr = function () {
                let xhr = $.ajaxSettings.xhr();
                //是否在上传文件
                if (xhr.upload) {
                    xhr.upload.addEventListener('progress', progressHandle, false);
                }
                //xhr对象返回给jQuery使用
                return xhr;
            };
        }
    }

    /**
     * 监听上传文件的progress事件
     * @param event
     */
    let progressHandle = function (event) {
        //上一次计算时间
        let lastTime = sessionStorage.getItem("_zjax_lastTime");
        if (!lastTime) {
            lastTime = 0;
        } else {
            lastTime = parseInt(lastTime);
        }
        //上一次计算的文件大小
        let lastSize = sessionStorage.getItem("_zjax_lastSize");
        if (!lastSize) {
            lastSize = 0;
        } else {
            lastSize = parseInt(lastSize);
        }
        if (lastTime == 0) {
            lastTime = new Date().getTime();
            lastSize = event.loaded;
            sessionStorage.setItem("_zjax_lastTime", String(lastTime));
            sessionStorage.setItem("_zjax_lastSize", String(lastSize));
            return;
        }
        let nowTime = new Date().getTime();
        let intervalTime = (nowTime - lastTime) / 1000;
        let intervalSize = event.loaded - lastSize;
        lastTime = nowTime;
        lastSize = event.loaded;
        let speed = intervalSize / intervalTime;
        let bSpeed = speed;//保存以b/s为单位的速度值，方便计算剩余时间
        let units = 'B/s';
        if (speed / 1024 > 1) {
            speed = speed / 1024;
            units = 'KB/s';
        }
        if (speed / 1024 > 1) {
            speed = speed / 1024;
            units = 'MB/s';
        }
        //剩余时间
        let leftTime = ((event.total - event.loaded) / bSpeed);
        //进度
        let progress = event.loaded / event.total * 100;
        console.log("当前进度：" + progress.toFixed(1) + "%    当前速度：" + speed.toFixed(1) + units + "   预计剩余时间：" + leftTime.toFixed(1) + "秒");
    }

    /**
     * 定时器，方法内部可通过id去关闭定时器
     * @param {Object} callback
     * @param {Object} time
     */
    let zinterval = function (callback, time) {
        var id = setInterval(() => callback(id), time);
    };

    /**
     * 开启轮询，轮询的url参数会替换url参数
     * 注意：轮询会禁用遮罩、缓存，不会被自己阻塞，也不会被锁定请求
     * @param config
     */
    _Zjax.prototype.startRound = function (config) {
        if (config.roundUrl != null) {
            config.url = config.roundUrl;

            zinterval(function (id) {
                $.ajax(config);
                //通过id关闭定时器
            }, config.roundTime)
        }
    }
    /**
     * 开启代理Url
     * @param config
     */
    _Zjax.prototype.startProxy = function (config) {
        let me = this;
        if (config.proxyUrl != null && !config.isProxy) {
            config.error = config.error.__$zq_fn_before(function () {
                if (arguments[0][0].status == "404") {
                    me._debug("原请求url 404 not found，将请求代理的url");
                    config.isProxy = true;
                    config.url = config.proxyUrl;
                    $.ajax(config);
                } else {
                    me._debug("原请求出现非404错误，代理不会生效");
                }
            });
        }
        ;
    }

    /**
     * 执行基础的ajax
     * 通过若干start方法，可以定制你的具有特定功能的ajax
     * @param config
     */
    _Zjax.prototype.baseAjax = function (config) {
        let me = this;
        config = me.init(config);
        if (!config)
            return;
        if(config.isRedirect){
            me.setRedirect(config);
            $.ajax(config);
        }
        me.setCookie(config);
        me.startRound(config);
        me.startLoading(config);
        if (!me.lockRequest(config))
            return;
        if (config.storageCache) {
            let cacheDate = me.startCache(config);
            if (cacheDate) {
                me._debug("找到缓存，将请求重定向至缓存");
                $.ajax({
                    url: cacheDate,
                    success: config.success
                });
                return;
            }
        }
        me.startProxy(config);
        me.startFileUpload(config);
        me.startBlockRequest(config);
        $.ajax(config);
    }

    /**
     * 调试模式，当配置项的isDebugger为true时将开启调试模式
     * @param sMessage 内部返回调试信息
     * @param bError 调试级别是否是错误
     * @private
     */
    _Zjax.prototype._debug = function (sMessage, bError) {
        if (!this.debugger) return;
        if (bError) {
            console.error(sMessage);
            return;
        }
        console.log(sMessage);
    }

    /**
     * 设置zjax是否开启调试模式
     * @param isDebugger
     */
    _Zjax.prototype.setDebugger = function (isDebugger) {
        this.debugger = isDebugger ? true : false;
    }

    /**
     * 按顺序将请求配置推入请求链
     * @param config
     */
    _Zjax.prototype.zjaxChain = function (config) {
        let me = this;
        config = me.init(config);
        config.requestFlag = 0;
        config.complete = config.complete.__$zq_fn_before(function () {
            config.requestFlag = 1;
            me.chainIndex++;
            if (me.chainIndex < 0 || me.chainIndex >= me.requestChain.length) {
                me._debug("请求链结束");
            }
            let cfg = me.requestChain[me.chainIndex];
            if (cfg && cfg.requestFlag == 0) {
                me.baseAjax(cfg);
            }
        });
        me.requestChain.push(config);
        return me;
    }


    /**
     * 手动执行链的zjax
     */
    _Zjax.prototype.runZjax = function () {
        let me = this;
        me._debug("zjax请求链开始，请求链长度: " + me.requestChain.length);
        if (me.requestChain.length > 0)
            me.baseAjax(me.requestChain[0]);
    }


    if (typeof ($zq_zjax) == "undefined") {
        var $zq_zjax = {
            //创建zjax对象
            createZjax: new _Zjax(),
            //传及zjax执行链
            zjaxChain: function (config) {
                return this.createZjax.zjaxChain(config);
            },
            /**
             * 执行带有配置型的zjax
             * @param config
             */
            zjax: function (config) {
                this.createZjax.baseAjax(config);
            },
            /**
             * 自阻塞ajax
             * @param config
             */
            zBjax: function (config){
                let _zjax=this.createZjax;
                config=$.extend({}, {
                    async: true,
                    isDebugger: true,
                    blockRequest: false,
                    blockName: null,
                    isRedirect: true
                },config);
                _zjax.initFn(config);
                _zjax.startBlockRequest(config);
                _zjax.setRedirect(config);
                $.ajax(config);
            },
            /**
             * 携带cookie与重定向ajax
             * @param config
             */
            zCjax:function (config) {
                let _zjax=this.createZjax;
                config=$.extend({}, {
                    isDebugger: true,
                    isCookie:true,
                    isRedirect:true
                },config);
                _zjax.initFn(config);
                _zjax.setRedirect(config);
                $.ajax(config);
            },
            /**
             * 带有遮罩的ajax todo
             * @param config
             */
            zLjax:function (config){
                let _zjax=this.createZjax;
                config=$.extend({}, {
                    isDebugger: true,
                    isRedirect: true,
                    beforeSend:function () {
                        console.log("遮罩开启")
                    },
                    complete:function () {
                        console.log("遮罩关闭");
                    }
                },config);
                _zjax.initFn(config);
                _zjax.setRedirect(config);
                $.ajax(config);
            },
            /**
             * 获取ajax的配置项
             * @returns {jQuery.ajaxSettings|{async, processData, accepts, contents, flatOptions, global, converters, type, contentType, responseFields, url, isLocal}|jQuery.ajaxSettings}
             */
            getAjaxSettings: function () {
                return $.ajaxSettings;
            },
            /**
             * 为ajax设置全局配置，为某项配置项设置之后，所有的ajax都会使用该配置项
             * 如：在全局配置了超时时长为10s，则所有的zjax超时时长均为10s而不是默认的30s。
             * Q：为什么通过createZjax方法new出来的对象里的配置项都能全局改？
             * A：@see init方法里的合并配置项，配置项优先级是 传入的配置项(定制)>ajax配置项(全局配置)>zjax默认配置项(写死)，通过全局修改ajax配置项，实现全局修改zjax配置项
             * @param settings
             */
            setZjaxSettings: function (settings) {
                $.ajaxSetup($.extend({}, settings));
            }
        }
        $.extend($, $zq_zjax);
    }

    /**
     * 将zjax绑定在dom的JQuery对象上
     * 注意：无论方法内部做了一些什么事，必须返回绑定的JQuery的Dom对象，不要让JQuery的dom链式操作在你的方法后断掉！！！！
     * @param config 配置项
     * @returns {*|jQuery.fn.init|jQuery|HTMLElement}
     */
    $.fn.zjax = function (config) {
        //注意：这里的this指向绑定在JQuery的dom对象
        if (this.length == 0) //如果dom的JQuery对象不存在，直接返回该对象
            return $(this);
        if (this.length > 1) { //如果dom的JQuery对象有多个，给每个对象绑定
            for (let i = 0; i < this.length; i++)
                $(this[i]).zjax($.extend(config, {$dom: $(this)}));
            return $(this);
        }
        $zq_zjax.zjax($.extend(config, {loadingDom: $(this), $dom: $(this)}));
        return $(this);
    };
})();