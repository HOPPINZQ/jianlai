(function () {
    window.onload = function () {
        window.setTimeout(fadeout, 500);
    }

    function fadeout() {
        document.querySelector('.preloader').style.opacity = '0';
        document.querySelector('.preloader').style.display = 'none';
    }

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
})();

let $me;
$(function (){
    $me=$(this);
    if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
        if(window.localStorage.getItem("close_fe")==null){
            alert("检测到您正在使用移动端浏览，移动端禁止配置和操作爬虫，如果你要浏览其他内容，请忽略！");
        }

    }
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

})

function searchWeb(key){
    $.get(`http://150.158.28.40:8806/hoppinzq?method=queryweb&params={"search":"${key}"}`,function (data) {
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
                    $li+=`<li class="li-item hide"><a href="${link.link}" class="page-web-link" target="_blank">${link.title}</a></li>`
                })
                $("#listShow").html($li);
                $me.cPager({
                    pageSize: 8, //每一页显示的记录条数
                    pageid: "pager", //分页容器ID
                    itemClass: "li-item" //个体元素名称
                });
            }
        }

        console.log(data)
    })
}