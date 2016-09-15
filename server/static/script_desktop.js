var event_id_counter = 0;

$(document).ready(function() {
	setInterval (function() {
		$.getJSON("http://130.237.3.216:5000/getEventsFrom/42/" + event_id_counter, function(result) {
			if (result.length > 0) {
				pillcountGoTo(result[result.length - 1].pillCount);
				event_id_counter = result[result.length - 1].id;
				handleNewEvents(result);
			}
		});
	}, 2000);
});

function goToUnitFromBottom(units, animDuration) {
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

function goToUnitFromTop(units, animDuration) {
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

function goToTenFromBottom(tens, animDuration) {
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

function goToTenFromTop(tens, animDuration) {
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

function addEvent(time, event) {
	var elem = $('<div/>', {'class' : 'history_row'}).append(
			$('<div/>', {'class' : 'history_time', text : time})
			).append(
			$('<div/>', {'class' : 'history_event', text : event})
			);
	if ($('.history_row').first().css('background-color') != 'rgb(161, 186, 186)')
		elem.css({'background-color' : 'rgb(161, 186, 186)'});
	elem.css({display : 'none'});
	$('.history_title').after(elem);
	elem.slideDown();
}

function pillcountGoTo(n) {
	var units = parseInt($('.pillnumber_units').text());
	var tens = parseInt($('.pillnumber_tens').text());
	var current = units + tens * 10;
	if (n > current) {
		// Animation from top
		if (n%10 != current%10)
			goToUnitFromTop(n%10, 400);
		if (Math.floor(n/10) != Math.floor(current/10))
			goToTenFromTop(Math.floor(n/10), 400);
	} else {
		// Animation from bottom
		if (n%10 != current%10)
			goToUnitFromBottom(n%10, 400);
		if (Math.floor(n/10) != Math.floor(current/10))
			goToTenFromBottom(Math.floor(n/10), 400);
	}
}

function handleNewEvents(eventList) {
	for (var i = 0; i < eventList.length; i++) {
		addEvent(formatTime(eventList[i].time), formatPillDelta(eventList[i].pillDelta));
	}
}

function formatTime(time) {
	var d = new Date(time*1000);
	var hours = d.getHours();
	var minutes = d.getMinutes();
	var result = "";
	if(hours < 10)
		result = "0" + hours;
	else
		result = result + hours;
	if (minutes < 10) 
		result = result + ":0" + minutes;
	else
		result = result + ":" + minutes;

	return result;
}

function formatPillDelta(pillDelta) {
	var result = "";
	if (pillDelta < 0)
		result = "Taken " + (-pillDelta);
	else
		result = "Loaded " + pillDelta;
	result = result + " pill";
	if (pillDelta == 1 || pillDelta == -1)
		result = result + ".";
	else
		result = result + "s.";

	return result;
}