+function ($) {
    'use strict'
    $(document).on('click', '.media[data-provide~="selectable"], .media-list[data-provide~="selectable"] .media:not(.media-list-header):not(.media-list-footer)', function () {
        var input = $(this).find('input');
        input.prop('checked', !input.prop("checked"));
        if (input.prop("checked")) {
            $(this).addClass('active');
        } else {
            $(this).removeClass('active');
        }
    });

}(jQuery) 


