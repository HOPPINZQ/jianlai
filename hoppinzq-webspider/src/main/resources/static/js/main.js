let $me;
let isP=false;
let isWeixin=false;
let errImg="http://150.158.28.40:9000/404/nopic.jpg";
$(function (){
    $me=$(this);
    /**
     * 增强JQuery的append方法
     * 在$原型链追加append2方法，使之可以在将html加入后执行回调
     * @See zq框架zq.js
     * @param html
     * @param callback
     */
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
    if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        isP=true;
        if(window.localStorage.getItem("close_fe")==null){
            alert("检测到您正在使用移动端浏览，移动端禁止配置和操作爬虫，如果你要浏览其他内容，请忽略！");
        }
        var ua = navigator.userAgent.toLowerCase();
        //判断是否内核是微信浏览器
        //微信会自动增强扩展网页，如用户不禁用需要我自己搞掉增强效果
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            isWeixin=true;
        }
    }
    window.setTimeout(fadeout, 500);

    window.onscroll = function () {
        var backToTo = document.querySelector(".scroll-top");
        if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
            backToTo.style.display = "flex";
        } else {
            backToTo.style.display = "none";
        }
    };
    let navbarToggler = document.querySelector(".mobile-menu-btn");
    navbarToggler.addEventListener('click', function () {
        navbarToggler.classList.toggle("active");
    });

    $(".close-fe").click(function () {
        alert('ok...')
        if(window.localStorage.getItem("close_fe")==null){
            window.localStorage.setItem("close_fe","0");
        }
    })

    $("#search").click(function () {
        if($("#key").val()==""){
            alert("请输入关键字");
            $("#key").focus();
        }else{
            searchWeb($("#key").val());
        }
    })

    $(".sqzk").click(function () {
        $(".search-nr").hide("slow");
    })

    let key=getWebURLKey("key");
    if(key!=null&&key.length!=0){
        $("#key").val(key)
        searchWeb($("#key").val());
    }

    let copyButton = document.querySelectorAll('.copy-btn');
    copyButton.forEach(element => {
        element.addEventListener('click', (e) => {
            $(copyButton).each(function (index,elem) {
                $(elem).text("尝试");
            });
            let elem = e.target.parentElement.children[1].innerText;
            let el = document.createElement('textarea');
            el.value = elem;
            document.body.appendChild(el);
            el.select();
            document.execCommand("copy");
            $(element).text("复制成功！");
            $(`#${$(element).data("input")}`).val(elem);
            $(`#${$(element).data("btn")}`).click();

            document.body.removeChild(el)
        })
    });

    $("#df_project").buttonLoading().off("click").on("click", function () {
        $(this).off("click").buttonLoading("start");
        $.get(`${ip}/hoppinzq?method=run&params={"id":"1","url":"https://thwiki.cc/%E5%AE%98%E6%96%B9%E8%A7%92%E8%89%B2%E5%88%97%E8%A1%A8"}`,function (data) {
            $(".df").html("");
            let df=JSON.parse(data);
            if(df.code!=200){
                $(".df").html(`<div class="content">
                                                    <h6>出现错误</h6>
                                                    <p>可以向我<a href="contact.html">反馈错误</a></p>
                                                </div>`);
            }else{
                $("#ffff").append2(`<div class="content" id="sqzhpf">
                                                    <h6 style="cursor: pointer">[收起展开]</h6>
                                                </div>`,function () {
                    $("#sqzhpf").off("click").on("click",function () {
                        $(".df").slideToggle("slow");
                    })
                })
                let msg=df.data;
                let names=msg.name;
                let length=names.length;
                let descriptions=msg.description;
                let imgs=msg.img;
                let links=msg.link;
                let nicknames=msg.nickname;
                for(let i=0;i<length;i++){
                    $(".df").append(`<div class="col-lg-2 col-xl-3 col-md-4 col-sm-6">
                                            <div class="single-team">
                                                <div class="top-content">
                                                    <div class="image">
                                                        ${loadImage(imgs[i].substring(1,imgs[i].length-1),"",names[i],errImg)}
                                                    </div>
                                                    <div class="social">${nicknames[i]}</div>
                                                </div>
                                                <div class="name">
                                                    <h3><a href="https://thwiki.cc${links[i]}" target="_blank">${names[i]}</a></h3>
                                                    <span>${descriptions[i]}</span>
                                                </div>
                                            </div>
                                        </div>`)
                }
            }
        })
    })

    $("#csdn_l").buttonLoading().off("click").on("click", function () {
        let $me=$(this);
        let csdnLink=$("#csdnlink").val();
        if(csdnLink.length==0){
            alert("请输入csdn博客链接");
            $("#csdnlink").focus();
            return;
        }
        if(csdnLink.indexOf("?")!=-1){
            csdnLink=csdnLink.substring(0,csdnLink.indexOf("?"))
        }
        $.ajax({
            url:`${ip}/hoppinzq?method=csdnFindMessage&params={'url':'${csdnLink}','type':'1'}`,
            type:'get',
            beforeSend:function () {
                $me.buttonLoading("start");
                $(".blog-st-1").html("");
                $(".blog-text-1").html("");
            },
            success:function (data) {
                let blog=JSON.parse(data);
                if(blog.code==200&&blog.data.html!=null){
                    $(".blog-st-1").append2(`<div class="alert-box success-alert"><div class="alert">
                                                            <div class="alert-icon">
                                                                <i class="lni lni-checkmark-circle"></i>
                                                            </div>
                                                            <div class="content">
                                                                <h6>爬取成功</h6>
                                                            </div>
                                                            <p style="cursor:pointer;color: #1e1d1d" id="zhsqb-1">[展开/收起]</p>
                                                        </div></div>`,function () {
                        $("#zhsqb-1").off("click").on("click",function () {
                            $(".blog-text-1").slideToggle("slow");
                        })
                    });

                    let html=`<blockquote><p><b style=""><font color="#46acc8" style="background-color: rgb(255, 255, 255);">文章爬取自csdn用户${blog.data.author}的文章，${blog.data.date}，有${blog.data.collect}个收藏，有${blog.data.like}个喜欢，原文请<a href="${blog.data.url}" target="_blank" style="">点我访问</a>。</font></b></p></blockquote>`;
                    $(".blog-text-1").append(html);
                    let tabClass=blog.data.classType;
                    if(tabClass.length!=0){
                        let uul=$(`<ul class="popular-tag"></ul>`);
                        uul.append(`<li>博客标签：</li>`);
                        $.each(tabClass,function (i,d){
                            uul.append(`<li><a href="javascript:void(0)" >${d}</a></li>`);
                        })
                        $(".blog-text-1").append(uul)
                    }
                    $(".blog-text-1").append2(blog.data.html,function () {
                        $(".blog-text-1").find("img").each(function (index_img,element_img){
                            let $element_img=$(element_img);
                            $(this).wrap($(`<a href="${$element_img.attr("src")||$element_img.data("src")}" data-lightbox="example-set" title="点击x关闭"></a>`).on("click",function () {
                                $(".lb-outerContainer").width($element_img.width()*2).height($element_img.height());
                                $("#lightbox").css("position","fixed");
                                $("#lightbox").css("top","400px !important");//先写死
                            }))
                            if($element_img.data("src")&&$element_img.attr("src")==undefined){
                                $element_img.attr("src",$element_img.data("src"));
                            }
                        })
                    });
                }else{
                    $(".blog-st-1").append(`<div class="alert-box danger-alert"><div class="alert">
                                                <div class="alert-icon">
                                                    <i class="lni lni-cross-circle"></i>
                                                </div>
                                                <div class="content">
                                                    <h6>出现错误</h6>
                                                    <p>可以向我<a href="contact.html">反馈错误</a></p>
                                                </div>
                                            </div></div>`);
                }
            },
            complete:function () {
                $me.buttonLoading("stop");
            }
        })
    })

    $("#weixin_l").buttonLoading().off("click").on("click", function () {
        let $me=$(this);
        let weixinLink=$("#weixinlink").val();
        if(weixinLink.length==0){
            alert("请输入输入微信公众号文章链接");
            $("#weixinlink").focus();
            return;
        }
        $.ajax({
            url:`${ip}/hoppinzq?method=csdnFindMessage&params={'url':'${weixinLink}','type':'3'}`,
            type:'get',
            beforeSend:function () {
                $me.buttonLoading("start");
                $(".blog-st-2").html("");
                $(".blog-text-2").html("");
            },
            success:function (data) {
                let blog=JSON.parse(data);
                if(blog.code==200&&blog.data.html!=null){
                    $(".blog-st-2").append2(`<div class="alert-box success-alert"><div class="alert">
                                                            <div class="alert-icon">
                                                                <i class="lni lni-checkmark-circle"></i>
                                                            </div>
                                                            <div class="content">
                                                                <h6>爬取成功</h6>
                                                            </div>
                                                            <p style="cursor:pointer;color: #1e1d1d" id="zhsqb-2">[展开/收起]</p>
                                                        </div></div>`,function () {
                        $("#zhsqb-2").off("click").on("click",function () {
                            $(".blog-text-2").slideToggle("slow");
                        })
                    });

                    let html=`<blockquote><p><b style=""><font color="#46acc8" style="background-color: rgb(255, 255, 255);">文章爬取自微信公众号${blog.data.author}的文章，有${blog.data.collect}个收藏，有${blog.data.like}个喜欢，原文请<a href="${blog.data.url}" target="_blank" style="">点我访问</a>。</font></b></p></blockquote>`;
                    $(".blog-text-2").append(html);
                    $(".blog-text-2").append2(blog.data.html,function () {
                        $(".blog-text-2").find("img").each(function (index_img,element_img){
                            let $element_img=$(element_img);
                            $(this).wrap($(`<a href="${$element_img.attr("src")||$element_img.data("src")}" data-lightbox="example-set" title="点击x关闭"></a>`).on("click",function () {
                                $(".lb-outerContainer").width($element_img.width()*2).height($element_img.height());
                                $("#lightbox").css("position","fixed");
                                $("#lightbox").css("top","400px !important");//先写死
                            }))
                            if($element_img.data("src")&&$element_img.attr("src")==undefined){
                                $element_img.attr("src",$element_img.data("src"));
                            }
                        })
                    });
                }else{
                    $(".blog-st-2").append(`<div class="alert-box danger-alert"><div class="alert">
                                                <div class="alert-icon">
                                                    <i class="lni lni-cross-circle"></i>
                                                </div>
                                                <div class="content">
                                                    <h6>出现错误</h6>
                                                    <p>可以向我<a href="contact.html">反馈错误</a></p>
                                                </div>
                                            </div></div>`);
                }

            },
            complete:function () {
                $me.buttonLoading("stop");
            }
        })
    })

    $("#tryL").click(function () {
        setTimeout(function () {
            let pos='right';
            if(isP){
                pos='bottom';
            }
            if(!isWeixin){
                $(".hero-content").tips({
                    popover: {
                        title: '在这停顿',
                        description: '在该处尝试百度搜索的功能，可以尝试通过多输入关键字以精确查询结果<hr>' +
                            '该提示框可以访问我的开源项目<a href="https://gitee.com/hoppin/hoppinzq-jquery-tips" target="_blank">基于JQuery写了一个提示框</a>(无需任何css，只需引入js即可，不会有任何样式冲突)',
                        position: pos,
                        cancelFunc: function () {

                        }
                    },
                })
            }else{

            }
        },400)
    })

    $.get(`${ip}/hoppinzq?method=queryAll&params={}`,function (data) {
        let m=JSON.parse(data)
        if(m.code==200){
            let sps=m.data;
            $.each(sps,function (i,s){
                if(s.id>0){
                    $("#allSpider").append(`<tr>
                                                <td>${s.name}</td>
                                                <td>${s.description}</td>
                                                <td class=""><a target="_blank" href="${s.urldemo}" class="ticket">访问</a></td>
                                                <td class=""><a href="${ip}/hoppinzq?method=run&amp;params={'id':'${s.id}','url':'${s.urldemo}'}" title="${ip}/hoppinzq?method=run&amp;params={"id":"${s.id}","url":"${s.urldemo==""?"目标网站":s.urldemo}"}">点我</a></td>
                                                <td><a class="ticket" href="add.html?id=${s.id}">配置</a></td>
                                            </tr>`);
                }
            })
        }
    })

})

