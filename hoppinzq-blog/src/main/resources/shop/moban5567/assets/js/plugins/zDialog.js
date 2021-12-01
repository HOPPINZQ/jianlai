/*
 * zDialog.js
 * 基于JQuery,JQueryUI,Bootstrap
 * @author zq
 * @CopyRight by HOPPIN&HAZZ ~zq
 */
;(function () {
    "use strict";

    function _ZDialog() {
        //_zDialog配置型
        let me = this;
        let defaultConfig = {
            dialogWidth: false,//默认弹框宽为600px
            dialogResizable: false,//默认弹框不能缩放
            dialogTop: 100,//默认距离顶部100px+xpx
            dialogType: 1,//默认弹框类型
            time: false,//默认不自动关闭
            head: {//默认头部配置
                title: "标题",//默认头部标题
                isDrag: false,//默认不能拖拽
                closeFn: function () {//默认点击x前回调，在回调内return false将不会关闭弹框

                }
            },
            body: {//默认身体
                html: "",//默认内容
                templet: "",//默认模板用[]占位
                data: {},//模板的数据
            },
            footer: {//默认尾部
                btn: [//默认按钮
                    // {
                    // 	btnText:"确定",
                    // 	btnFn:function(){
                    // 		zq.alert("点击了确定")
                    // 	}
                    // },
                    // {
                    // 	btnType:"cancel"
                    // }
                ]
            }
        };
        me.config = defaultConfig;
        let defaultType = {
            alert: function (options) {
                me.clearHeader();
                me.setHtml(me.isUndefined(options.html) ? "内容" : `<p style='height: 50px;font-size: 16px;line-height: 50px;'>${options.html}</p>`);
                me.setButton(me.isUndefined(options.btn) ? [{
                    btnText: "好的",
                    btnFn: function () {
                        me.closeAllDialog();
                    }
                }] : options.btn);
                return me.getConfig();
            },
            /**
             * 默认confirm
             * 必传参数：title,body,btn
             * @param options
             */
            confirm: function (options) {
                me.setTitle(me.isUndefined(options.title) ? "确定" : options.title);
                me.setHtml(me.isUndefined(options.html) ? "内容" : options.html);
                me.setButton(me.isUndefined(options.btn) ? [{
                    btnText: "确定",
                    btnFn: function () {
                        me.closeAllDialog();
                    }
                },
                    {
                        btnType: "cancel"
                    }] : options.btn);
                return me.getConfig();
            },
        }
        me.defaultType = defaultType;
    }

    _ZDialog.prototype = {
        constructor: _ZDialog,
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
        loadImage: function (url, dom) {
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
                image.src = "404的图片路径";
                if (!me.isUndefined(dom)) {
                    $(dom).append(image);
                }
            };
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
         * @param originFun
         * @param before
         * @param after
         * @returns {_class}
         */
        constructorJS: function (originFun, before, after) {
            function _class() {
                before.apply(this, arguments);
                originFun.apply(this, arguments);
                after.apply(this, arguments);
            }

            return _class;
        }
    }

    /**
     * 通过弹框返回的唯一索引关闭弹框
     * @param index
     */
    _ZDialog.prototype.closeModel = function (index) {
        $("." + index).remove();
    }
    /**
     * 关闭所有弹框
     * @param title
     */
    _ZDialog.prototype.closeAllDialog = function (title) {
        $(".dialog").remove();
    }

    /**
     * 设置标题
     * @param title
     */
    _ZDialog.prototype.setTitle = function (title) {
        let me = this;
        me.config.head.title = title;
    }
    _ZDialog.prototype.setHtml = function (html) {
        let me = this;
        me.config.body.html = html;
    }
    _ZDialog.prototype.setButton = function (button) {
        let me = this;
        me.config.footer.btn = button;
    }
    _ZDialog.prototype.clearHeader = function () {
        let me = this;
        me.config.head = null;
    }
    _ZDialog.prototype.clearFooter = function () {
        let me = this;
        me.config.footer = null;
    }
    _ZDialog.prototype.getConfig = function () {
        let me = this;
        return me.config;
    }


    /**
     * 弹出内置弹框
     * @param type
     * @param config
     * @returns {{url}|jQuery|boolean|*}
     */
    _ZDialog.prototype.defaultDialog = function (type, config) {
        let me = this;
        if (me.defaultType[type]) {
            config = me.defaultType[type].apply(me, Array.prototype.slice.call(arguments, 1));
            return me.baseDialog(config);
        } else if (typeof type === 'object' || !type) {
            return me.each(function () {
                me.createZDialog.baseDialog(config);
            });
        } else {
            $.error('方法 ' + type + '不存在');
        }
    }

    /**
     * 弹出提示框
     */
    _ZDialog.prototype.baseMsg=function (config){
        $(".model-msg").remove();
        let model_index = $(".model-msg").length;//获取当前页面有多少弹框
        let model_class = "model_" + model_index;//生成唯一的class
        let $dialog = $("<div></div>").addClass("dialog-tips-base model-msg").append($("<div></div>").addClass(
            "dialog-tips-base-msg").append($("<span></span>").append(config.html)));
        $dialog.appendTo($("body"))
        setTimeout(function() {
            $dialog.remove();
        }, config.time || 2000);
        //弹框成功将返回一个自定义对象给调用方，方便调用方根据回调内容做一些操作，如通过对象的model_class去关闭弹框
        return {
            "component_type": "msg",
            "model_class": model_class
        };
    },
    /**
     * 配置弹框
     * @param config
     * @returns {{url}|jQuery|boolean}
     */
    _ZDialog.prototype.baseDialog = function (config) {
        let me = this;
        config = $.extend({}, me.config, config);
        let model_index = $(".model").length;//获取当前页面有多少弹框
        let model_class = "model_" + model_index;//生成唯一的class
        let headConfig = config.head;//获取头部配置项
        let bodyConfig = config.body;//获取身体配置项
        let footerConfig = config.footer;//获取尾部配置项
        let $model_close;
        let $mode_title;
        let $model_header;
        if(headConfig!=null){
            $model_close = $("<button></button>").addClass("close").append("<span>×</span>");//生成右上x的JQ对象
            $mode_title = $("<span></sapn>").addClass("modal-title").append(headConfig.title);//生成标题的JQ对象
            $model_header = $("<div></div>").addClass("modal-header");//生成弹框顶部JQ对象
            $model_header.append($mode_title).append($model_close);//装配顶部
        }
        let $mode_body;//定义身体的JQ对象
        if (bodyConfig.html == "") {
            //如果配置项的html参数为空(注意由于html是默认配置项的内容，在合并的时候要么有值，要么是默认的"")，就加载模板内容~~之后会将
            $mode_body = $("<div></div>").addClass("modal-body model-body-main").append(me.loadTemplete(bodyConfig.templete, bodyConfig.data));
        } else {
            //如果配置项html参数不为空，将html填充至弹框身体内
            $mode_body = $("<div></div>").addClass("modal-body model-body-main").append(bodyConfig.html);
        }
        let $mode_footer;//定义尾部的JQ对象
        if(footerConfig!=null){
            if (footerConfig.btn.length == 0) {
                //如果用户没有配置，将尾部JQ对象置为空，这样就装配不了尾部了
                $mode_footer = "";
            } else {
                //如果用户配置了尾部
                let btnConfig = footerConfig.btn;//获取按钮的配置项
                $mode_footer = $("<div></div>").addClass("modal-footer");//生成尾部的JQ对象
                //遍历按钮配置项，装配按钮至尾部JQ对象
                $.each(btnConfig, function (index, value) {
                    if (me.isUndefined(value.btnType)) {
                        //配置项无btnType，用配置项的配置
                    } else if (value.btnType == "cancel") {//配置项有btnType，用预设的配置
                        value = {
                            btnText: "取消",
                            btnFn: function () {
                                $model.remove();
                            }
                        }
                    }
                    //装配按钮
                    $("<a>" + value.btnText + "</a>").on("click", function () {
                        me.constructorJS(value.btnFn(), $model.remove());
                    }).appendTo($mode_footer);
                })
            }
        }
        let $model = $("<div></div>").addClass("modal");//生成最外层JQ对象，这个承载了整个弹框的DOM的JQ对象包括遮罩
        let $model_dialog = $("<div></div>").addClass("modal-dialog")//生成弹框外围元素的JQ对象，这个是用于弹框外围定位
        if (config.dialogWidth && me.elementType(config.dialogWidth) == "number") {
            $model_dialog.width(config.dialogWidth);
        }
        if (config.dialogTop && me.elementType(config.dialogTop) == "number") {
            $model_dialog.offset({top: config.dialogTop});
        }
        let $model_content = $("<div></div>").addClass("modal-content");//生成弹框内部元素定位的JQ对象
        if(headConfig!=null){
            $model_content.append($model_header);
        }
        $model_content.append($mode_body);
        if(footerConfig!=null){
            $model_content.append($mode_footer);
        }
        $model.append($model_dialog.append($model_content))
            .addClass(model_class).appendTo("body").show();//整个弹框及模态框装配进入body并展示出来
        if(headConfig!=null){
            $model_close.on("click", function () {//右上x动态绑定回调
                me.timeout().then(function () {
                    if (headConfig.closeFn === undefined) {//先执行回调函数后再关闭弹框，如果回调函数返回false将不会关闭弹框
                        return me.timeout(200).then(function () {
                            $model.remove();
                        });
                    } else {
                        headConfig.closeFn();
                        return false;
                    }
                })
            });
            if (headConfig.isDrag) {//如果配置项（顶部）允许拖拽，将可以拖拽，基于JQueryUI
                $model.children(".modal-dialog").draggable({scroll: true, cursor: "move", handle: ".modal-header"});
            }
        }
        if (config.time && me.elementType(config.time) == "number") {//如果配置项有时间，则等待一段时间自动关闭弹框
            setTimeout(function () {
                $model.remove()
            }, config.time);
        }
        if (config.dialogResizable) {//如果配置项允许缩放，则可以缩放，基于JQueryUI
            $model.children(".modal-dialog").resizable({minHeight: 150, minWidth: 200,});
        }
        //弹框成功将返回一个自定义对象给调用方，方便调用方根据回调内容做一些操作，如通过对象的model_class去关闭弹框
        return {
            "component_type": "model",
            "model_class": model_class
        };
    }

    if (typeof ($zq_zdialog) == "undefined") {
        let $zq_zdialog = {
            createZDialog: new _ZDialog(),
            zdialog: function (config, type) {
                if (type) {
                    return this.createZDialog.defaultDialog(type, config);
                } else {
                    return this.createZDialog.baseDialog(config);
                }
            },
            zmsg:function (config){
                if(!config.time){
                    config.time=2000;
                }
                return this.createZDialog.baseMsg(config);
            }
        }
        $.extend($, $zq_zdialog);
    }
})();