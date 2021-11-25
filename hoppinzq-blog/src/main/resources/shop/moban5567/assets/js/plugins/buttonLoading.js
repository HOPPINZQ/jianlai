(function ($) {
    var methods = {
            start: function () {
                let loda = getData(this);
                if (loda) loda.start();
            },
            stop: function () {
                let loda = getData(this);
                if (loda) loda.stop();
            }
        },
        getData = function (el) {
            return el.data('loda-button-data');
        },
        setData = function (el, data) {
            el.data('loda-button-data', data);
        },
        buttonLoading = function (element) {
            this.me = element;
            this.$me = $(element);
            if (getData(this.$me)) return;
            setData(this.$me, this);
        };
    buttonLoading.prototype = {
        start: function () {
            this.$me.data("loadingq", this.$me.text().trim()).addClass("animated-bg").addClass("loading-black").attr("disabled", "disabled").html("等待中");
        },
        stop: function () {
            this.$me.removeClass("animated-bg").removeClass("loading-black").removeAttr("disabled").html(this.$me.data("loadingq"));
        }
    };
    $.fn.buttonLoading = function (options) {
        if (methods[options]) {
            return methods[options].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof options === 'object' || !options) {
            return this.each(function () {
                new buttonLoading(this);
                return this;
            });
        } else {
            $.error('方法 ' + options + '不存在');
        }
    };
}(jQuery));