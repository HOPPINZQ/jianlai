let ipconfig=__zqBlog.ipConfig;
let ip=ipconfig.ip_;
let fileIp=ipconfig.fileServer_;
let blogPort=ipconfig.blogPort;

//保存页面所有操作信息与操作对象，将回显初值
let zq = {
    blogId:__zqBlog.uuid(32, 62),
    blogTypeCode: 1,//博客类型fwb 1,markdown 2,fwb_simple 0
    blogType: "fwb",//博客类型fwb,markdown,fwb_simple
    csdnLink:"",
    csdnData:null,
    editor: null,//编辑器对象
    blogMarkdown: ">在这里写博客",
    blogText: "",
    blogHtml: "",
    blogTitle: "",
    blogDescription: "",
    blogHeadImage: "",//https://hoppinzq.com/static/image/Steam_G1vTRZ0hp8.png
    blogClassBig: [
        {label: "大选项1", value: 10},
        {label: "大选项2", value: 1},
        {label: "大选项3", value: 2},
        {label: "大选项4", value: 3},
        {label: "大选项5", value: 4},
        {label: "大选项6", value: 5}
    ],
    blogClassBigSelected: "",
    blogClassSmall: [
        {label: "123123", value: 10},
        {label: "asdsa", value: 1},
        {label: "zxc", value: 2},
        {label: "小选项4", value: 3},
        {label: "小选项5", value: 4},
        {label: "小选项6", value: 5}
    ],
    blogClassSmallSelected: [],//["1", "2"]
    isBlogCreateYourSelf: 0,
    isBlogCommit: 0,
    blogCopyLink: "",
    blogLevel: 5,
    blogInterval:null
};

