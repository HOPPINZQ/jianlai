$( document ).ready(function() {
    "use strict";

	new Chartist.Line('.ct-chart-1', {
	  labels: ['周一', '周二', '周三', '周四', '周五'],
	  series: [
		[12, 9, 7, 8, 5],
		[2, 1, 3.5, 7, 3],
		[1, 3, 4, 5, 6]
	  ]
	}, {
	  fullWidth: true,
	  chartPadding: {
		right: 40
	  }
	});
	

	var chart = new Chartist.Line('.ct-chart-2', {
	  labels: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16],
	  series: [
		[5, 5, 10, 8, 7, 5, 4, null, null, null, 10, 10, 7, 8, 6, 9],
		[10, 15, null, 12, null, 10, 12, 15, null, null, 12, null, 14, null, null, null],
		[null, null, null, null, 3, 4, 1, 3, 4,  6,  7,  9, 5, null, null, null],
		[{x:3, y: 3},{x: 4, y: 3}, {x: 5, y: undefined}, {x: 6, y: 4}, {x: 7, y: null}, {x: 8, y: 4}, {x: 9, y: 4}]
	  ]
	}, {
	  fullWidth: true,
	  chartPadding: {
		right: 10
	  },
	  low: 0
	});
	

	var chart = new Chartist.Line('.ct-chart-3', {
	  labels: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16],
	  series: [
		[5, 5, 10, 8, 7, 5, 4, null, null, null, 10, 10, 7, 8, 6, 9],
		[10, 15, null, 12, null, 10, 12, 15, null, null, 12, null, 14, null, null, null],
		[null, null, null, null, 3, 4, 1, 3, 4,  6,  7,  9, 5, null, null, null],
		[{x:3, y: 3},{x: 4, y: 3}, {x: 5, y: undefined}, {x: 6, y: 4}, {x: 7, y: null}, {x: 8, y: 4}, {x: 9, y: 4}]
	  ]
	}, {
	  fullWidth: true,
	  chartPadding: {
		right: 10
	  },
	  lineSmooth: Chartist.Interpolation.cardinal({
		fillHoles: true,
	  }),
	  low: 0
	});
	
	new Chartist.Line('.ct-chart-4', {
	  labels: [1, 2, 3, 4, 5, 6, 7, 8],
	  series: [
		[5, 9, 7, 8, 5, 3, 5, 4]
	  ]
	}, {
	  low: 0,
	  showArea: true
	});
	
	new Chartist.Line('.ct-chart-5', {
	  labels: [1, 2, 3, 4, 5, 6, 7, 8],
	  series: [
		[1, 2, 3, 1, -2, 0, 1, 0],
		[-2, -1, -2, -1, -2.5, -1, -2, -1],
		[0, 0, 0, 1, 2, 2.5, 2, 1],
		[2.5, 2, 1, 0.5, 1, 0.5, -1, -2.5]
	  ]
	}, {
	  high: 3,
	  low: -3,
	  showArea: true,
	  showLine: false,
	  showPoint: false,
	  fullWidth: true,
	  axisX: {
		showLabel: false,
		showGrid: false
	  }
	});
	
	var chart = new Chartist.Line('.ct-chart-6', {
	  labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
	  series: [
		[12, 9, 7, 8, 5, 4, 6, 2, 3, 3, 4, 6],
		[4,  5, 3, 7, 3, 5, 5, 3, 4, 4, 5, 5],
		[5,  3, 4, 5, 6, 3, 3, 4, 5, 6, 3, 4],
		[3,  4, 5, 6, 7, 6, 4, 5, 6, 7, 6, 3]
	  ]
	}, {
	  low: 0
	});

	var seq = 0,
	  delays = 80,
	  durations = 500;

	chart.on('created', function() {
	  seq = 0;
	});

	chart.on('draw', function(data) {
	  seq++;

	  if(data.type === 'line') {
		data.element.animate({
		  opacity: {
			begin: seq * delays + 1000,
			dur: durations,
			from: 0,
			to: 1
		  }
		});
	  } else if(data.type === 'label' && data.axis === 'x') {
		data.element.animate({
		  y: {
			begin: seq * delays,
			dur: durations,
			from: data.y + 100,
			to: data.y,
			easing: 'easeOutQuart'
		  }
		});
	  } else if(data.type === 'label' && data.axis === 'y') {
		data.element.animate({
		  x: {
			begin: seq * delays,
			dur: durations,
			from: data.x - 100,
			to: data.x,
			easing: 'easeOutQuart'
		  }
		});
	  } else if(data.type === 'point') {
		data.element.animate({
		  x1: {
			begin: seq * delays,
			dur: durations,
			from: data.x - 10,
			to: data.x,
			easing: 'easeOutQuart'
		  },
		  x2: {
			begin: seq * delays,
			dur: durations,
			from: data.x - 10,
			to: data.x,
			easing: 'easeOutQuart'
		  },
		  opacity: {
			begin: seq * delays,
			dur: durations,
			from: 0,
			to: 1,
			easing: 'easeOutQuart'
		  }
		});
	  } else if(data.type === 'grid') {
		var pos1Animation = {
		  begin: seq * delays,
		  dur: durations,
		  from: data[data.axis.units.pos + '1'] - 30,
		  to: data[data.axis.units.pos + '1'],
		  easing: 'easeOutQuart'
		};

		var pos2Animation = {
		  begin: seq * delays,
		  dur: durations,
		  from: data[data.axis.units.pos + '2'] - 100,
		  to: data[data.axis.units.pos + '2'],
		  easing: 'easeOutQuart'
		};

		var animations = {};
		animations[data.axis.units.pos + '1'] = pos1Animation;
		animations[data.axis.units.pos + '2'] = pos2Animation;
		animations['opacity'] = {
		  begin: seq * delays,
		  dur: durations,
		  from: 0,
		  to: 1,
		  easing: 'easeOutQuart'
		};

		data.element.animate(animations);
	  }
	});

	chart.on('created', function() {
	  if(window.__exampleAnimateTimeout) {
		clearTimeout(window.__exampleAnimateTimeout);
		window.__exampleAnimateTimeout = null;
	  }
	  window.__exampleAnimateTimeout = setTimeout(chart.update.bind(chart), 12000);
	});
	
	var chart = new Chartist.Line('.ct-chart-7', {
	  labels: ['周一', '周二', '周三', '周四', '周五', '周六'],
	  series: [
		[1, 5, 2, 5, 4, 3],
		[2, 3, 4, 8, 1, 2],
		[5, 4, 3, 2, 1, 0.5]
	  ]
	}, {
	  low: 0,
	  showArea: true,
	  showPoint: false,
	  fullWidth: true
	});

	chart.on('draw', function(data) {
	  if(data.type === 'line' || data.type === 'area') {
		data.element.animate({
		  d: {
			begin: 2000 * data.index,
			dur: 2000,
			from: data.path.clone().scale(1, 0).translate(0, data.chartRect.height()).stringify(),
			to: data.path.clone().stringify(),
			easing: Chartist.Svg.Easing.easeOutQuint
		  }
		});
	  }
	});
	
	var chart = new Chartist.Pie('.ct-chart-8', {
	  series: [10, 20, 50, 20, 5, 50, 15],
	  labels: [1, 2, 3, 4, 5, 6, 7]
	}, {
	  donut: true,
	  showLabel: false
	});

	chart.on('draw', function(data) {
	  if(data.type === 'slice') {
		var pathLength = data.element._node.getTotalLength();

		data.element.attr({
		  'stroke-dasharray': pathLength + 'px ' + pathLength + 'px'
		});

		var animationDefinition = {
		  'stroke-dashoffset': {
			id: 'anim' + data.index,
			dur: 1000,
			from: -pathLength + 'px',
			to:  '0px',
			easing: Chartist.Svg.Easing.easeOutQuint,
			fill: 'freeze'
		  }
		};
		if(data.index !== 0) {
		  animationDefinition['stroke-dashoffset'].begin = 'anim' + (data.index - 1) + '.end';
		}
		data.element.attr({
		  'stroke-dashoffset': -pathLength + 'px'
		});
		data.element.animate(animationDefinition, false);
	  }
	});

	chart.on('created', function() {
	  if(window.__anim21278907124) {
		clearTimeout(window.__anim21278907124);
		window.__anim21278907124 = null;
	  }
	  window.__anim21278907124 = setTimeout(chart.update.bind(chart), 10000);
	});

	
	var data = {
	  labels: ['W1', 'W2', 'W3', 'W4', 'W5', 'W6', 'W7', 'W8', 'W9', 'W10'],
	  series: [
		[1, 2, 4, 8, 6, -2, -1, -4, -6, -2]
	  ]
	};

	var options = {
	  high: 10,
	  low: -10,
	  axisX: {
		labelInterpolationFnc: function(value, index) {
		  return index % 2 === 0 ? value : null;
		}
	  }
	};

	new Chartist.Bar('.ct-chart-9', data, options);

	
	new Chartist.Bar('.ct-chart-10', {
	  labels: ['Q1', 'Q2', 'Q3', 'Q4'],
	  series: [
		[800000, 1200000, 1400000, 1300000],
		[200000, 400000, 500000, 300000],
		[100000, 200000, 400000, 600000]
	  ]
	}, {
	  stackBars: true,
	  axisY: {
		labelInterpolationFnc: function(value) {
		  return (value / 1000) + 'k';
		}
	  }
	}).on('draw', function(data) {
	  if(data.type === 'bar') {
		data.element.attr({
		  style: 'stroke-width: 30px'
		});
	  }
	});
	

	new Chartist.Pie('.ct-gauge-chart', {
	  series: [20, 10, 30, 40]
	}, {
	  donut: true,
	  donutWidth: 60,
	  startAngle: 270,
	  total: 200,
	  low:0,
	  showLabel: false
	});

	
	new Chartist.Bar('.ct-chart-11', {
	  labels: ['周一', '周二', '周三', '周四', '周五', '周六','周日'],
	  series: [
		[5, 4, 3, 7, 5, 10, 3],
		[3, 2, 9, 5, 4, 6, 4]
	  ]
	}, {
	  seriesBarDistance: 10,
	  reverseData: true,
	  horizontalBars: true,
	  axisY: {
		offset: 70
	  }
	});
	
	new Chartist.Bar('.ct-chart-12', {
	  labels: ['物品 1', '物品 2', '物品 3', '物品 4'],
	  series: [
		[5, 4, 3, 7],
		[3, 2, 9, 5],
		[1, 5, 8, 4],
		[2, 3, 4, 6],
		[4, 1, 2, 1]
	  ]
	}, {
	  stackBars: true,
	  axisX: {
		labelInterpolationFnc: function(value) {
		  return value.split(/\s+/).map(function(word) {
			return word[0];
		  }).join('');
		}
	  },
	  axisY: {
		offset: 20
	  }
	}, [
	  ['screen and (min-width: 400px)', {
		reverseData: true,
		horizontalBars: true,
		axisX: {
		  labelInterpolationFnc: Chartist.noop
		},
		axisY: {
		  offset: 60
		}
	  }],
	  ['screen and (min-width: 800px)', {
		stackBars: false,
		seriesBarDistance: 10
	  }],
	  ['screen and (min-width: 1000px)', {
		reverseData: false,
		horizontalBars: false,
		seriesBarDistance: 15
	  }]
	]);
}); 
