$(function () {
    "use strict";
    $(".myadmin-alert .closed").click(function(event) {
        $(this).parents(".myadmin-alert").fadeToggle(350);
        return false;
    });
    $(".myadmin-alert-click").click(function(event) {
        $(this).fadeToggle(350);
        return false;
    });
    $(".showtop").click(function() {
        $(".alerttop").fadeToggle(350);
    });
    $(".showtop2").click(function() {
        $(".alerttop2").fadeToggle(350);
    });
    $(".showbottom").click(function() {
        $(".alertbottom").fadeToggle(350);
    });
    $(".showbottom2").click(function() {
        $(".alertbottom2").fadeToggle(350);
    });
    $("#showtopleft").click(function() {
        $("#alerttopleft").fadeToggle(350);
    });
    $("#showtopright").click(function() {
        $("#alerttopright").fadeToggle(350);
    });
    $("#showbottomleft").click(function() {
        $("#alertbottomleft").fadeToggle(350);
    });
    $("#showbottomright").click(function() {
        $("#alertbottomright").fadeToggle(350);
    });
	
  }); 