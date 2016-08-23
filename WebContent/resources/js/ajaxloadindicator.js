/**
 * Show load indicator while Ajax results are loading
 */


function onAjaxLoad(data) {
	if (data.status == "begin") {
		clearMessages();
		showSpinner();
	} else {
		hideSpinner();
	}
}

function clearMessages() {
	var messages = document.getElementsByClassName('errorMsg');
	for (var i = 0; i < messages.length; i++) {
		messages[i].innerHTML = "";
	}
}

function showSpinner() {
	document.getElementById('ajaxloader').style.display = "block";
}

function hideSpinner() {
	document.getElementById('ajaxloader').style.display = "none";
}