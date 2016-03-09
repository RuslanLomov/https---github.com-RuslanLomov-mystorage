$(document).ready(function() {
	$('input[name=login]').keypress(function(e) {
		if (e.which === 32)
			return false;
	});
	$('input[name=password]').keypress(function(e) {
		if (e.which === 32)
			return false;
	});
});