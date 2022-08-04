let ipconfig = __zqBlog.ipConfig;
let requestBlogIp = ipconfig.ip_ + ":" + ipconfig.blogPort;
$(function () {
    if(__zqBlog.isMobile){
        $(".silder-blog-extra").hide();
    }
    let url = window.location.href;
    if (url.substring(url.length - 1) == "#") {
        url = url.split("#")[0];
    }
    let blogId = url.substring(url.lastIndexOf("/") + 1);
    if(blogId.indexOf("?")!=-1){
        blogId=blogId.split("?")[0];
    }
    if(blogId.indexOf("#")!=-1){
        blogId=blogId.split("#")[0];
    }
    //移除该博客的稍后再看
    let blog_laters=localStorage.getItem("later:blog");
    if(blog_laters!=null){
        let blog_later_id="blog:id:"+blogId;
        localStorage.removeItem(blog_later_id);
        let blog_later_ids=blog_laters.split(",");
        blog_later_ids = blog_later_ids.filter(function(item) {
            return item != blogId
        });
        localStorage.setItem("later:blog",blog_later_ids);
    }
    let blogDetailUrl=`${requestBlogIp}/hoppinzq?method=queryBlog&params={"blogVo":{"id":"${blogId}","searchType":0,"blogDetail":1}}`;
    if(__zqBlog.user!=null&&__zqBlog.user.id==$("#author").val()){
        blogDetailUrl=`${requestBlogIp}/hoppinzq?method=queryBlog&params={"blogVo":{"id":"${blogId}","searchType":0,"blogDetail":1,"author":"${__zqBlog.user.id}","blogExtra":1}}`;
    }
    $.ajax({
        url: blogDetailUrl,
        timeout: 20000,
        complete: function () {

        },
        success: function (json) {
            let d_ = JSON.parse(json);
            if (d_.code == 200 && d_.data && d_.data.list && d_.data.list.length == 1) {
                let blog = d_.data.list[0];
                $("title").text(blog.title);
                $('meta[name="description"]').attr("content",blog.description);
                $('meta[name="keywords"]').attr("content",blog.title);
                $('meta[name="baidu-search"]').attr("content",`{"autorun":true,"install":true,"keyword":"${blog.title}"}`);
                $(".blog-details-content").prepend2(`<h3 class="blog-details-title">
                        ${blog.title}
                    </h3>
                    <div class="blog-details-meta" style="height: 50px;">
                        <div class="blog-top-bar-message" style="float: left; height: 100%;align-items: center;display: flex;">
                        作者：
                        <a href="${requestBlogIp}/author/${blog.user.id}" class="text-success blog-list-link">
                        ${blog.authorName}
                        </a> &nbsp;&nbsp;/&nbsp;&nbsp;
                            ${__zqBlog.getRealDate(blog.updateTime)}
                        </div>
                        <div class="widget-tags blog-class-all" style="float: right;">
                            <a class="widget-tag-link widget-tag-bq" href="JavaScript:void(0);">标签:</a>
                        </div>
                    </div>
                    
                    <div class="blog-qutation">
                        <p>
                             博客描述：${blog.description}
                        </p>
                    </div>
                    <div style="border: 0px solid #45ab49;padding: 15px;">
                        <div class="blog-text">
                            ${blog.html}
                        </div> 
                    </div> `,function () {
                    if(blog.id=="342754217120808960"){
                        initScript();
                    }
                    $(".blog-text").children("p").each(function (index_p,element_p) {
                        $(element_p).attr("id","blog_text_list_"+index_p);
                        $(".blog-ml").append(`<li><a href="#blog_text_list_${index_p}">${$(element_p).text().substring(0,10)+"..."}</a></li>`)
                        if(index_p==$(".blog-text").children("p").length-1){
                            //页内搜索
                            $('#web_inner_search').fullsearch({
                                highlight: true,
                                search_data: ".search-result",
                                search_title: ".result-section",
                                search_content: ".result-content",
                                list: ".blog-text",
                                nodata: "未找到相关数据",
                            });
                        }
                    });
                    //图片处理
                    //1、点击放大
                    //2、关于微信公众号部分图片处理data-src
                    $(".blog-text").find("img").each(function (index_img,element_img){
                        let $element_img=$(element_img);
                        $(this).wrap($(`<a href="${$element_img.attr("src")||$element_img.data("src")}" data-lightbox="example-set" title="点击x关闭"></a>`).on("click",function () {

                            // alert($element_img.height());
                            // setTimeout(function () {
                            //     alert($(".lb-outerContainer").height())
                            // },500)
                            $(".lb-outerContainer").width($element_img.width()*2).height($element_img.height());
                            $("#lightbox").css("position","fixed");
                            $("#lightbox").css("top","400px !important");//先写死
                        }))
                        if($element_img.data("src")&&$element_img.attr("src")==undefined){
                            $element_img.attr("src",$element_img.data("src"));
                        }
                    })
                    __zqBlog.stopLoading(0,500);
                });
                //字号略微调高
                $("body").css("font-size","1rem")
                //代码高光
                hljs.initHighlightingOnLoad();

                //遍历所有code标签，给由三行以上的代码片段添加一个按钮用以切换主题，鼠标移入显示，移除隐藏，阻止移入移出时的冒泡
                $("code").each(function (index_code,element_code){
                    let $element_code=$(element_code);
                    if($element_code.find("span").length>3){
                        let $code=$element_code.on({
                            mouseover : function(e1){
                                e1.stopPropagation();
                            } ,
                            mouseout : function(e2){
                                e2.stopPropagation();
                            }
                        }).prepend($(`<span class="code_change cursor-pointer">切换主题</span>`).off("click").on("click",function () {
                            //如果想一个按钮控制一个代码片段主题切换，不用修改
                            //如果想一个按钮控制所有代码片段主题切换，去掉上面的off("click"),即点击就触发所有的主题切换
                            $code.toggleClass("hljs");
                        }))
                    }
                })
                let blogClassId = blog.blogClass;
                let blogClassName = blog.blogClassName;
                let classReg = /[| ||]+/g;
                blogClassId = blogClassId.split(classReg);
                blogClassName = blogClassName.split(classReg);
                if (blogClassId.length > 0 && blogClassName.length > 0) {
                    $.each(blogClassId, function (index, data) {
                        if (blogClassName[index] != undefined) {
                            $(".widget-tag-bq").after(`<a class="widget-tag-link" data-id="${data}" href="${ipconfig.ip_ + ":" + ipconfig.blogPort+"/bloglist.html?s=&c="+data+"&cn="+blogClassName[index]}">${blogClassName[index]}</a>`);
                        }
                    })
                }

                let blogFile=blog.fileFj;
                if(blogFile&&blogFile!=null){
                    $(".blog-text").after(`<div class="blog-details-grid">
                        <div class="row mb-n7">
                            <div class="col-12 mb-7"> 
                                <p>
                                     附件(点击下载)：    <span>${__zqBlog.fileSizeFormat(blogFile.file_volume)}</span> 
                                </p>
                                <a href="${__zqBlog.ipConfig.fileServer_+"/baseFile/downloadFile/"+blogFile.file_id}">${blogFile.file_name}${blogFile.file_type}</a>
                            </div>
                        </div>
                    </div>`)
                }

                if(blog.user&&blog.user!=null){
                    if(__zqBlog.user!=null&&blog.user.id==__zqBlog.user.id){
                        $(".blog-top-bar-message").append(`<a id="blog_detail_update" href="${requestBlogIp}/writeblog.html?id=${blog.id}">修改</a><a id="blog_detail_delete">删除</a>`)
                        $('#blog_detail_delete').on("click",function () {
                            let checkDelete=confirm("确认删除？")
                            if (checkDelete){
                                $.ajax({
                                    url:`${requestBlogIp}/hoppinzq?method=deleteBlog&params={"id":"${blog.id}"}`,
                                    success:function (data) {
                                        alert("删除成功");
                                        window.location.href=`${requestBlogIp}`;
                                    },
                                    error:function () {
                                        alert("删除失败");
                                    }
                                })
                            }
                        })
                    }
                    if(!__zqBlog.isMobile){
                        $(".blog-detail-author").append(`<div class="entry-meta user-blog-left-swiper">
                            <div class="entry-author entry-author_style-1">
                                <a class="entry-author__avatar" href="${requestBlogIp}/author/${blog.user.id}" title="进入${blog.user.username}的博客空间"
                                   rel="author">
                                   ${__zqBlog.loadImage(blog.user.image,"",blog.user.username+"头像",__zqBlog.ipConfig.errorImagePath)} 
                                </a>
                                <div class="entry-author__text">
                                    <a class="entry-author__name" title="进入${blog.user.username}的博客空间" rel="author"
                                       href="${requestBlogIp}/author/${blog.user.id}">${blog.authorName}</a>
                                    <a class="entry-author__description">${blog.user.description}</a>
                                </div>
                            </div>
                        </div> `);
                        $(".user-blog-left-swiper").append2(`<div class="socials-share-box">
                            <ul class="social-list">
                                <li class="qrcode-share">
                                    <a class="sharing-btn sharing-btn-primary facebook-btn facebook-theme-bg-hover"
                                       data-placement="top" title="${blog.user.username}的二维码" href="javaScript:void(0)">
                                        <div id="author_qrcode" style="width: 256px;height: 256px;display: none"></div>
                                        <div class="share-item__icon">
                                            <i class="las la-qrcode"></i>
                                        </div>
                                    </a>
                                </li>
                                <li class="mail-share">
                                    <a class="sharing-btn sharing-btn-primary twitter-btn twitter-theme-bg-hover"
                                       data-placement="top" title="${blog.user.username}的邮箱" href="javaScript:void(0)">
                                        <div class="share-item__icon">
                                            <i class="las la-mail-bulk"></i>
                                        </div>
                                    </a>
                                </li>
                                <li class="link-share">
                                    <a class="sharing-btn pinterest-btn pinterest-theme-bg-hover" data-placement="top"
                                       title="${blog.user.username}的首页链接" href="javaScript:void(0)">
                                        <div class="share-item__icon">
                                            <i class="las la-link"></i>
                                        </div>
                                    </a>
                                </li>
                                <li class="other-blog-share" style="width: 200px;max-height: 400px;margin-top: 10px">
                                    <div class="widget-list">
                                        <ul class="list-group list-group-flush list-auth-class-count"></ul>
                                    </div>
                                </li>
                            </ul>
                        </div>`,function () {
                            let qrcode = new QRCode(document.getElementById("author_qrcode"), {
                                width : 256,
                                height : 256
                            });
                            qrcode.makeCode(`${requestBlogIp}/author/${blog.user.id}`);
                            __zqBlog.zinterval(function (qrcode_time_img_id) {
                                let qrcode_image_base64=$("#author_qrcode").find("img").attr("src");
                                if(qrcode_image_base64!=undefined){
                                    new jBox('Tooltip', {
                                        attach: '.qrcode-share',
                                        theme: 'TooltipBorderThick',
                                        width: 256,
                                        height : 256,
                                        closeOnMouseleave: true,
                                        position: {
                                            x: 'right',
                                            y: 'center'
                                        },
                                        outside: 'x',
                                        pointer: 'top:15',
                                        content: "<img src='"+qrcode_image_base64+"'>",
                                        animation: 'move'
                                    });
                                    clearInterval(qrcode_time_img_id);
                                }
                            },200);
                            new jBox('Tooltip', {
                                attach: '.mail-share',
                                theme: 'TooltipBorderThick',
                                closeOnMouseleave: true,
                                position: {
                                    x: 'right',
                                    y: 'center'
                                },
                                outside: 'x',
                                pointer: 'top:15',
                                content: `${blog.user.email||"邮箱地址被作者隐藏了！"}`,
                                animation: 'move'
                            });
                            new jBox('Tooltip', {
                                attach: '.link-share',
                                theme: 'TooltipBorderThick',
                                closeOnMouseleave: true,
                                position: {
                                    x: 'right',
                                    y: 'center'
                                },
                                outside: 'x',
                                pointer: 'top:15',
                                content: `我的首页:<a href="${requestBlogIp}/author/${blog.user.id}">${requestBlogIp}/author/${blog.user.id}</a>`,
                                animation: 'move'
                            });
                            $.ajax({
                                url:`${requestBlogIp}/hoppinzq?method=queryUserClassCount&params={"auth_id":"${blog.user.id}","limit":6}`,
                                success:function (data){
                                    let json=JSON.parse(data);
                                    if (json.code==200){
                                        let blogClasses=json.data;
                                        $.each(blogClasses,function (index,data) {
                                            if(data.class_count){
                                                $(".list-auth-class-count").append(`<li class="list-group-item d-flex justify-content-between align-items-center">
                                                ${data.name}<span>${data.class_count}</span>
                                            </li>`)
                                            }
                                        })
                                    }
                                }
                            })
                        })
                    }
                }else{
                    $(".blog-detail-author").append(`<div class="entry-meta user-blog-left-swiper">
                            <div class="entry-author entry-author_style-1">
                                <a class="entry-author__avatar" href="javaScript:void(0)" rel="author">
                                   <i class="las la-user-circle"></i>
                                </a>
                                <div class="entry-author__text">
                                    <a class="entry-author__name" rel="author"
                                       href="javaScript:void(0)">该用户已注销</a>
                                </div>
                            </div>
                        </div> `);
                }

                //跟随滚动
                let $stickySidebar = $('.silder-blog-extra');
                let $stickyHeader = $('.header-top-bar');
                let marginTop = ($stickyHeader.length) ? ($stickyHeader.outerHeight() + 20) : 0;
                if ($.isFunction($.fn.theiaStickySidebar)) {
                    $stickySidebar.theiaStickySidebar({
                        additionalMarginTop: marginTop,
                        additionalMarginBottom: 20,
                    });
                }

                //当前登录人评论头像
                if(__zqBlog.user==null){
                    $(".write-blog-user-img").append("<a>点击登录</a>");
                    $("#btn_comment").click(function () {
                        //alert("请先登录，方可评论")
                    })
                }else{
                    $(".write-blog-user-img").append(__zqBlog.loadImage(__zqBlog.user.image,"","",__zqBlog.ipConfig.errorImagePath));
                }
                $("#btn_comment").click(function () {
                    let comment_text=$("#comment_text").val();
                    if(comment_text.trim().length==0){
                        alert("请输入评论内容");
                        $("#comment_text").focus();
                        return;
                    }
                    $.zBCjax({
                        url:`${requestBlogIp}/hoppinzq?method=commentBlog`,
                        type:"post",
                        isRedirect:true,
                        data:JSON.stringify({
                            "comment":{
                                "author":__zqBlog.user==null?0:__zqBlog.user.id,
                                "comment":comment_text,
                                "blogId":blog.id
                            }
                        }),
                        dataType:"json",
                        beforeSend:function () {
                            $("#btn_comment").buttonLoading().buttonLoading('start');
                        },
                        //contentType: "application/json",
                        success:function (json) {
                            console.log(json)
                            if(json.code==200){
                                alert("评论成功！");
                                $("#comment_text").val("");
                                refreshComment(json.data);
                            }else{
                                alert("评论失败")
                            }
                        },error:function () {
                            alert("评论失败")
                        },complete:function () {
                            $("#btn_comment").buttonLoading('stop');
                        }
                    })
                })
                if(blog.isComment==0){
                    if(blog.blogComment&&blog.blogComment!=null){
                        refreshComment(blog.blogComment);
                    }
                }else{
                    $(".comments").css("pointer-events","none")
                        .css("opacity",".2").after($(`<h1 class="alert-danger" style="cursor: not-allowed;text-align: center;">评论功能已被作者禁用！</h1>`));
                }
                //分享
                $(".share-blog").click(function () {
                    let target = url;
                    let save = function (e){
                        e.clipboardData.setData('text/plain',target);
                        e.preventDefault();//阻止默认行为
                    }
                    document.addEventListener('copy',save);
                    document.execCommand("copy");
                    alert("OK，已复制链接到粘贴板，微信目前不提供个人授权。")
                })
                //加载额外信息，点赞收藏之类的
                if(blog.blogExtra!=undefined){
                    let blogExtra=blog.blogExtra;
                    if(blogExtra.setType!=1){
                        $("#blog_is_like").text("取消喜欢和收藏");
                    }else{
                        $("#blog_is_like").text("喜欢并收藏")
                    }
                    $("#blog_is_like").click(function () {
                        alert("暂未实现");
                        //喜欢收藏功能，
                    });
                }
            } else {
                __zqBlog.stopLoading(0,1000);
                //找不到博客
                 $(".blog-detail-t").html(`<h1>哦不，没有博客被找到，你可以选择 </h1><ul><li><a href='${requestBlogIp}' target="_self">返回首页</a></li><li><a href='${requestBlogIp}/writeblog.html' target="_self">编写博客</a></li></ul>`);
            }
            $(".social-links").find(".social-link").remove();
        },
        error: function (a, b) {
            __zqBlog.stopLoading(0,1000);
        }
    });

    /**
     * 最受欢迎的4条博客
     */
    $.ajax({
        url: ipconfig.ip_ + ":" + ipconfig.blogPort+`/hoppinzq?method=getHotBlog&params={"blogVo":{"pageIndex":1,"pageSize":4,"searchType":0,"blogReturn":1,"order":4}}`,
        success: function (json) {
            let data=JSON.parse(json);
            if(data.code==200){
                let blogs=data.data.list;
                $.each(blogs,function (index,blog) {
                    if(blog.id!=blogId){
                        $(".popular-blog").append(`<div class="widget-post-list">
                                <a href="${ipconfig.ip_ + ":" + ipconfig.blogPort+"/blog/"+blog.id}" class="post-thumb">
                                    ${__zqBlog.loadImage(__zqBlog.ipConfig.fileProxyServer_+"/"+blog.image,"","image_not_found",__zqBlog.ipConfig.errorImagePath,{
                            "width":"70px",
                            "height":"70px",
                            "object-fit":"cover"
                        })} 
                                </a>
                                <div class="widget-post-content">
                                    <h3 class="widget-sub-title">
                                        <a href="${ipconfig.ip_ + ":" + ipconfig.blogPort+"/blog/"+blog.id}">${blog.title}</a>
                                    </h3>
                                    <p class="post-meta">${__zqBlog.getRealDate(blog.updateTime)}</p>
                                </div>
                            </div>`);
                    }
                })
            }
        }
    });

    $.ajax({
        url: ipconfig.ip_ + ":" + ipconfig.blogPort+`/hoppinzq?method=getHotBlogClass&params={"limit":10}`,
        success: function (json) {
            let data=JSON.parse(json);
            if(data.code==200){
                let blogClasses=data.data;
                $.each(blogClasses,function (index,blogClass) {
                    $(".hot-blog-class").append(`<a class="widget-tag-link" href="${ipconfig.ip_ + ":" + ipconfig.blogPort+"/bloglist.html?s=&c="+blogClass.id+"&cn="+blogClass.name}">${blogClass.name}</a>`)
                })
            }
        }
    })

})

