$(document).ready(function() {
	var w = $('.menu').width();
	$('.menu').css({left : -w});
	$('.table-row:even').css("background-color", "#A1BABA");

	// newEvent function test
	$('.pillnumber').click(function() {
		newEvent('dummy1','dummy2');
	});
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

function newEvent(timestamp, eventstring) {
	var htmlString = '<div class="table-row">\
						<div class="table-cell">' + timestamp + '</div>\
						<div class="table-cell">' + eventstring + '</div>\
					</div>';
	var newElement = $(htmlString).css({display : 'none'});
	newElement.css({backgroundColor : $('.table-row:eq(1)').css('background-color')});
	$('.table-row').first().before(newElement);
	$('.table-row').first().slideDown();
	$('.table-row').last().slideUp(function() {
		$('.table-row').last().remove();
	});
}

