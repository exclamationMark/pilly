$(document).ready(function() {
	/*setInterval (function() {
		$.getJSON("http://127.0.0.1:5000/candyCounter/updates", function(result) {
			// Do something
		});
	}, 2000);*/
});

function addOneUnit(animDuration) {
	var units = parseInt($('.pillnumber_units').text()) + 1;
	if (units > 9)
		units = 0;
	var destination = $('.pillnumber_units').offset();
	var newElement = $('<div class="pillnumber_units"></div>')
					.text(units).css({top : destination.top + 110,
									left : destination.left,
									opacity : '0'});
	$('.pillnumber_units').after(newElement);
	$('.pillnumber_units').first().animate({top : '-=60pt', opacity : '0'}, {queue : false, duration : animDuration});
	newElement.animate({top : '-=110', opacity : '1'}, {queue : false,
														duration : animDuration,
														complete : removeFirstUnit});
}

function subtractOneUnit(animDuration) {
	var units = parseInt($('.pillnumber_units').text()) - 1;
	if (units < 0)
		units = 9;
	var destination = $('.pillnumber_units').offset();
	var newElement = $('<div class="pillnumber_units"></div>')
					.text(units).css({top : destination.top - 60,
									left : destination.left,
									opacity : '0'});
	$('.pillnumber_units').after(newElement);
	$('.pillnumber_units').first().animate({top : '+=110pt', opacity : '0'}, {queue : false, duration : animDuration});
	newElement.animate({top : '+=60', opacity : '1'}, {queue : false,
														duration : animDuration,
														complete : removeFirstUnit});
}

function addOneTen(animDuration) {
	var tens = parseInt($('.pillnumber_tens').text()) + 1;
	if (tens > 9)
		tens = 0;
	var destination = $('.pillnumber_tens').offset();
	var newElement = $('<div class="pillnumber_tens"></div>')
					.text(tens).css({top : destination.top + 110,
									left : destination.left,
									opacity : '0'});
	$('.pillnumber_tens').after(newElement);
	$('.pillnumber_tens').first().animate({top : '-=60pt', opacity : '0'}, {queue : false, duration : animDuration});
	newElement.animate({top : '-=110', opacity : '1'}, {queue : false,
														duration : animDuration,
														complete : removeFirstTen});
}

function subtractOneTen(animDuration) {
	var tens = parseInt($('.pillnumber_tens').text()) - 1;
	if (tens < 0)
		tens = 9;
	var destination = $('.pillnumber_tens').offset();
	var newElement = $('<div class="pillnumber_tens"></div>')
					.text(tens).css({top : destination.top - 60,
									left : destination.left,
									opacity : '0'});
	$('.pillnumber_tens').after(newElement);
	$('.pillnumber_tens').first().animate({top : '+=110pt', opacity : '0'}, {queue : false, duration : animDuration});
	newElement.animate({top : '+=60', opacity : '1'}, {queue : false,
														duration : animDuration,
														complete : removeFirstTen});
}

function removeFirstUnit() {
	$('.pillnumber_units').first().remove();
}

function removeFirstTen() {
	$('.pillnumber_tens').first().remove();
}

function addOne() {
	var units = parseInt($('.pillnumber_units').text());
	addOneUnit(200);
	if (units == 9)
		addOneTen(200);
}

function subtractOne() {
	var units = parseInt($('.pillnumber_units').text());
	subtractOneUnit(200);
	if (units == 0)
		subtractOneTen(200);
}

$(document).click(function() {
  subtractOne();
});