function refreshComment(comments){
    $(".blog-comment-title").text(`评论(${comments.length})`);
    $(".blog_detail_comments").html("");
    $.each(comments,function (index_c,data_c) {
        if(data_c.user){
            $(".blog_detail_comments").append2($(`<div class="author-list d-flex flex-wrap" id="blog-comment-id-${data_c.id}" style="margin-bottom: 5px;">
                                   ${__zqBlog.loadImage(data_c.user.image,"author-profile align-self-start",data_c.user.username+"头像",__zqBlog.ipConfig.errorImagePath,{
                width:84,height:84
            })} 
                            <div class="author-info">
                                <h3 class="author-title">${data_c.user.username}</h3>
                                <div class="author-meta">
                                    <span class="blog-comment-date">${__zqBlog.getRealDate(data_c.date)}</span>
                                    ${data_c.isUserLike==0?'<i class=\"lar la-heart cursor-pointer blog-comment-like\" style="font-size:20px;"></i>':'<i class=\"las la-heart cursor-pointer blog-comment-like\" style="font-size:20px;color: #cc3c3c"></i>'}
                                    <span class='blog-comment-like cursor-pointer' data-id="${data_c.id}">${data_c.commentLike}</span>
                                </div>
                                <p>
                                    ${data_c.comment}
                                </p>
                                <p class="replay">
                                    ${(__zqBlog.user!=null&&data_c.user.id==__zqBlog.user.id)?"<a href='javaScript:void(0)' data-id="+data_c.id+"><i class='fa fa-reply'></i> 编辑</a>":""}
                                    ${(__zqBlog.user!=null&&data_c.user.id==__zqBlog.user.id)?"<a href='javaScript:void(0)' class='delete-blog-comment' data-id="+data_c.id+"><i class='fa fa-reply'></i> 删除</a>":""}
                                    <a href="javaScript:void(0)" data-id="${data_c.id}"><i class="fa fa-reply"></i> 收起</a>
                                    <a href="javaScript:void(0)" data-id="${data_c.id}"><i class="fa fa-reply"></i> 回复</a>
                                </p>
                            </div>
                        </div>`).addClass((data_c.pid==0)?"":"author-list-replay"),function () {
                $(".delete-blog-comment").off("click").on("click",function () {//博客评论删除,只有自己
                    let commentId=$(this).data("id");
                    $.ajax({
                        url:ipconfig.ip_ + ":" + ipconfig.blogPort+"/hoppinzq?method=commentDelete&params={'comment_id':'"+commentId+"'}",
                        success:function (data) {
                            let json=JSON.parse(data)
                            if(json.code==200){
                                alert("删除评论成功！");
                                $("#blog-comment-id-"+commentId).remove();
                            }else{
                                alert("删除评论失败！")
                            }
                        },
                        error:function () {
                            alert("删除评论失败！")
                        }
                    })
                })
            });
        }else{

        }
    })
}