//保存页面所有要初始化的方法
let _zqInit = {
    zq: zq,
    //初始化简单富文本编辑器
    initSimpleEditor: function () {
        let me = this;
        let simpleEditor = new MediumEditor('.editable', {
            toolbar: {
                buttons: ['extensionBtnX', 'bold', 'italic', 'underline', 'strikethrough', 'quote', 'anchor', 'image', 'justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull', 'orderedlist', 'unorderedlist', 'pre', 'removeFormat', 'outdent', 'indent', 'h2', 'h3', 'html'],
            },
            extensions: {
                extensionBtnX: new ExtensionBtnX()
            },
            buttonLabels: 'fontawesome',
            elementsContainer: document.getElementById('container')
        })

        //为简单富文本编辑器添加图片插件
        $('.editable').mediumInsert({
            editor: simpleEditor,
        });

        //绑定监听简单富文本内容改变事件
        simpleEditor.subscribe('editableInput', function (event, editable) {});
        zq.editor = simpleEditor;//编辑器存放
    },

    //初始化文件上传组件
    initFileLoader: function () {
        let me = this;
        //初始化附件上传组件，可上传任意类型，小于100M
        $("#blog_fj").fileinput({
            maxFileCount: 1,
            showUpload: false,
            showCaption: false,
            browseClass: "btn btn-success btn-sm",
            fileType: "any",
            maxFileSize: 1024 * 100,
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
        });

        //简易的验证  大小 格式
        $('#avatarInput').on('change', function (e) {
            var filemaxsize = 1024 * 5;//最大是5M
            var target = $(e.target);
            var Size = target[0].files[0].size / 1024;
            if (Size > filemaxsize) {
                alert('图片过大，请重新选择!');
                $(".avatar-wrapper").children().remove;
                return false;
            }
            //image/png,image/jpg...
            if (!this.files[0].type.match(/image.*/)) {
                alert('请选择正确的图片!')
            } else {
                var filename = document.querySelector("#avatar-name");
                var texts = document.querySelector("#avatarInput").value;
                var teststr = texts;
                testend = teststr.match(/[^\\]+\.[^\(]+/i); //直接完整文件名的
                filename.innerHTML = testend;
            }

        });

        $(".avatar-save").on("click", function () {
            var img_lg = document.getElementById('imageHead');
            // 截图小的显示框内的内容
            html2canvas(img_lg, {
                allowTaint: true,
                taintTest: false,
                onrendered: function (canvas) {
                    canvas.id = "mycanvas";
                    //生成base64图片数据
                    //var dataUrl = canvas.toDataURL("image/jpeg");
                    //blob对象丢给后台
                    canvas.toBlob(function (blob) {
                        var newImg = document.createElement("img"),
                            url = URL.createObjectURL(blob);
                        newImg.onload = function () {
                            URL.revokeObjectURL(url);
                        };
                        newImg.src = url;
                        document.body.appendChild(newImg);
                        let formData = new FormData();
                        formData.append("img", blob, document.querySelector("#avatarInput").value.match(/[^\\]+\.[^\(]+/i));//文件名
                        $.ajax({
                            url: fileIp+"/baseFile/fileUpload",//先写死
                            data: formData,
                            type: "POST",
                            contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                            processData: false,// 告诉jQuery不要去处理发送的数据
                            success: function (re) {
                                if (re.success == '1') {
                                    zq.blogHeadImage = re.data.fileRealName;
                                    let $aimage = $(".blog_head_image_part");
                                    if ($aimage.find("img").length) {
                                        $aimage.find("img").attr('src', re.data.filePath);
                                    } else {
                                        $aimage.append(`<img src="${re.data.filePath}">`);
                                    }
                                    $(".blog_head_image_set").hide();
                                    $(".blog_head_image").show();
                                } else {
                                    alert("文件服务器错误")
                                }
                            }
                        });
                    }, "image/png", 1);
                }
            });
        })
    },

    save2Redis:function (){
        let me=this;
        let editor = zq.editor;
        let message="";
        if (zq.blogType == "fwb") {
            // 获取编辑器内容
            let blogHtml = editor.txt.html();
            message = blogHtml;
        } else if (zq.blogType == "fwb_simple") {
            let blogHtml = editor.elements[0].innerHTML;
            message = blogHtml;
        } else if (zq.blogType == "markdown") {
            let markdownText = editor.getMarkdown();
            message = markdownText;
        }
        let blogSaveData={
            "id":zq.blogId,
            "text":window.btoa(window.encodeURIComponent(message)),
            "html":""
        }
        let blog=me.collectData(blogSaveData);
        $.zBjax({
            url:ip+":"+blogPort+"/hoppinzq?method=saveBlog2Redis",
            type:"post",
            blockRequest:true,
            blockName:"zjax_save_blog2Redis",
            data:JSON.stringify({
                "blog":blog
            }),
            dataType:"json",
            beforeSend:function (){
                $(".re-save-blog").toggleClass("fa-spin");
            },
            success:function (data){
                if(data.code!=200){
                    $("#blog_update_time").html(`<span class='alert-danger'>保存草稿失败！</span>`);
                }else{
                    $("#blog_update_time").text(data.data.lastUpdateTime);
                }
            },
            error:function (){
                $("#blog_update_time").html(`<span class='alert-danger'>保存草稿失败！</span>`);
                __zqBlog._debug("保存草稿失败！",true)
            },
            complete:function (){
                $(".re-save-blog").delay(2000).toggleClass("fa-spin");
            }
        })
    },
    //每30s保存一次草稿
    startSave2Redis:function () {
        let me=this;
        $("#blog_update_time").text(__zqBlog.getRealDate());
        zq.blogInterval=setInterval(function (){
            me.save2Redis();
        },30000)
    },
    //博客页面1初始化方法
    page1Init: function () {
        let me = this;
        $(".myaccount-tab-menu").find("a").eq(0).css("pointer-events", "all");
        //为博客构建页面下一步动态添加loading与绑定事件
        $(".step1-2-2").buttonLoading().off("click").on("click", function () {
            let $me = $(this);
            if ($me.data("check") === undefined || !$me.data("check")) {//按钮初始化状态
                if (zq.blogType == "csdn" && $("#csdn_blog_link").val() == "") {
                    alert("请填写url");
                    $("#csdn_blog_link").focus();
                    return;
                }
                //弹出zdialog组件
                let dialogIndex = $.zdialog({
                    html: "一旦选择构建方式后无法修改，是否确认？",
                    btn: [{
                        btnText: "确定",
                        btnFn: function () {
                            //开启redis存储草稿
                            me.startSave2Redis();
                            switch (zq.blogType) {
                                case "fwb_simple"://简单富文本
                                    $me.data("check", "1");
                                    $(".editable_fwb").remove();
                                    $("#container").show();
                                    $("#editable_markdown").remove();
                                    me.turnToNext(1);
                                    break;
                                case "markdown"://markdown初始化
                                    $me.data("check", "1");
                                    //markdown编辑器设置初始值
                                    $("#editable_markdown_textarea").val(zq.blogMarkdown);
                                    $("#container").remove();
                                    $(".editable_fwb").remove();
                                    $("#editable_markdown").show().css("z-index", 1111);
                                    let markdownEdit = editormd("editable_markdown", {
                                        width: "100%",
                                        height: 460,
                                        syncScrolling: "single",
                                        saveHTMLToTextarea: true,    // 保存HTML到Textarea
                                        searchReplace: true,
                                        path: "static/lib/",
                                        imageUpload: true,
                                        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                                        imageUploadURL: fileIp+"/baseFile/markdown",
                                        crossDomainUpload: true,
                                        onload: function () {
                                            __zqBlog._debug("markdown编辑器初始化成功");
                                        }
                                    });
                                    zq.editor = markdownEdit;
                                    $(".markdown_top").show();
                                    //markdown帮助
                                    $("#markdown_help").click(function () {
                                        $.get("static/test.md", function (md) {
                                            markdownEdit.clear();
                                            markdownEdit.appendMarkdown(md);
                                            markdownEdit.fullscreen();
                                        });
                                    });
                                    //markdown编辑器全屏
                                    $("#markdown_width").click(function () {
                                        markdownEdit.fullscreen();
                                    });
                                    //导入md文件
                                    $("#markdown_import").click(function () {
                                        let $fileMarkdown = $("#markdown_file");
                                        $fileMarkdown.click();
                                        $fileMarkdown.change(function () {
                                            let file = this.files[0];
                                            if (file) {
                                                if (file.name.indexOf(".md") == -1) {
                                                    alert("只支持上传.md后缀的文件");
                                                    return;
                                                }
                                                let reader = new FileReader();
                                                reader.readAsText(file, "utf-8");
                                                reader.onload = function () {
                                                    //this.result
                                                    let blobUrl = window.URL.createObjectURL(new Blob([this.result]));
                                                    $.get(blobUrl, function (md) {
                                                        markdownEdit.clear();
                                                        markdownEdit.appendMarkdown(md);
                                                    });
                                                }
                                            }
                                        });
                                    });
                                    //隐藏/显示右侧markdown关闭实时预览域
                                    $("#markdown_watch").click(function () {
                                        let $me = $(this);
                                        if ($me.data("text") === "关闭实时预览") {
                                            $me.data("text", "开启实时预览");
                                            markdownEdit.unwatch();
                                        } else {
                                            $me.data("text", "关闭实时预览");
                                            markdownEdit.watch();
                                        }
                                        $me.text($me.data("text"));
                                    });
                                    //绑定伸缩事件，只向下
                                    $("#editable_markdown").resizable({
                                        handles: "s",
                                        minHeight: 50,
                                        autoHide: true,
                                        animate: true,
                                        alsoResize: "#editable_markdown_textarea",
                                        helper: "blog-resizable-helper"
                                    });
                                    me.turnToNext(1);
                                    break;

                                case "csdn"://csdn初始化,csdn使用富文本作为编辑器👇
                                    $me.data("check", "2").buttonLoading('start');
                                    zq.csdnLink=$("#csdn_blog_link").val();
                                    $.zCjax({
                                        url:ip+":"+blogPort+"/hoppinzq?method=csdnBlog&params={'csdnUrl':'"+zq.csdnLink+"'}",
                                        success:function (msg){
                                            let json=JSON.parse(msg);
                                            console.log(json);
                                            if(json.code==200){
                                                zq.csdnData=json.data;
                                                setTimeout(function () {
                                                    $me.data("check", "1");
                                                    me.turnToNext(1);
                                                    $me.buttonLoading("stop");
                                                    me.fwbInit();
                                                }, 1000);//这个时间先写死
                                            }
                                        },
                                        error:function (data){
                                            console.log(data)
                                        }
                                    })
                                    break;
                                case "fwb"://富文本（默认）初始化
                                default:
                                    me.fwbInit();
                                    me.turnToNext(1);
                                    $me.data("check", "1");
                                    break;

                            }
                            //博客构建后，博客构建页所有组件置为不可用
                            $(".step1").find("input,label").each(function (index, element) {
                                let $me = $(element);
                                $me.attr("disabled", "disabled").addClass("cursor-not-allowed");
                            })
                        }
                    },
                        {
                            btnType: "cancel"
                        }
                    ]
                }, "confirm");
            } else if ($me.data("check") == "1") {//按钮成功进入下一页状态
                me.turnToNext(1);
            } else if ($me.data("check") == "2") {//按钮处于等待状态
                return;
            } else {//按钮异常状态
                return;
            }
        });
    },

    fwbInit:function (){
        $("#container").remove();
        $(".editable_fwb").show();
        $("#editable_markdown").remove();
        let E = window.wangEditor
        let editor = new E('.editable_fwb');
        // 挂载highlight插件，代码高光
        editor.highlight = hljs;
        //editor.customConfig.uploadImgMaxSize = 3 * 1024 * 1024;//限制图片大小为3M
        editor.config.uploadImgServer = fileIp+'/baseFile/fwb';
        editor.config.uploadFileName = 'fwbFileImg';
        editor.create();
        //zq.csdnData
        if(zq.csdnData!=null){
            editor.txt.html(zq.csdnData.html)
        }else{
            editor.txt.html(zq.blogHtml) // 重新设置编辑器内容
        }
        //zq.editor.destroy();//销毁原来的简单富文本
        zq.editor = editor;//将富文本编辑对象存入
        //为富文本编辑域添加竖向伸缩事件
        $(".w-e-text-container").resizable({
            handles: "s",
            minHeight: 50,
            animate: true,
            autoHide: true,
            helper: "blog-resizable-helper"
        });
    },

    //博客页面2初始化方法
    page2Init: function () {
        let me = this;
        //为编辑页面下一页绑定点击事件
        $(".step2-2-3").click(function () {
            if (zq.editor == null) {
                $.zdialog({
                    html: "未找到编辑器，请重新编写！",
                }, "alert");
                return;
            }
            //清除定时器保存草稿的功能
            clearInterval(zq.blogInterval);
            let editor = zq.editor;
            if (zq.blogType == "fwb") {
                // 获取编辑器内容
                let blogText = editor.txt.text();
                if (blogText.length == 0) {
                    $.zdialog({
                        html: "不写点东西吗？",
                    }, "alert");
                    return;
                }
                zq.blogText = blogText;
                __zqBlog._debug("博客文本是：" + blogText);
                let blogHtml = editor.txt.html();
                __zqBlog._debug("博客html是：" + blogHtml);
                zq.blogHtml = blogHtml;
                me.turnToNext(2);
            } else if (zq.blogType == "fwb_simple") {
                let blogText = editor.elements[0].innerText;
                if (blogText.length == 0) {
                    $.zdialog({
                        html: "不写点东西吗？",
                    }, "alert");
                    return;
                }
                zq.blogText = blogText;
                __zqBlog._debug("博客文本是：" + blogText);
                let blogHtml = editor.elements[0].innerHTML;
                __zqBlog._debug("博客html是：" + blogHtml);
                zq.blogHtml = blogHtml;
                me.turnToNext(2);
            } else if (zq.blogType == "markdown") {
                let markdownText = editor.getMarkdown();
                if (markdownText.length == 0) {
                    $.zdialog({
                        html: "不写点东西吗？",
                    }, "alert");
                    return;
                }
                __zqBlog._debug("markdown文本是：" + markdownText);
                zq.blogText = markdownText;
                zq.blogMarkdown = markdownText;
                let markdownHtml = editor.getHTML();
                __zqBlog._debug("markdown的Html是：" + markdownHtml);
                zq.blogHtml = markdownHtml;
                me.turnToNext(2);
            }
        });
        //手动保存到草稿
        $(".step2-save-cg").click(function () {
            me.save2Redis();
        })
    },

    //博客页面3初始化方法
    page3Init: function () {
        let me = this;
        //初始化博客标题
        $("#blog_title").val(zq.blogTitle);
        //初始化博客描述
        $("#blog_description").val(zq.blogDescription);
        //初始化博客封面图片
        if (zq.blogHeadImage != "") {
            $(".blog_head_image_set").hide();
            let image = new Image();
            image.src = zq.blogHeadImage;
            image.onload = function () {
                $(".blog_head_image_part").append(this);
            };
            image.onerror = function (e) {
                image.src = "https://hoppinzq.com/static/image/dignitas.png";//404图片路径
                $(".blog_head_image_part").append(image);
            };
            $(".blog_head_image").show();
        } else {
            _zqInit.initBlogHeadImage();
        }
        //阻止事件冒泡
        $(".reload_blog_head_image").click(function (e) {
            e.preventDefault();
        })

        //初始化博客星星
        $(".star-blog").each(function (index, element) {
            if (index < zq.blogLevel) {
                $(element).addClass("star-selected")
            }
        });

        //为博客打星（鼠标移入或者点击）添加事件
        $(".star-blog").on("mouseenter click", function () {
            let level = this.getAttribute("id").split("level")[1];
            zq.blogLevel=level;
            $(".star-blog").removeClass("star-selected");
            for (let i = level; i > 0; i--) {
                $("#level" + i).addClass("star-selected")
            }

        });

        //为博客选择类别初始化数据
        zq.blogClassSelectBigCompont = _zqInit.initBlogClassBig(zq.blogClassBig, zq.blogClassBigSelected);
        zq.blogClassSelectSmallCompont = _zqInit.initBlogClassSmall(zq.blogClassSmall, zq.blogClassSmallSelected);

        //为博客类别(小类)新增页初始化数据
        let blogTagDefault = new Tag("blog_tag_default");
        blogTagDefault.tagValue = zq.blogClassBig;
        blogTagDefault.isDisable = true;
        blogTagDefault.initView();
        let blogTagActive = new Tag("blog_tag_active");
        blogTagActive.tagValue = zq.blogClassSmall;
        blogTagActive.initView();

        //大类点击新增绑定事件
        $(".without-blog-class-big").click(function () {
            $.zdialog({
                html: "大类不允许新增，选择其他作为类别吗？",
                btn: [{
                    btnText: "选择其他",
                    btnFn: function () {
                        zq.blogClassSelectBigCompont.setResult(5);
                    }
                },
                    {
                        btnType: "cancel"
                    }
                ]
            }, "alert");
        });

        //保存小类绑定事件
        $(".save-blog-class").click(function () {
            let insert_tag = [];
            $(".active-tag").find("div.tagItem").each(function (index, element) {
                let $me = $(element);
                if ($me.data("id") == "") {
                    insert_tag.push($me.find("span").text());
                    zq.blogClassSmall.push({
                        label: $me.find("span").text(),
                        value: 123//先写死
                    })
                }
            })
            ////新增接口
            //zq.blogClassSmall.push()
            zq.blogClassSelectSmallCompont = _zqInit.initBlogClassSmall(zq.blogClassSmall);
        })

        //博客原创绑定事件
        $("#blog_yc").click(function () {
            $(".blog_copy_link_div").hide();
        })

        //博客转载绑定事件
        $("#blog_zz").click(function () {
            $(".blog_copy_link_div").show();
        })

        //是否转载
        $("#blog_yc").click(function () {
            zq.isBlogCreateYourSelf == "0";
        })
        $("#blog_zz").click(function () {
            zq.isBlogCreateYourSelf == "1";
        })

        //博客是否原创
        if (zq.isBlogCreateYourSelf == "0") {
            $("#blog_yc").click();
        } else {
            //转载的话赋初值
            $("#blog_copy_link").val(zq.blogCopyLink);
            $("#blog_zz").click();
        }

        //是否评论
        $("#blog_comment_yes").click(function () {
            zq.isBlogCommit=="0"
        })
        $("#blog_comment_no").click(function () {
            zq.isBlogCommit=="1"
        })

        //博客是否允许评论
        if (zq.isBlogCommit == "0") {
            $("#blog_comment_yes").click()
        } else {
            $("#blog_comment_no").click()
        }

        //博客标题复原
        $('#blog_title').click(function () {
            $(this).css('border', '1px solid #dee2e6').css('background-color', '#fff');
        });

        //博客大类选择下拉复原
        $('#blog_class_select_big').click(function () {
            $(this).css('border', '1px solid #dee2e6').css('background-color', '#fff');
        })
        //博客小类选择下拉复原
        $('#blog_class_select_small').click(function () {
            $(this).css('border', '1px solid #dee2e6').css('background-color', '#fff');
        })

        //博客页面3上一步
        $(".step3-2-2").click(function () {
            me.startSave2Redis();
            me.turnToBefore(3);
        });


        //新增/保存
        $(".insertBlog").click(function (){
            let blog_title = $("#blog_title").val().trim();
            if (blog_title.length == 0) {
                $("#blog_title").focus();
                $('#blog_title').css('border', '2px solid #dc3545').css('background-color', 'rgba(255,146,144,0.42)');
                $.zmsg({
                    html: "请填写博客标题"
                });
                return;
            }
            if (blog_title.length > 64) {
                $("#blog_title").val("");
                $("#blog_title").focus();
                $.zmsg({
                    html: "博客标题长度太长"
                })
                return;
            }
            if (zq.blogHeadImage == "") {
                $.zmsg({
                    html: "请选择上传博客封面图片"
                });
                return;
            }
            zq.blogTitle = blog_title;
            zq.blogDescription = $("#blog_description").val().trim();
            if (zq.blogClassBigSelected.length == 0) {
                $.zmsg({
                    html: "请选择大类"
                });
                return;
            }
            if (zq.blogClassSmallSelected.length == 0) {
                $.zmsg({
                    html: "请选择小类"
                });
                return;
            }
            //window.btoa(window.encodeURIComponent(str))
            //"Q2hpbmElRUYlQkMlOEMlRTQlQjglQUQlRTUlOUIlQkQ="
            //window.decodeURIComponent(window.atob('Q2hpbmElRUYlQkMlOEMlRTQlQjglQUQlRTUlOUIlQkQ='))
            //"China，中国"
            let blog=me.collectData();
            let param=JSON.stringify({
                "blog":blog
            });
            $.zCjax({
                url:ip+":"+blogPort+"/hoppinzq?method=insertBlog",
                type:"post",
                isRedirect:true,
                data:param,
                dataType:"json",
                //contentType: "application/json",
                beforeSend:function (){
                    $(".insertBlog").buttonLoading().buttonLoading('start');
                    $(".step3-2-2").buttonLoading().buttonLoading('start');
                    $(".preview-show-blog").buttonLoading().buttonLoading('start');
                },
                success:function (data) {
                    if(data.code==200){
                        $.zmsg({
                            html: "新增成功！"
                        });
                        setTimeout(function () {
                            $(".insertBlog").off("click").fadeOut(500).delay(500).buttonLoading('stop');
                            $(".step3-2-2").off("click").fadeOut(500).delay(500).buttonLoading('stop');
                            $(".preview-show-blog").off("click").fadeOut(500).delay(500).buttonLoading('stop');
                            $(".back-home").delay(500).fadeIn(500).on("click",function () {
                                window.location.href=ip+":"+blogPort;
                            });
                            $(".forward-blog").delay(500).fadeIn(500).on("click",function () {
                                window.location.href=ip+":"+blogPort+"/"+zq.blogId;
                            });
                        },1500);
                    }
                },
                error:function (){
                    alert("新增失败！请检查数据重新新增");
                    $(".insertBlog").buttonLoading('stop');
                    $(".step3-2-2").buttonLoading('stop');
                    $(".preview-show-blog").buttonLoading('stop');
                },
                complete:function (xhr,data) {

                }
            })
        })
    },

    /**
     * 收集数据，传入的数据将会替换收集的数据相同key的value
     * @param data
     */
    collectData:function(data){
        if(data===undefined||data==null||__zqBlog.elementType(data)!="object"){
            data={};
        }
        let bigClass=zq.blogClassBigSelected;
        let SmallClass=zq.blogClassSmallSelected;
        let _class=bigClass;
        $.each(SmallClass,function (index,classData){
            _class+=","+classData;
        })
        let _data={
            "title":$('#blog_title').val(),
            "description":$("#blog_description").val(),
            "build_type":zq.blogTypeCode,
            "csdn_link":$("#csdn_blog_link").val(),
            "text":window.btoa(window.encodeURIComponent(zq.blogText)),
            "star":zq.blogLevel,
            "is_comment":zq.isBlogCommit,
            "file":"____file",
            "_class":_class,
            "is_create_self":zq.isBlogCreateYourSelf,
            "music_file":"____music_file",
            "image":zq.blogHeadImage,
            "html":window.btoa(window.encodeURIComponent(zq.blogHtml)),
            "copy_link":$("#blog_copy_link").val()
        }
        return $.extend({},_data,data);

    },

    /**
     * 下一步,步骤
     * @param index
     */
    turnToNext: function (index) {
        let me = this;
        let step = ".step" + index;
        let next = ".step" + (index + 1);
        $(step).removeClass("show").removeClass("active");
        $(next).addClass("show").addClass("active");
        //解除步骤锁定
        $(".myaccount-tab-menu").find("a").eq(index).css("pointer-events", "all");
    },
    /**
     * 上一步,步骤
     * @param index
     */
    turnToBefore: function (index) {
        let me = this;
        let step = ".step" + index;
        let before = ".step" + (index - 1);
        $(step).removeClass("show").removeClass("active");
        $(before).addClass("show").addClass("active");
    },
    submit: function () {

    },

    /**
     * 初始化博客封面
     * */
    initBlogHeadImage: function () {
        $(".blog_head_image").hide();
        $(".blog_head_image_").show();
    },

    /**
     * 初始化博客大类下拉选择框
     * 大类只能选中一条
     * @param data 所有值
     * @param selected 选中默认值
     */
    initBlogClassBig: function (data, selected) {
        $("#blog_class_select_big").html("");
        let blogClassSelectBig = $("#blog_class_select_big").mySelect({
            mult: false,
            option: data,
            onChange: function (res) {
                zq.blogClassBigSelected = res;
                _zqLog(res);
            }
        });
        if (selected != undefined && selected.length > 0) {
            blogClassSelectBig.setResult(selected);
        }
        return blogClassSelectBig;
    },

    /**
     * 初始化博客小类下拉选择框
     * 小类可以选中多条
     * @param data 所有值
     * @param selected 选中默认值
     */
    initBlogClassSmall: function (data, selected) {
        $("#blog_class_select_small").html("");
        let blogClassSelectSmall = $("#blog_class_select_small").mySelect({
            mult: true,//true为多选,false为单选
            option: data,
            onChange: function (res) {
                zq.blogClassSmallSelected = res;
                _zqLog(res);
            }
        });
        if (selected != undefined && selected.length > 0) {
            blogClassSelectSmall.setResult(selected);
        }
        return blogClassSelectSmall;
    }
}

/**
 * 简单富文本拓展原型-清除默认
 * 仅仅针对简单富文本，他还是比较简单，如果你想拓展你的功能，仿照我的写就行
 * @constructor
 */
function ExtensionBtnX() {
    this.button = document.createElement('button');
    this.button.className = 'medium-editor-action';
    this.button.innerText = 'X';
    this.button.onclick = this.onClick.bind(this);
}

ExtensionBtnX.prototype.getButton = function () {
    return this.button;
};

ExtensionBtnX.prototype.onClick = function () {
    $(".simple_fwb_default").remove();
};

$(function () {

    $(".myaccount-tab-menu").find("a").each(function (index, element) {
        $(element).css("pointer-events", "none");
    })
    //初始化简单富文本编辑器
    _zqInit.initSimpleEditor();

    //隐藏两个富文本编辑器
    $("#container").hide();//已初始化
    $(".editable_fwb").hide();//未初始化
    $("#editable_markdown").hide();

    //为博客构建类型单选框添加点击事件
    //保存选中的博客构建类型
    //如果点击了csdn，则显示csdn的一个url输入框
    $(".blog_type_code_radio").off("click").on("click", function () {
        let blog_type = $(this).data("type");
        let blog_type_code = $(this).data("code");
        if (blog_type != "csdn") {
            $(".csdn_blog_link_div").hide();
        } else {
            $(".csdn_blog_link_div").show();
        }
        zq.blogTypeCode=blog_type_code;
        zq.blogType = blog_type;
    });
    _zqInit.initFileLoader();
    _zqInit.page1Init();
    _zqInit.page2Init();
    _zqInit.page3Init();

    $(".preloader").delay(1000).fadeOut(1000);
})