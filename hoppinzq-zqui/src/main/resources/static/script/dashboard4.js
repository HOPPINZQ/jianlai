$(function () {

    'use strict';

    var options = {
        series: [{
            name: 'git提交统计',
            data: [8, 9, 2, 4, 7, 1.5, 6, 5, 3, 2]
        }],
        chart: {
            foreColor: "#bac0c7",
            type: 'bar',
            height: 200,
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
        colors: ['#6993ff'],
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '30%',
                endingShape: 'rounded',
                colors: {
                    backgroundBarColors: ['#f0f0f0'],
                    backgroundBarOpacity: 0,
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
            show: false,
        },
        fill: {
            opacity: 1
        }
    };

    var chart = new ApexCharts(document.querySelector("#charts_widget_1_chart"), options);
    chart.render();


    var options = {
        series: [9, 5, 13],
        labels: ['吃饭', '睡觉', '打火猫'],
        chart: {
            height: 230,
            type: 'donut',
        },
        dataLabels: {
            enabled: false
        },
        responsive: [{
            breakpoint: 480,
            options: {
                chart: {
                    width: 200
                },
                legend: {
                    show: false
                }
            }
        }],
        colors: ['#04a08b', '#6993ff', '#ff9920'],
        legend: {
            position: 'bottom',
            horizontalAlign: 'center',
        }
    };

    var chart = new ApexCharts(document.querySelector("#charts_widget_2_chart"), options);
    chart.render();


    var options = {
        chart: {
            height: 180,
            type: "radialBar"
        },

        series: [77],
        colors: ['#0052cc'],
        plotOptions: {
            radialBar: {
                hollow: {
                    margin: 15,
                    size: "70%"
                },
                track: {
                    background: '#ff9920',
                },

                dataLabels: {
                    showOn: "always",
                    name: {
                        offsetY: -10,
                        show: false,
                        color: "#888",
                        fontSize: "13px"
                    },
                    value: {
                        color: "#111",
                        fontSize: "30px",
                        show: true
                    }
                }
            }
        },

        stroke: {
            lineCap: "round",
        },
        labels: ["Progress"]
    };

    var chart = new ApexCharts(document.querySelector("#revenue5"), options);

    chart.render();


    var options1 = {
        series: [{
            name: 'Java',
            data: [44, 55, 41, 67, 22, 43, 44]
        }, {
            name: 'SQL',
            data: [13, 23, 20, 8, 13, 27, 13]
        }, {
            name: 'JavaScript',
            data: [23, 17, 12, 9, 15, 24, 18]
        }, {
            name: 'Python',
            data: [23, 12, 12, 15, 12, 5, 11]
        }, {
            name: 'VUE',
            data: [14, 25, 18, 8, 5, 11, 14]
        }],
        chart: {
            foreColor: "#bac0c7",
            type: 'bar',
            height: 310,
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
        colors: ['#0052cc', '#04a08b', '#00baff', '#ff9920', '#ff562f'],
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '15%',
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
            categories: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Set', 'Sun'],
        },
        legend: {
            show: true,
            position: 'top',
            horizontalAlign: 'center',
        },
        fill: {
            opacity: 1
        }
    };

    var chart1 = new ApexCharts(document.querySelector("#charts_widget_11_chart"), options1);
    chart1.render();

}); 
