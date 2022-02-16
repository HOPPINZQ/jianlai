$(function () {
    "use strict";

    $('.mailbox-messages input[type="checkbox"]').iCheck({
        checkboxClass: 'icheckbox_flat-blue',
        radioClass: 'iradio_flat-blue'
    });

    $(".checkbox-toggle").click(function () {
        var clicks = $(this).data('clicks');
        if (clicks) {
            $(".mailbox-messages input[type='checkbox']").iCheck("uncheck");
            $(".ion", this).removeClass("ion-android-checkbox-outline").addClass('ion-android-checkbox-outline-blank');
        } else {
            $(".mailbox-messages input[type='checkbox']").iCheck("check");
            $(".ion", this).removeClass("ion-android-checkbox-outline-blank").addClass('ion-android-checkbox-outline');
        }
        $(this).data("clicks", !clicks);
    });

    $(".mailbox-star").click(function (e) {
        e.preventDefault();
        var $this = $(this).find("a > i");
        var glyph = $this.hasClass("glyphicon");
        var fa = $this.hasClass("fa");
        if (glyph) {
            $this.toggleClass("glyphicon-star");
            $this.toggleClass("glyphicon-star-empty");
        }

        if (fa) {
            $this.toggleClass("fa-star");
            $this.toggleClass("fa-star-o");
        }
    });

});

$('.read-mail-bx').slimScroll({
    height: '350'
});

$('.inbox-bx').slimScroll({
    height: '540'
});

$('.contact-bx').slimScroll({
    height: '280'
});