$(function () {
    "use strict";

    $('.connectedSortable').sortable({
        placeholder: 'sort-highlight',
        connectWith: '.connectedSortable',
        handle: '.box-header, .nav-tabs',
        forcePlaceholderSize: true,
        zIndex: 999999
    });
    $('.connectedSortable .box-header, .connectedSortable .nav-tabs-custom').css('cursor', 'move');

    $('.todo-list').sortable({
        placeholder: 'sort-highlight',
        handle: '.handle',
        forcePlaceholderSize: true,
        zIndex: 999999
    });

    $('.todo-list').todoList({
        onCheck: function () {
            window.console.log($(this), '被选中');
        },
        onUnCheck: function () {
            window.console.log($(this), '被选中');
        }
    });

});