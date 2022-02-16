$(function () {
    $(".tst1").on("click", function () {
        $.toast({
            heading: '新消息',
            text: 'HOPPIN给你发了一个私信',
            position: 'top-right',
            loaderBg: '#ff6849',
            icon: 'info',
            hideAfter: 3000,
            stack: 6
        });
    });
    $(".tst2").on("click", function () {
        $.toast({
            heading: '新消息',
            text: 'HOPPIN给你发了一个私信',
            position: 'top-right',
            loaderBg: '#ff6849',
            icon: 'warning',
            hideAfter: 3500,
            stack: 6
        });
    });
    $(".tst3").on("click", function () {
        $.toast({
            heading: '新消息',
            text: 'HOPPIN给你发了一个私信',
            position: 'top-right',
            loaderBg: '#ff6849',
            icon: 'success',
            hideAfter: 3500,
            stack: 6
        });
    });
    $(".tst4").on("click", function () {
        $.toast({
            heading: '新消息',
            text: 'HOPPIN给你发了一个私信',
            position: 'top-right',
            loaderBg: '#ff6849',
            icon: 'error',
            hideAfter: 3500
        });
    });
});
