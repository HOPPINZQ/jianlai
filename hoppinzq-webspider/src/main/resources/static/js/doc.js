(function () {
    window.onload = function () {
        window.setTimeout(fadeout, 500);
    }
    //eruda.init();
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

const menuButtonOpen = document.querySelector(".menu-open");
const menuButtonClose = document.querySelector(".menu-close");
const sidebar = document.querySelector(".doc-sidebar");
const docOverlay = document.querySelector(".doc_overlay");

menuButtonOpen.addEventListener("click", () => {
    sidebar.classList.add("show");
    docOverlay.classList.add('open');
});
menuButtonClose.addEventListener("click", () => {
    sidebar.classList.remove("show");
    docOverlay.classList.remove('open');
});
docOverlay.addEventListener("click", () => {
    sidebar.classList.remove("show");
    docOverlay.classList.remove('open');
});


// ===== copy code
const copyButton = document.querySelectorAll('.copy-btn');
copyButton.forEach(element => {
    element.addEventListener('click', (e) => {
        $(copyButton).each(function (index,elem) {
            $(elem).text("复制");
        });
        const elem = e.target.parentElement.children[1].innerText;
        const el = document.createElement('textarea');
        el.value = elem;
        document.body.appendChild(el);
        el.select();
        document.execCommand("copy");
        $(element).text("复制成功！");

        document.body.removeChild(el)
    })
});
var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl)
})

$(function () {
    $(".img-light").on("click",function () {
        let $img=$(this).find("img")[0];
        $(".lb-outerContainer").width($img.width*2).height($img.height);
        $("#lightbox").css("position","fixed");
        $("#lightbox").css("top","400px !important");//先写死
    })

})