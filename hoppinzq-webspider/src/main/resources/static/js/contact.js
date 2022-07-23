$(function () {
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

    $(".send").click(function () {
        let name=$("#name").val();
        let contact=$("#contact").val();
        let message=$("#message").val();
         $.get(`${ip}/hoppinzq?method=feedback&params={"name":"${name}","contact":"${contact}","message":"${message}"}`,function (data){
             let m=JSON.parse(data);
             if(m.code==200){
                 alert("反馈成功！");
             }else{
                 alert("反馈失败！");
             }
         })
    })
})
function fadeout() {
    document.querySelector('.preloader').style.opacity = '0';
    document.querySelector('.preloader').style.display = 'none';
}