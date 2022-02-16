$(function () {
    'use strict';
    $('.countnm').each(function () {
        $(this).prop('Counter', 0).animate({
            Counter: $(this).text()
        }, {
            duration: 5000,
            easing: 'swing',
            step: function (now) {
                $(this).text(Math.ceil(now));
            }
        });
    });

    $('.act-div').slimScroll({
        height: '410px'
    });

    var options = {
        series: [{
            name: "在线时长",
            data: [3, 4, 10, 5, 1, 2]
        }],
        chart: {
            foreColor: "#bac0c7",
            height: 180,
            type: 'area',
            zoom: {
                enabled: false
            }
        },
        colors: ['#0052cc'],
        dataLabels: {
            enabled: false,
        },
        stroke: {
            show: true,
            curve: 'smooth',
            lineCap: 'butt',
            colors: undefined,
            width: 1,
            dashArray: 0,
        },
        markers: {
            size: 1,
            colors: '#0052cc',
            strokeColors: '#0052cc',
            strokeWidth: 2,
            strokeOpacity: 0.9,
            strokeDashArray: 0,
            fillOpacity: 1,
            discrete: [],
            shape: "circle",
            radius: 5,
            offsetX: 0,
            offsetY: 0,
            onClick: undefined,
            onDblClick: undefined,
            hover: {
                size: undefined,
                sizeOffset: 3
            }
        },
        grid: {
            borderColor: '#f7f7f7',
            row: {
                colors: ['transparent'],
                opacity: 0
            },
            yaxis: {
                lines: {
                    show: true,
                },
            },
        },
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                opacityFrom: 0.01,
                opacityTo: 1,
                stops: [0, 90, 100]
            }
        },
        xaxis: {
            categories: ['周一', '周二', '周三', '周四', '周五', '周六'],
            labels: {
                show: true,
            },
            axisBorder: {
                show: true
            },
            axisTicks: {
                show: true
            },
            tooltip: {
                enabled: true,
            },
        },
        yaxis: {
            labels: {
                show: true,
                formatter: function (val) {
                    return val + "小时";
                }
            }

        },
    };
    var chart = new ApexCharts(document.querySelector("#charts_widget_2_chart"), options);
    chart.render();

    var options = {
        series: [{
            name: '天辉',
            data: [44, 55, 41, 67, 22, 43, 44, 55, 41, 67, 22, 90]
        }, {
            name: '夜魇',
            data: [13, 23, 20, 8, 13, 27, 13, 23, 20, 8, 13, 40]
        }],
        chart: {
            foreColor: "#bac0c7",
            type: 'bar',
            height: 347,
            stacked: true,
            toolbar: {
                show: false
            },
            zoom: {
                enabled: true
            }
        },
        responsive: [{
            breakpoint: 480,
            options: {
                legend: {
                    position: 'bottom',
                    offsetX: -10,
                    offsetY: 0
                }
            }
        }],
        grid: {
            show: true,
            borderColor: '#f7f7f7',
        },
        colors: ['#6993ff', '#f64e60'],
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '20%',
                colors: {
                    backgroundBarColors: ['#f0f0f0'],
                    backgroundBarOpacity: 1,
                },
            },
        },
        dataLabels: {
            enabled: false
        },

        xaxis: {
            categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        },
        legend: {
            show: true,
            position: 'top',
            horizontalAlign: 'right',
        },
        fill: {
            opacity: 1
        }
    };

    var chart = new ApexCharts(document.querySelector("#charts_widget_1_chart"), options);
    chart.render();


});
