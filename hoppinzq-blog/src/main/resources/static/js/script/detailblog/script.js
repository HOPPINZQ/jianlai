$(function () {
    setTimeout(function () {
        var $stickySidebar = $('.silder-blog-extra');
        var $stickyHeader = $('.header-top-bar');
        var marginTop = ($stickyHeader.length) ? ($stickyHeader.outerHeight() + 20) : 0;
        if ($.isFunction($.fn.theiaStickySidebar)) {
            $stickySidebar.theiaStickySidebar({
                additionalMarginTop: marginTop,
                additionalMarginBottom: 20,
            });
        }
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
        url: `${requestBlogIp}/hoppinzq?method=queryBlog&params={"blogVo":{"id":"${blogId}","searchType":0}}`,
        timeout: 20000,
        complete: function () {
            $(".preloader").delay(1000).fadeOut(1000);
        },
        success: function (json) {
            let d_ = JSON.parse(json);
            if (d_.code == 200 && d_.data && d_.data.list && d_.data.list.length == 1) {
                let blog = d_.data.list[0];
                $(".blog-details-content").prepend(`<h3 class="blog-details-title">
                        ${blog.title}
                    </h3>
                    <div class="blog-details-meta" style="height: 50px;">
                        <div style="float: left; height: 100%;align-items: center;display: flex;">
                        作者 by
                        <a href="/${blog.author}" class="text-success blog-list-link">
                        ${blog.author}
                        </a> / ${__zqBlog.getRealDate(blog.updateTime)}
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
                    <div>
                        ${blog.html}
                    </div>`);
                let blogClassId = blog.blogClass;
                let blogClassName = blog.blogClassName;
                let classReg = /[| ||]+/g;
                blogClassId = blogClassId.split(classReg);
                blogClassName = blogClassName.split(classReg);
                if (blogClassId.length > 0 && blogClassName.length > 0) {
                    $.each(blogClassId, function (index, data) {
                        if (blogClassName[index] != undefined) {
                            $(".widget-tag-bq").after(`<a class="widget-tag-link" href="JavaScript:Void(0);">${data}_${blogClassName[index]}</a>`);
                        }
                    })
                }
            } else {
                alert("出错了");
            }
            $(".social-links").find(".social-link").remove();
        },
        error: function (a, b) {

        }
    })

})