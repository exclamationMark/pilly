$(document).ready(function() {
	var w = $('.menu').width();
	$('.menu').css({left : -w});
});

function openMenu() {
	$('.menu').animate({left : 0});
}

function closeMenu() {
	var w = $('.menu').width();
	$('.menu').animate({left : -w});
}