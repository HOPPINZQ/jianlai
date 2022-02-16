$(function () {
  'use strict';
	
	$('#date').formatter({
	  'pattern': '{{99}}/{{99}}/{{9999}}',
	  'persistent': true
	});
	
	$('#date2').formatter({
	  'pattern': '{{9999}}-{{99}}-{{99}}',
	  'persistent': true
	});
	
	$('#time').formatter({
	  'pattern': '{{99}}:{{99}}',
	  'persistent': true
	});
	
	$('#date-time').formatter({
	  'pattern': '{{99}}/{{99}}/{{9999}} {{99}}:{{99}}',
	  'persistent': true
	});
	
	$('#phone').formatter({
	  'pattern': '({{999}}) {{999}}-{{9999}}',
	  'persistent': true
	});
	
	$('#phone2').formatter({
	  'pattern': '+1 {{999}}-{{999}}-{{999}}',
	  'persistent': true
	});
	
	$('#percent').formatter({
	  'pattern': '%{{99}}.{{99}}',
	  'persistent': true
	});
	
	$('#username').formatter({
	  'pattern': '{{aaaaaaaa}}',
	  'persistent': true
	});
	
	$('#price').formatter({
	  'pattern': '￥ {{999}}.{{99}}',
	  'persistent': true
	});

	$('#creditcard').formatter({
	  'pattern': '{{9999}}-{{9999}}-{{9999}}-{{9999}}',
	  'persistent': true
	});

	$('#ssn').formatter({
	  'pattern': '{{999}}-{{99}}-{{9999}}',
	  'persistent': true
	});

	$('#productkey').formatter({
	  'pattern': 'P{{a}} {{999}} {{9999}}',
	  'persistent': true
	});
	
});