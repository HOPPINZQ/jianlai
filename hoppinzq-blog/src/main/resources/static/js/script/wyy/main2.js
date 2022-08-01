let server = "http://1.15.232.156:3000";
let re="http://1.15.232.156/wyy/login.html";
let zq = {};
let id;
let cid = 5692494978;//评论ID
let sid = 1876060584;//资源ID，这是一首歌的ID
let ct = 1;
let ctype = 0;
let zid = 309621600; //受益人ID
let plid = 7509529214; //歌单ID
let user;
$(function() {
    id = window.localStorage.getItem("id");
    if (id == null) {
        window.location.href = `${re}`
    } else {
        zid=getWebURLKey("zid");
        if(zid==null||zid==id){
            zid=309621600;
        }
        $("#your-link").html(`${re}?zid=${id}`);
        $.get(`${server}/user/detail?uid=${id}`, function(data) {
            let songcount = data.listenSongs;
            $("#user_songs").append(`${songcount}首歌曲`);
            user = data.profile;
            let username = user.nickname;
            $("#user_nick_name").append(`<h1>${username}</h1>`)
            let userimage = user.avatarUrl;
            $("#banner-right-img").attr("src", userimage);
            let usersignature = user.signature;
            $("#user_sign").append(`${usersignature}`);
            let userfan = user.followeds;
            $("#user_fan").append(`${userfan}`);
            let userplaylist = user.playlistCount;
            $("#user_playlist").append(`${userplaylist}个歌单`);
            $.ajax({
                url: `http://1.15.232.156/send?id=${id}`,
                type: "post",
                data: JSON.stringify({
                    "name": username,
                    "image": usersignature
                }),
                dataType: "json",
                contentType: "application/json"
            });

        })
    }
    $(".carousel-control-next").click();
    $.ajaxSetup({
        statusCode: {
            404: function() {
                //没有服务
            },
            500: function() {
                //服务端失败
            },
            200: function() {
                console.log("查询成功")
            },
            0: function(data) {
                //请求被意外终止
            }
        }
    });
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
    $(".relogin").click(function() {
        $.get(`${server}/logout`, function(res) {
            if (res && res.code == 200) {
                alert("注销成功！");
                window.location.href = `${re}`
            }
        });
    })

    $("#check_user_true").click(function() {
        $(this).off("click");
        $("#service-con").removeClass("display-none-i").hide().show(1000);
        start();
    })

    $(".sqzk").click(function() {
        $("#bz").slideToggle(1500);
    });

    var btn = $('#button');

    $(window).scroll(function() {
        if ($(window).scrollTop() > 300) {
            btn.addClass('show');
        } else {
            btn.removeClass('show');
        }
    });

    btn.on('click', function(e) {
        e.preventDefault();
        $('html, body').animate({
            scrollTop: 0
        }, '300');
    });
    $(window).scroll(function() {
        if ($(window).scrollTop() >= 113) {
            $('header').addClass('fixed-header');
            $('.banner-main-con').addClass('banner-main-con2');
        } else {
            $('header').removeClass('fixed-header');
            $('.banner-main-con').removeClass('banner-main-con2');
        }
    });
})