function initScript(){
    //动态装配dom
    $("body").append2(`<a id="scrollUp" class="prohibitScript" title="阻止脚本" style="cursor:pointer;position: fixed; z-index: 214;right: 100px"><i class="las la-eye-slash"></i></a>
<canvas class="webgl blog_sc"></canvas>
<button id="play-music" class="blog_sc" type="button" aria-label="Play music"><svg id="music-svg" fill="currentColor"
                                                                   viewBox="0 0 512 512" width="100" title="music">
    <path
            d="M470.38 1.51L150.41 96A32 32 0 0 0 128 126.51v261.41A139 139 0 0 0 96 384c-53 0-96 28.66-96 64s43 64 96 64 96-28.66 96-64V214.32l256-75v184.61a138.4 138.4 0 0 0-32-3.93c-53 0-96 28.66-96 64s43 64 96 64 96-28.65 96-64V32a32 32 0 0 0-41.62-30.49z" />
</svg>
</button>`,function () {
        //dom装配完成回调
        $(".prohibitScript").off("click").on("click",function () {
            let $i=$(this).find("i");
            $(".blog_sc").toggle();
            $i.toggleClass("la-eye").toggleClass("la-eye-slash");
        })
        //动态装配CSS
        let thisCss = [];
        thisCss.push('.webgl {position: absolute;width: 100vw;height: 100vh; z-index: -2;top: 0;left: 0;outline: none;height: 5929px;}');
        thisCss.push('#music-svg {width: 3.5vh;}');
        thisCss.push(
            '#play-music {position: fixed;left: 0;top: 0;bottom: 0;height: 12vh;width: 12vh;transform: translateY(2vh);right: 0;margin: auto;background: transparent;color: inherit; border: none;cursor: pointer;}'
        );
        let thisStyle = document.createElement('style');
        thisStyle.innerHTML = thisCss.join('\n'),
            document.getElementsByTagName('head')[0].appendChild(thisStyle);
        //动态装配JS
        let script = document.createElement("script");
        script.type="module";
        script.src = `${requestBlogIp}/static/js/script/detailblog/canvas.js`;
        document.getElementsByTagName("head").item(0).appendChild(script);
    });
}