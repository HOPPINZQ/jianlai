!function ($) {
    "use strict";

    var CalendarApp = function () {
        this.$body = $("body")
        this.$calendar = $('#calendar'),
            this.$event = ('#external-events div.external-event'),
            this.$categoryForm = $('#add-new-events form'),
            this.$extEvents = $('#external-events'),
            this.$modal = $('#my-event'),
            this.$saveCategoryBtn = $('.save-category'),
            this.$calendarObj = null
    };


    CalendarApp.prototype.onDrop = function (eventObj, date) {
        var $this = this;
        var originalEventObject = eventObj.data('eventObject');
        var $categoryClass = eventObj.attr('data-class');
        var copiedEventObject = $.extend({}, originalEventObject);
        copiedEventObject.start = date;
        if ($categoryClass)
            copiedEventObject['className'] = [$categoryClass];
        $this.$calendar.fullCalendar('renderEvent', copiedEventObject, true);
        if ($('#drop-remove').is(':checked')) {
            eventObj.remove();
        }
    },
        CalendarApp.prototype.onEventClick = function (calEvent, jsEvent, view) {
            var $this = this;
            var form = $("<form></form>");
            form.append("<label>Change event name</label>");
            form.append("<div class='input-group'><input class='form-control' type=text value='" + calEvent.title + "' /><span class='input-group-btn'><button type='submit' class='btn btn-success waves-effect waves-light'><i class='fa fa-check'></i> Save</button></span></div>");
            $this.$modal.modal({
                backdrop: 'static'
            });
            $this.$modal.find('.delete-event').show().end().find('.save-event').hide().end().find('.modal-body').empty().prepend(form).end().find('.delete-event').unbind('click').click(function () {
                $this.$calendarObj.fullCalendar('removeEvents', function (ev) {
                    return (ev._id == calEvent._id);
                });
                $this.$modal.modal('hide');
            });
            $this.$modal.find('form').on('submit', function () {
                calEvent.title = form.find("input[type=text]").val();
                $this.$calendarObj.fullCalendar('updateEvent', calEvent);
                $this.$modal.modal('hide');
                return false;
            });
        },
        CalendarApp.prototype.onSelect = function (start, end, allDay) {
            var $this = this;
            $this.$modal.modal({
                backdrop: 'static'
            });
            var form = $("<form></form>");
            form.append("<div class='row'></div>");
            form.find(".row")
                .append("<div class='col-md-6'><div class='form-group'><label class='control-label'>事件名称</label><input class='form-control' placeholder='输入事件名称' type='text' name='title'/></div></div>")
                .append("<div class='col-md-6'><div class='form-group'><label class='control-label'>分类</label><select class='form-control' name='category'></select></div></div>")
                .find("select[name='category']")
                .append("<option value='bg-danger'>红</option>")
                .append("<option value='bg-success'>绿</option>")
                .append("<option value='bg-purple'>紫</option>")
                .append("<option value='bg-primary'>紫</option>")
                .append("<option value='bg-pink'>粉</option>")
                .append("<option value='bg-info'>蓝</option>")
                .append("<option value='bg-warning'>黄</option></div></div>");
            $this.$modal.find('.delete-event').hide().end().find('.save-event').show().end().find('.modal-body').empty().prepend(form).end().find('.save-event').unbind('click').click(function () {
                form.submit();
            });
            $this.$modal.find('form').on('submit', function () {
                var title = form.find("input[name='title']").val();
                var beginning = form.find("input[name='beginning']").val();
                var ending = form.find("input[name='ending']").val();
                var categoryClass = form.find("select[name='category'] option:checked").val();
                if (title !== null && title.length != 0) {
                    $this.$calendarObj.fullCalendar('renderEvent', {
                        title: title,
                        start: start,
                        end: end,
                        allDay: false,
                        className: categoryClass
                    }, true);
                    $this.$modal.modal('hide');
                } else {
                    alert('事件标题不可为空！');
                }
                return false;

            });
            $this.$calendarObj.fullCalendar('unselect');
        },
        CalendarApp.prototype.enableDrag = function () {
            $(this.$event).each(function () {
                var eventObject = {
                    title: $.trim($(this).text())
                };
                $(this).data('eventObject', eventObject);
                $(this).draggable({
                    zIndex: 999999,
                    revert: true,
                    revertDuration: 0
                });
            });
        }
    CalendarApp.prototype.init = function () {
        this.enableDrag();
        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();
        var form = '';
        var today = new Date($.now());
        //
        var defaultEvents = [{
            title: '我的生日!',
            start: '2022-02-06 12:44:00',
            end: '2022-02-06 12:44:00',
            className: 'bg-info'
        }, {
            title: '锻炼身体',
            start: today,
            end: today,
            className: 'bg-danger'
        }, {
            title: '敲代码',
            start: '2022-02-08',
            end: '2022-02-10',
            className: 'bg-info'
        },
            {
                title: '背单词',
                start: '2022-02-07 12:44:00',
                end: '2022-02-07 14:44:00',
                className: 'bg-danger'
            }, {
                title: '看视频',
                start: new Date($.now() + 784800000),
                className: 'bg-success'
            }];

        var $this = this;
        $this.$calendarObj = $this.$calendar.fullCalendar({
            defaultView: 'month',
            allDayText: '元气满满的一天',
            handleWindowResize: true,
            customButtons: {
                myCustomButton: {
                    text: '自定义按钮',
                    click: function () {
                        alert('点击了自定义按钮!');
                    }
                }
            },
            businessHours: [
                {
                    dow: [1, 2, 3, 4, 5],
                    start: '05:30',
                    end: '24:00'
                },
                {
                    dow: [6, 7],
                    start: '6:00',
                    end: '23:00'
                }
            ],
            header: {
                left: 'prev,next today myCustomButton',
                center: 'title',
                right: 'month,agendaWeek,agendaDay,list,listMonth,listYear'
            },
            events: defaultEvents,

            navLinks: true,
            nowIndicator: true,
            dayMaxEvents: true,

            editable: true,
            droppable: true,
            eventLimit: true,
            selectable: true,
            //事件渲染
            eventRender: function (event, element) {
                element.on("dblclick", function () {
                    if (!confirm(event.title + "事件将被移除，确认吗?")) {
                        return false;
                    }
                })
            },
            drop: function (date) {
                $this.onDrop($(this), date);
            },
            eventDragStart: function () {
                $("#trash-calendar").addClass("trash-calendar-active");
            },
            eventDragStop: function (event, jsEvent, ui, view) {
                $("#trash-calendar").removeClass("trash-calendar-active");
            },
            eventDrop: function (event, delta, revertFunc, jsEvent, ui, view) {
                if (!confirm(event.title + "事件将被放置在时间：" + event.start.format() + "，确认改变吗?")) {
                    revertFunc();
                }
            },
            select: function (start, end, allDay) {
                $this.onSelect(start, end, allDay);
            },
            eventClick: function (calEvent, jsEvent, view) {
                $("#kaoyan-modal").modal("show");
                $this.onEventClick(calEvent, jsEvent, view);
            }

        });

        this.$saveCategoryBtn.on('click', function () {
            var categoryName = $this.$categoryForm.find("input[name='category-name']").val();
            var categoryColor = $this.$categoryForm.find("select[name='category-color']").val();
            if (categoryName !== null && categoryName.length != 0) {
                $this.$extEvents.append('<div class="m-15 external-event bg-' + categoryColor + '" data-class="bg-' + categoryColor + '" style="position: relative;"><i class="fa fa-hand-o-right"></i>' + categoryName + '</div>')
                $this.enableDrag();
            }

        });
    },

        $.CalendarApp = new CalendarApp, $.CalendarApp.Constructor = CalendarApp

}(window.jQuery), function ($) {
    "use strict";
    $.CalendarApp.init()
}(window.jQuery);