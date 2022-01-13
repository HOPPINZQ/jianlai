$(function () {
    setTimeout(function () {

    }, 250);
    if(__zqBlog.isMobile){
        $(".silder-blog-extra").hide();
    }
    let ipconfig = __zqBlog.ipConfig;
    let requestBlogIp = ipconfig.ip_ + ":" + ipconfig.blogPort;
    let url = window.location.href;
    if (url.substring(url.length - 1) == "#") {
        url = url.split("#")[0];
    }
    let blogId = url.substring(url.lastIndexOf("/") + 1);
    $.ajax({
        url: `${requestBlogIp}/hoppinzq?method=queryBlog&params={"blogVo":{"id":"${blogId}","searchType":0,"blogDetail":1}}`,
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
                        <a href="/${blog.user.id}" class="text-success blog-list-link">
                        ${blog.authorName}
                        </a> &nbsp;&nbsp;/&nbsp;&nbsp;
                            ${__zqBlog.getRealDate(blog.updateTime)}
                        </div>
                        <div class="widget-tags blog-class-all" style="float: right;">
                            <a class="widget-tag-link widget-tag-bq" href="JavaScript:Void(0);">标签:</a>
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
                    //关于微信公众号部分图片处理
                    $(".blog-text").find("img").each(function (index_img,element_img){
                        let $element_img=$(element_img);
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
                        }).prepend($(`<span class="code_change cursor-pointer">切换主题</span>`).on("click",function () {
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
                            $(".widget-tag-bq").after(`<a class="widget-tag-link" data-id="${data}" href="JavaScript:Void(0);">${blogClassName[index]}</a>`);
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
                        $(".blog-top-bar-message").append(`<a id="blog_detail_update" href="http://127.0.0.1:8809/writeblog.html?id=${blog.id}">修改</a><a id="blog_detail_delete">删除</a>`)
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
                    $(".blog-detail-author").append(`<div class="entry-meta user-blog-left-swiper">
                            <div class="entry-author entry-author_style-1">
                                <a class="entry-author__avatar" href="author.html" title="进入${blog.user.username}的博客空间"
                                   rel="author">
                                   ${__zqBlog.loadImage(blog.user.image,"",blog.user.username+"头像",__zqBlog.ipConfig.errorImagePath)} 
                                </a>
                                <div class="entry-author__text">
                                    <a class="entry-author__name" title="进入${blog.user.username}的博客空间" rel="author"
                                       href="author.html">${blog.authorName}</a>
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
                                        <ul class="list-group list-group-flush">
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                Java虚拟机<span>120</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                JavaAgent<span>120</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                July, 2021<span>120</span>
                                            </li>
                                        </ul>
                                    </div>
                                </li>
                            </ul>
                        </div>`,function () {
                        let qrcode = new QRCode(document.getElementById("author_qrcode"), {
                            width : 256,
                            height : 256
                        });
                        qrcode.makeCode(`${requestBlogIp}/author/${blog.user.id}`);
                        let qrcode_time_img_id=__zqBlog.zinterval(function (qrcode_time_img_id) {
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
                    })
                }else{
                    $(".blog-detail-author").append(`<div class="entry-meta user-blog-left-swiper">
                            <div class="entry-author entry-author_style-1">
                                <a class="entry-author__avatar" href="author.html" rel="author">
                                   <i class="las la-user-circle"></i>
                                </a>
                                <div class="entry-author__text">
                                    <a class="entry-author__name" rel="author"
                                       href="author.html">该用户已注销</a>
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
                }else{
                    $(".write-blog-user-img").append(__zqBlog.loadImage(__zqBlog.user.image,"","",__zqBlog.ipConfig.errorImagePath));
                }

                if(blog.blogComment&&blog.blogComment!=null){
                    let comments=blog.blogComment;
                    $(".blog-comment-title").text(`评论(${comments.length})`);
                    $.each(comments,function (index_c,data_c) {
                        if(data_c.user){
                            $(".blog_detail_comments").append($(`<div class="author-list d-flex flex-wrap">
                                   ${__zqBlog.loadImage(data_c.user.image,"author-profile align-self-start",data_c.user.username+"头像",__zqBlog.ipConfig.errorImagePath,{
                                width:84,height:84
                            })} 
                            <div class="author-info">
                                <h3 class="author-title">${data_c.user.username}</h3>
                                <div class="author-meta">
                                    <span class="blog-comment-date">${__zqBlog.getRealDate(data_c.date)}</span>
                                    ${data_c.isUserLike==0?'<i class=\"lar la-heart cursor-pointer blog-comment-like\" style="font-size:20px;"></i>':'<i class=\"las la-heart cursor-pointer blog-comment-like\" style="font-size:20px;color: #cc3c3c"></i>'}
                                    <span class='blog-comment-like cursor-pointer' data-id="${data_c.id}">${data_c.like}</span>
                                </div>
                                
                                <p>
                                    ${data_c.comment}
                                </p>
                                <p class="replay">
                                    <a href="#" data-id="${data_c.id}"><i class="fa fa-reply"></i> 收起</a>
                                    <a href="#" data-id="${data_c.id}"><i class="fa fa-reply"></i> 回复</a>
                                </p>
                            </div>
                        </div>`).addClass((data_c.pid==0)?"":"author-list-replay"));
                        }else{

                        }
                    })
                }
            } else {
                //找不到博客
                 $(".blog-detail-t").html(`<h1>哦不，没有博客被找到（待添加样式）</h1><a href='${requestBlogIp}' target="_self">返回首页</a>`);
            }
            $(".social-links").find(".social-link").remove();
        },
        error: function (a, b) {

        }
    })

})