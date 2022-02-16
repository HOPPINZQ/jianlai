$(function () {
    "use strict";
    $("#range_01").ionRangeSlider(), $("#range_02").ionRangeSlider( {
        min: 100, max: 1e3, from: 550
    }),
	$("#range_03").ionRangeSlider( {
        type: "double", grid: !0, min: 0, max: 1e3, from: 200, to: 800, prefix: "￥"
    }),
	$("#range_04").ionRangeSlider( {
        type: "double", grid: !0, min: -1e3, max: 1e3, from: -500, to: 500
    }),
	$("#range_05").ionRangeSlider( {
        type: "double", grid: !0, min: -1e3, max: 1e3, from: -500, to: 500, step: 250
    }),
	$("#range_06").ionRangeSlider( {
        grid: !0, from: 3, values: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
    }),
	$("#range_07").ionRangeSlider( {
        grid: !0, min: 1e3, max: 1e6, from: 2e5, step: 1e3, prettify_enabled: !0
    }),
	$("#range_08").ionRangeSlider( {
        min: 100, max: 1e3, from: 550, disable: !0
    }),
	$("#range_09").ionRangeSlider( {
        grid: !0, min: 18, max: 70, from: 30, prefix: "年龄 ", max_postfix: "+"
    }),
	$("#range_10").ionRangeSlider( {
        type: "double", min: 100, max: 200, from: 145, to: 155, prefix: "宽度: ", postfix: " 米", decorate_both: !0
    }),
	$("#range_11").ionRangeSlider( {
        type: "single", grid: !0, min: -90, max: 90, from: 0, postfix: "°"
    }),
	$("#range_12").ionRangeSlider( {
        type: "double", min: 1e3, max: 2e3, from: 1200, to: 1800, hide_min_max: !0, hide_from_to: !0, grid: !0
    })
});