$(document).ready(function() {
	var w = $('.menu').width();
	$('.menu').css({left : -w});
});

function openMenu() {
	$('.menu').animate({left : 0});
	$('.overlay').css({display : 'block'});
	$('.overlay').animate({opacity : 0.9});
}

function closeMenu() {
	var w = $('.menu').width();
	$('.menu').animate({left : -w});
	$('.overlay').animate({opacity : 0.0}, function() {
		$('.overlay').css({display : 'none'});
	});
}