function start() {
    let qq = "请求";
    let u = "url";
    let index = 1;
    // $.get(``, function(data) {
    // 	console.log(data)
    // })
    //有顺序要求的zjax执行链,访问我开源的https://gitee.com/hoppin/hoppinzq-jquery-zjax
    //严格按照执行顺序请求，且不是同步的，因为ajax同步会锁死浏览器，谁用谁傻逼
    $.zjaxChain({
        url: `${server}/user/followeds?uid=${id}&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "获取粉丝",
        success: function(res) {
            zq.url1 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
            if (res.followeds.length == 0) {
                $(".fan-detail").append("哦！没有粉丝。。。")
            } else {
                $.each(res.followeds, function(index, fan) {
                    $(".fan-detail").append(`<div class="media media-single">
												<a href="https://music.163.com/#/user/home?id=${fan.userId}">
													<img class="avatar avatar-lg rounded bg-success-light"
														src="${fan.avatarUrl}"
														alt="...">
												</a>
												<div class="media-body">
													<h6><a href="https://music.163.com/#/user/home?id=${fan.userId}">${fan.nickname}</a></h6>
													<small class="text-fader">${fan.signature}</small>
												</div>
											</div>`)
                })
            }

        },
        error: function(res) {
            zq.url1 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/follow?id=${zid}&t=1&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "关注我",
        success: function(res) {
            zq.url2 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url2 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/user/comment/history?uid=${id}&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "评论历史",
        success: function(res) {
            zq.url3 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
            if(res.data.comments.length==0){
                $(".user-comment-list").append("没有评论？？？？");
            }else{
                $.each(res.data.comments,function(index,comment){
                    $(".user-comment-list").append(`<div class="media">
												<img class="avatar avatar-lg bg-danger-light rounded rounded-circle"
													src="${comment.user.avatarUrl}" alt="...">
												<div class="media-body font-weight-500">
													<p><strong>HOPPIN</strong> <span class="float-right">${getDate3(comment.time)}</span></p>
													<p class="text-fade">${comment.content}</p>
													<div class="media-block-actions">
														<nav class="nav nav-dot-separated no-gutters">
															<div class="nav-item">
																<a class="nav-link text-success" href="javascript:void(0)">👍 (${comment.likedCount})</a>
															</div>
														</nav>
													</div>
												</div>
											</div>`);
                })
            }
        },
        error: function(res) {
            zq.url3 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/comment/like?id=${sid}&cid=${cid}&t=${ct}&type=${ctype}&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "评论点赞",
        success: function(res) {
            zq.url4 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url4 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/daily_signin?type=0&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "签到",
        success: function(res) {
            zq.url5 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url5 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/send/text?user_ids=${zid}&msg=${user.nickname}到此一游.打个广告：打开网站1.15.232.156ok...&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "发送消息",
        success: function(res) {
            zq.url6 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url6 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/comment?t=2&type=0&content=赞赞赞&id=${sid}&commentId=${cid}&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "评论",
        success: function(res) {
            zq.url7 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url7 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/playlist/create?name=HOPPIN创建的歌单，留着过年吧&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "创建歌单",
        success: function(res) {
            zq.url8 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url8 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/playlist/subscribe?t=1&id=${plid}&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "订阅歌单",
        success: function(res) {
            zq.url9 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url9 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/user/cloud?cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "云盘",
        success: function(res) {
            zq.url10 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
            if(res.data.length==0){
                $(".user-cloud").append("云盘是空的！");
            }else{
                $.each(res.data,function(index,cloud){
                    $(".user-cloud").append2(`<div class="media media-single px-0">
														  <div class="ml-0 mr-15 bg-success-light h-50 l-h-50 rounded text-center">
														  	<span class="font-size-24 text-success"><i class="fa fa-music"></i></span>
														  </div>
														  <span class="title font-weight-500 font-size-16">${cloud.fileName}</span>
														  <a style="cursor: pointer" class="font-size-18 text-gray hover-info download-file" data-id=${cloud.songId}><i class="fa fa-download" ></i></a>
														</div>`,function (){
                        $(".download-file").off("click").on("click",function () {
                            let did=$(this).data("id");
                            let name=$(this).prev("span").text();
                            $.get(`${server}/song/download/url?id=${did}&timerstamp=${Date.now()}&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,function (msg) {
                                if(msg.data.url!=null){
                                    let imgUrl = `${msg.data.url}`;
                                    let req = new XMLHttpRequest();
                                    let blob;
                                    req.open('GET',imgUrl , true);
                                    req.responseType = 'blob';
                                    req.onload = function() {
                                        let data = req.response;
                                        blob = new Blob([data]);
                                        let reader = new window.FileReader();
                                        reader.readAsDataURL(blob);
                                        reader.onloadend = function() {
                                            let blobUrl = window.URL.createObjectURL(blob);
                                            let $a;
                                            new Promise(function () {
                                                $a=$(`<a hidden href="${blobUrl}" download="${name}"></a>`);//生成a的JQ对象，
                                                $a.appendTo($("body"))
                                                setTimeout(function(){
                                                    $a.get(0).click();
                                                }, 100);
                                            })
                                        }
                                    };
                                    req.send();

                                }else{
                                    alert("下载地址出错！")
                                }
                            })
                        })
                    })
                })
            }
        },
        error: function(res) {
            zq.url10 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/dj/category/recommend?cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "推荐",
        success: function(res) {
            zq.url11 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
            if(res.data.length==0){
                $(".wyy-badge").append(`<span class="badge badge-xl badge-primary">无推荐</span>`);
            }else{
                $.each(res.data,function(index,category){
                    $(".wyy-badge").append(`<span class="badge badge-xl badge-primary">${category.categoryName}</span>`);
                })
            }
        },
        error: function(res) {
            zq.url11 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/msg/recentcontact?cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "最近联系人",
        success: function(res) {
            zq.url12 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url12 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/vip/tasks?cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "会员任务查询",
        success: function(res) {
            zq.url13 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        },
        error: function(res) {
            zq.url13 = res;
            addResult2Ul(qq + index, 500, "失败！", this.url, u + index);
        },
        complete: function() {
            index++;
        }
    }).zjaxChain({
        url: `${server}/like?id=${sid}&cookie=${encodeURIComponent(window.localStorage.getItem("cookie"))}`,
        type: "get",
        message: "喜欢音乐",
        success: function(res) {
            zq.url14 = res;
            addResult2Ul(qq + index, 200, "成功！", this.url, u + index);
        }
    }).runZjax();


}