function getWebURLKey(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (pair[0] == variable) {
            return this.reomveJing(pair[1]);
        }
    }
    return null;
}

function reomveJing(str) {
    return str.lastIndexOf("#")==str.length-1?str.substring(0,str.length-1):str;
}

function searchWeb(key){
    $.get(`${ip}/hoppinzq?method=queryweb&params={"search":"${key}"}`,function (data) {
        $(".search-nr").show(1000);
        $("#listShow").html("");
        data=JSON.parse(data);
        if(data.code!=200){
            $("#listShow").html("没有内容被找到");
        }else{
            let links=data.data;
            if(links.length==0){
                $("#listShow").html("没有内容被找到");
            }else{
                let $li="";
                $.each(links,function (index,link){
                    $li+=`<li class="li-item hide"><a href="${link.link}" title="${link.link}" class="page-web-link" target="_blank">${link.title.length>50?link.title.substring(0,100):link.title}</a></li>`
                })
                $("#listShow").html($li);
                $me.cPager({
                    pageSize: 8, //每一页显示的记录条数
                    pageid: "pager", //分页容器ID
                    itemClass: "li-item" //个体元素名称
                });
            }
        }
    })
}

function fadeout() {
    document.querySelector('.preloader').style.opacity = '0';
    document.querySelector('.preloader').style.display = 'none';
}

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
function loadImage(url,className,alt,errorUrl,style) {
    let id=uuid(32,62);
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
        $(".image-"+id).attr("src",errorUrl);
    };
    return image.outerHTML;
}

function uuid(len, radix){
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
}