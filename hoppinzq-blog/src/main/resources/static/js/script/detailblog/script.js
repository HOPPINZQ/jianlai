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
            $(".preloader").delay(1000).fadeOut(1000);
        },
        success: function (json) {
            let d_ = JSON.parse(json);
            if (d_.code == 200 && d_.data && d_.data.list && d_.data.list.length == 1) {
                let blog = d_.data.list[0];
                $("title").text(blog.title);
                $('meta[name="description"]').attr("content",blog.description);
                $('meta[name="keywords"]').attr("content",blog.title);
                $('meta[name="baidu-search"]').attr("content",`{"autorun":true,"install":true,"keyword":"${blog.title}"}`);
                $(".blog-details-content").prepend(`<h3 class="blog-details-title">
                        ${blog.title}
                    </h3>
                    <div class="blog-details-meta" style="height: 50px;">
                        <div class="blog-top-bar-message" style="float: left; height: 100%;align-items: center;display: flex;">
                        作者 by &nbsp;&nbsp;
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
                    </div> `);
                //字号略微调高
                $("body").css("font-size","1rem")
                //代码高光
                hljs.initHighlightingOnLoad();

                let $code=$("code").on({
                    mouseover : function(e1){
                        e1.stopPropagation();
                    } ,
                    mouseout : function(e2){
                        e2.stopPropagation();
                    }
                }).prepend($(`<span class="code_change cursor-pointer">切换主题</span>`).on("click",function () {
                    $code.toggleClass("hljs");
                }))

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
                                     附件(点击下载)：       
                                </p>
                                <a href="${__zqBlog.ipConfig.fileServer_+"/baseFile/downloadFile/"+blogFile.file_id}">${blogFile.file_name}${blogFile.file_type}</a>
                            </div>
                        </div>
                    </div>`)
                }

                if(blog.user&&blog.user!=null){
                    if(__zqBlog.user!=null&&blog.user.id==__zqBlog.user.id){
                        $(".blog-top-bar-message").append(`<a id="blog_detail_update" href="http://127.0.0.1:8809/writeblog.html?id=${blog.id}">修改博客</a><a id="blog_detail_delete">删除博客</a>`)
                        $('#blog_detail_delete').on("click",function () {
                            alert("确认删除？");
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
                    $(".user-blog-left-swiper").append(`<div class="socials-share-box">
                            <ul class="social-list">
                                <li class="facebook-share">
                                    <a class="sharing-btn sharing-btn-primary facebook-btn facebook-theme-bg-hover"
                                       data-placement="top" title="Share on Facebook" href="#">
                                        <div class="share-item__icon">
                                            <svg fill="#888" preserveAspectRatio="xMidYMid meet" height="1.3em"
                                                 width="1.3em" viewBox="0 0 40 40">
                                                <g>
                                                    <path d="m21.7 16.7h5v5h-5v11.6h-5v-11.6h-5v-5h5v-2.1c0-2 0.6-4.5 1.8-5.9 1.3-1.3 2.8-2 4.7-2h3.5v5h-3.5c-0.9 0-1.5 0.6-1.5 1.5v3.5z"></path>
                                                </g>
                                            </svg>
                                        </div>
                                    </a>
                                </li>
                                <li class="twitter-share">
                                    <a class="sharing-btn sharing-btn-primary twitter-btn twitter-theme-bg-hover"
                                       data-placement="top" title="Share on Twitter" href="#">
                                        <div class="share-item__icon">
                                            <svg fill="#888" preserveAspectRatio="xMidYMid meet" height="1.3em"
                                                 width="1.3em" viewBox="0 0 40 40">
                                                <g>
                                                    <path d="m31.5 11.7c1.3-0.8 2.2-2 2.7-3.4-1.4 0.7-2.7 1.2-4 1.4-1.1-1.2-2.6-1.9-4.4-1.9-1.7 0-3.2 0.6-4.4 1.8-1.2 1.2-1.8 2.7-1.8 4.4 0 0.5 0.1 0.9 0.2 1.3-5.1-0.1-9.4-2.3-12.7-6.4-0.6 1-0.9 2.1-0.9 3.1 0 2.2 1 3.9 2.8 5.2-1.1-0.1-2-0.4-2.8-0.8 0 1.5 0.5 2.8 1.4 4 0.9 1.1 2.1 1.8 3.5 2.1-0.5 0.1-1 0.2-1.6 0.2-0.5 0-0.9 0-1.1-0.1 0.4 1.2 1.1 2.3 2.1 3 1.1 0.8 2.3 1.2 3.6 1.3-2.2 1.7-4.7 2.6-7.6 2.6-0.7 0-1.2 0-1.5-0.1 2.8 1.9 6 2.8 9.5 2.8 3.5 0 6.7-0.9 9.4-2.7 2.8-1.8 4.8-4.1 6.1-6.7 1.3-2.6 1.9-5.3 1.9-8.1v-0.8c1.3-0.9 2.3-2 3.1-3.2-1.1 0.5-2.3 0.8-3.5 1z"></path>
                                                </g>

                                            </svg>
                                        </div>
                                    </a>
                                </li>
                                <li class="pinterest-share">
                                    <a class="sharing-btn pinterest-btn pinterest-theme-bg-hover" data-placement="top"
                                       title="Share on Pinterest" href="#">
                                        <div class="share-item__icon">
                                            <svg fill="#888" preserveAspectRatio="xMidYMid meet" height="1.3em"
                                                 width="1.3em" viewBox="0 0 40 40">
                                                <g>
                                                    <path d="m37.3 20q0 4.7-2.3 8.6t-6.3 6.2-8.6 2.3q-2.4 0-4.8-0.7 1.3-2 1.7-3.6 0.2-0.8 1.2-4.7 0.5 0.8 1.7 1.5t2.5 0.6q2.7 0 4.8-1.5t3.3-4.2 1.2-6.1q0-2.5-1.4-4.7t-3.8-3.7-5.7-1.4q-2.4 0-4.4 0.7t-3.4 1.7-2.5 2.4-1.5 2.9-0.4 3q0 2.4 0.8 4.1t2.7 2.5q0.6 0.3 0.8-0.5 0.1-0.1 0.2-0.6t0.2-0.7q0.1-0.5-0.3-1-1.1-1.3-1.1-3.3 0-3.4 2.3-5.8t6.1-2.5q3.4 0 5.3 1.9t1.9 4.7q0 3.8-1.6 6.5t-3.9 2.6q-1.3 0-2.2-0.9t-0.5-2.4q0.2-0.8 0.6-2.1t0.7-2.3 0.2-1.6q0-1.2-0.6-1.9t-1.7-0.7q-1.4 0-2.3 1.2t-1 3.2q0 1.6 0.6 2.7l-2.2 9.4q-0.4 1.5-0.3 3.9-4.6-2-7.5-6.3t-2.8-9.4q0-4.7 2.3-8.6t6.2-6.2 8.6-2.3 8.6 2.3 6.3 6.2 2.3 8.6z"></path>
                                                </g>
                                            </svg>
                                        </div>
                                    </a>
                                </li>
                                <li class="linkedin-share">
                                    <a class="sharing-btn linkedin-btn linkedin-theme-bg-hover" data-placement="top"
                                       title="Share on Linkedin" href="#">
                                        <div class="share-item__icon">
                                            <svg fill="#888" preserveAspectRatio="xMidYMid meet" height="1.3em"
                                                 width="1.3em" viewBox="0 0 40 40">
                                                <g>
                                                    <path d="m13.3 31.7h-5v-16.7h5v16.7z m18.4 0h-5v-8.9c0-2.4-0.9-3.5-2.5-3.5-1.3 0-2.1 0.6-2.5 1.9v10.5h-5s0-15 0-16.7h3.9l0.3 3.3h0.1c1-1.6 2.7-2.8 4.9-2.8 1.7 0 3.1 0.5 4.2 1.7 1 1.2 1.6 2.8 1.6 5.1v9.4z m-18.3-20.9c0 1.4-1.1 2.5-2.6 2.5s-2.5-1.1-2.5-2.5 1.1-2.5 2.5-2.5 2.6 1.2 2.6 2.5z"></path>
                                                </g>
                                            </svg>
                                        </div>
                                    </a>
                                </li>
                            </ul>
                        </div>`)
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
                 $(".blog-detail-t").html("<h1>哦不，没有博客被找到（待添加样式）</h1>");

            }
            $(".social-links").find(".social-link").remove();
        },
        error: function (a, b) {

        }
    })

})