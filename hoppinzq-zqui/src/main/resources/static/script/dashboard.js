$(function () {
    'use strict';
    let options = {
        chart: {
            height: 325,
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
    let chart = new ApexCharts(document.querySelector("#revenue5"), options);
    chart.render();
    $("#calendar").fullCalendar({
        defaultView: 'month',
        allDayText: '看比赛了',
        handleWindowResize: true,
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },
        events: [{
            title: 'G2 vs NIP!',
            start: '2022-02-06 12:44:00',
            end: '2022-02-06 12:44:00',
            className: 'bg-info'
        }, {
            title: 'Navi vs NIP',
            start: '2022-02-08',
            end: '2022-02-08',
            className: 'bg-info'
        }],
    });
});
