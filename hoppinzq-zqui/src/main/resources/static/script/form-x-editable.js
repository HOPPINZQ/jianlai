$(function () {
    "use strict";
    $(function () {
        $('#username').editable({
            type: 'text',
            pk: 1,
            name: 'username',
            title: '输入用户名'
        });
        $('#firstname').editable({
            validate: function (value) {
                if ($.trim(value) == '') return '不可为空';
            }
        });
        $('#sex').editable({
            prepend: "性别",
            source: [{
                value: 1,
                text: '男'
            }, {
                value: 2,
                text: '女'
            }],
            display: function (value, sourceData) {
                var colors = {
                        "": "#98a6ad",
                        1: "#5fbeaa",
                        2: "#5d9cec"
                    },
                    elem = $.grep(sourceData, function (o) {
                        return o.value == value;
                    });

                if (elem.length) {
                    $(this).text(elem[0].text).css("color", colors[value]);
                } else {
                    $(this).empty();
                }
            }
        });

        $('#status').editable();

        $('#group').editable({
            showbuttons: false
        });

        $('#dob').editable();

        $('#comments').editable({
            showbuttons: 'bottom'
        });

        $('#inline-username').editable({
            type: 'text',
            pk: 1,
            name: 'username',
            title: '输入用户名',
            mode: 'inline'
        });

        $('#inline-firstname').editable({
            validate: function (value) {
                if ($.trim(value) == '') return '不可为空';
            },
            mode: 'inline'
        });

        $('#inline-sex').editable({
            prepend: "选择性别",
            mode: 'inline',
            source: [{
                value: 1,
                text: '男'
            }, {
                value: 2,
                text: '女'
            }],
            display: function (value, sourceData) {
                var colors = {
                        "": "#98a6ad",
                        1: "#5fbeaa",
                        2: "#5d9cec"
                    },
                    elem = $.grep(sourceData, function (o) {
                        return o.value == value;
                    });

                if (elem.length) {
                    $(this).text(elem[0].text).css("color", colors[value]);
                } else {
                    $(this).empty();
                }
            }
        });
        $('#inline-status').editable({
            mode: 'inline'
        });
        $('#inline-group').editable({
            showbuttons: false,
            mode: 'inline'
        });
        $('#inline-dob').editable({
            mode: 'inline'
        });
        $('#inline-comments').editable({
            showbuttons: 'bottom',
            mode: 'inline'
        });
    });
});