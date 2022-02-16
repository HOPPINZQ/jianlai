var AnimationsCSS3 = function () {
    var _componentAnimationCSS = function () {
        $('body').on('click', '.animation', function (e) {
            var animation = $(this).data('animation');
            $(this).parents('.box').addClass('animated ' + animation).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
                $(this).removeClass('animated ' + animation);
            });
            e.preventDefault();
        });
    };
    return {
        init: function () {
            _componentAnimationCSS();
        }
    }
}();

document.addEventListener('DOMContentLoaded', function () {
    AnimationsCSS3.init();
});
