'use strict';

let test_module = (function () {
	
	const testButton = document.getElementById('db-conn-test');
	const testResult = document.getElementById('db-conn-result');
	const testDBLink = '/api/test/connection';
	
	function registerEvents() {		
		if (testButton) {
			testButton.addEventListener('click', testDBConnection);
		}
	}
	
	function testDBConnection() {	
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : testDBLink,
			timeout : 100000,
			success : function(data) {
				displayMessage('alert-success', data.message);				
			},
			error : function(data) {
				displayMessage('alert-danger', data.message);
			}
		});
	}
	
	function displayMessage(type, message) {
		testResult.classList.remove('alert-primary');
		testResult.classList.remove('alert-success');
		testResult.classList.remove('alert-danger');
		
		testResult.innerText = message;
		testResult.classList.add(type);
	}
	
	return {
		registerEvents: registerEvents
	}

})();