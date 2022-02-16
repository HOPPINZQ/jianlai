$(function () {

    $(".knob").knob({
      draw: function () {

        if (this.$.data('skin') == 'tron') {

          var a = this.angle(this.cv)
              , sa = this.startAngle
              , sat = this.startAngle
              , ea
              , eat = sat + a
              , r = true;

          this.g.lineWidth = this.lineWidth;

          this.o.cursor
          && (sat = eat - 0.3)
          && (eat = eat + 0.3);

          if (this.o.displayPrevious) {
            ea = this.startAngle + this.angle(this.value);
            this.o.cursor
            && (sa = ea - 0.3)
            && (ea = ea + 0.3);
            this.g.beginPath();
            this.g.strokeStyle = this.previousColor;
            this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sa, ea, false);
            this.g.stroke();
          }

          this.g.beginPath();
          this.g.strokeStyle = r ? this.o.fgColor : this.fgColor;
          this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sat, eat, false);
          this.g.stroke();

          this.g.lineWidth = 2;
          this.g.beginPath();
          this.g.strokeStyle = this.o.fgColor;
          this.g.arc(this.xy, this.xy, this.radius - this.lineWidth + 1 + this.lineWidth * 2 / 3, 0, 2 * Math.PI, false);
          this.g.stroke();

          return false;
        }
      }
    });

	
	$("#sparkline5").sparkline([18, 22, 38, 20], {
        type: 'pie',
        height: '100',
        sliceColors: ['#3aa0dc', '#06d79c', '#745af2', '#ff4c52'],
        highlightLighten: 1.1
    }); 
	
    $(".sparkline").each(function () {
      var $this = $(this);
      $this.sparkline('html', $this.data());
    });

    drawDocSparklines();
    drawMouseSpeedDemo();

  });
  function drawDocSparklines() {

    $('#compositebar').sparkline('html', {type: 'bar', barColor: '#aaf'});
    $('#compositebar').sparkline([7,3,10,5,4,8,6,4,2,4,5,3,4,5],
        {composite: true, fillColor: false, lineColor: '#ff4c52'});

    $('.sparkline-1').sparkline();

    $('.largeline').sparkline('html',
        {type: 'line', height: '2.5em', width: '4em'});

    $('#linecustom').sparkline('html',
        {
          height: '1.5em', width: '8em', lineColor: '#ff4c52', fillColor: '#faa700',
          minSpotColor: false, maxSpotColor: false, spotColor: '#745af2', spotRadius: 3
        });

    $('.sparkbar').sparkline('html', {type: 'bar'});

    $('.barformat').sparkline([1, 3, 5, 3, 8], {
      type: 'bar',
      tooltipFormat: '{{value:levels}} - {{value}}',
      tooltipValueLookups: {
        levels: $.range_map({':2': 'Low', '3:6': 'Medium', '7:': 'High'})
      }
    });

    $('.sparktristate').sparkline('html', {type: 'tristate'});
    $('.sparktristatecols').sparkline('html',
        {type: 'tristate', colorMap: {'-2': '#3aa0dc', '2': '#3aa0dc'}});

    $('#compositeline').sparkline('html', {fillColor: false, changeRangeMin: 0, chartRangeMax: 10});
    $('#compositeline').sparkline([7,3,10,5,4,8,6,4,2,4,5,3,4,5],
        {composite: true, fillColor: false, lineColor: '#ff4c52', changeRangeMin: 0, chartRangeMax: 10});

    $('#normalline').sparkline('html',
        {fillColor: false, normalRangeMin: -1, normalRangeMax: 8});
    $('#normalExample').sparkline('html',
        {fillColor: false, normalRangeMin: 80, normalRangeMax: 95, normalRangeColor: '#4f4'});

    $('.discrete1').sparkline('html',
        {type: 'discrete', lineColor: '#3aa0dc', xwidth: 18});
    $('#discrete2').sparkline('html',
        {type: 'discrete', lineColor: '#3aa0dc', thresholdColor: '#ff4c52', thresholdValue: 4});

    $('.sparkbullet').sparkline('html', {type: 'bullet'});

    $('.sparkpie').sparkline('html', {type: 'pie', height: '1.0em'});

    $('.sparkboxplot').sparkline('html', {type: 'box'});
    $('.sparkboxplotraw').sparkline([1, 3, 5, 8, 10, 15, 18],
        {type: 'box', raw: true, showOutliers: true, target: 6});

    $('.boxfieldorder').sparkline('html', {
      type: 'box',
      tooltipFormatFieldlist: ['med', 'lq', 'uq'],
      tooltipFormatFieldlistKey: 'field'
    });

    $('.clickdemo').sparkline();
    $('.clickdemo').bind('sparklineClick', function (ev) {
      var sparkline = ev.sparklines[0],
          region = sparkline.getCurrentRegionFields();
      value = region.y;
      alert("Clicked on x=" + region.x + " y=" + region.y);
    });

    $('.mouseoverdemo').sparkline();
    $('.mouseoverdemo').bind('sparklineRegionChange', function (ev) {
      var sparkline = ev.sparklines[0],
          region = sparkline.getCurrentRegionFields();
      value = region.y;
      $('.mouseoverregion').text("x=" + region.x + " y=" + region.y);
    }).bind('mouseleave', function () {
      $('.mouseoverregion').text('');
    });
  }

  function drawMouseSpeedDemo() {
    var mrefreshinterval = 500;
    var lastmousex = -1;
    var lastmousey = -1;
    var lastmousetime;
    var mousetravel = 0;
    var mpoints = [];
    var mpoints_max = 30;
    $('html').mousemove(function (e) {
      var mousex = e.pageX;
      var mousey = e.pageY;
      if (lastmousex > -1) {
        mousetravel += Math.max(Math.abs(mousex - lastmousex), Math.abs(mousey - lastmousey));
      }
      lastmousex = mousex;
      lastmousey = mousey;
    });
    var mdraw = function () {
      var md = new Date();
      var timenow = md.getTime();
      if (lastmousetime && lastmousetime != timenow) {
        var pps = Math.round(mousetravel / (timenow - lastmousetime) * 1000);
        mpoints.push(pps);
        if (mpoints.length > mpoints_max)
          mpoints.splice(0, 1);
        mousetravel = 0;
        $('#mousespeed').sparkline(mpoints, {width: mpoints.length * 2, tooltipSuffix: ' pixels per second'});
      }
      lastmousetime = timenow;
      setTimeout(mdraw, mrefreshinterval);
    };
    setTimeout(mdraw, mrefreshinterval);
  }