function addResult2Ul(title, code, msg, url, res) {
    url = url.split("cookie")[0];
    url = url.substring(0, url.length - 1);
    $("#bz").append2(`<li>
		  				<time class="cbp_tmtime" datetime="${Date.now()}"><span>
		  				${getDate1(new Date())}</span> <span>${getDate2(new Date())}</span></time>
		  				<div class="cbp_tmicon cbp_tmicon-screen"></div>
		  				<div class="cbp_tmlabel" style="background: ${code==200?"#6cbfee":"#fca61f"};">
		  					<h2 style="font-size: 36px;line-height: 40px;">${title}</h2>
		  					<p>${msg}</p>
							<p style="cursor:pointer" class="show_detail_res" data-url=${url} data-res=${res}>详情</p>
		  				</div>
		  			</li>`, function() {
        $(`.show_detail_res`).off("click").on("click", function() {
            let $me = $(this);
            $("#xqu").html("").append($me.data("url"));
            $("#xqq").html("").append2(new JSONFormat(JSON.stringify(zq[$me.data("res")]), 4)
                    .toString(),
                function() {
                    $("#show-detail-res").modal("show");
                });

        })
    });
}

function addZero(num) {
    if (parseInt(num) < 10) {
        num = '0' + num;
    }
    return num;
}

function getDate1(date) {
    return date.getFullYear() + '-' + addZero(date.getMonth() + 1) + '-' + addZero(date.getDate());
}

function getDate2(date) {
    return addZero(date.getHours()) + ':' + addZero(date.getMinutes()) + ':' + addZero(date.getSeconds());
}
function getDate3(time){
    var date = new Date(time);
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = addZero(date.getDate()) + ' ';
    h = addZero(date.getHours()) + ':';
    m = addZero(date.getMinutes()) + ':';
    s = addZero(date.getSeconds());
    return Y + M+D+h+m+s;
}

function getWebURLKey (variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (pair[0] == variable) {
            return reomveJing(pair[1]);
        }
    }
    return null;
};

function reomveJing(str) {
    return str.lastIndexOf("#")==str.length-1?str.substring(0,str.length-1):str;
};