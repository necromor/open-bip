'use strict';

let panel_sandbox = (function () {

	const sandboxSave = document.getElementById('sandboxSaveButton');
	const sandboxSaveLink = '/api/sandbox/save';
	const messageDiv = document.getElementById('ajax-message');
	const showMessageTime = 4000;
	
	const token = $("meta[name='_csrf']").attr("content"); 
	const header = $("meta[name='_csrf_header']").attr("content");
	
	
	function registerSandboxEvent() {		
		if (sandboxSave) {
			sandboxSave.addEventListener('click', saveSandbox);
		}
	}
	
	function saveSandbox() {
		let sandbox = {};
		sandbox['title'] = $("#sandboxTitle").val();
		sandbox['content'] = $("#sandboxContent").val();
		sandbox['link'] = $("#sandboxLink").val();
		
		$.ajax({
			type : "PUT",
			beforeSend: function(request) {
			    request.setRequestHeader(header, token);
			},
			contentType : "application/json",
			url : sandboxSaveLink,
			data : JSON.stringify(sandbox),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				showResult(data);				
			},
			error : function(e) {
				showMessage('alert-danger', data.message);
			},
			done : function(e) {
				console.log("DONE");
			}
		});
	}
	
	function showResult(data) {
		let resultClass = 'alert-success';
		if (data.error) {
			resultClass = 'alert-danger';
		}
		showMessage(resultClass, data.message);
	}
	
	function showMessage(type, message) {
		sandboxSave.disabled = true;
		messageDiv.classList.add('show');
		messageDiv.classList.add(type);
		messageDiv.innerText = message;
		window.setTimeout(hideMessage, showMessageTime);
	}
	
	function hideMessage() {
		messageDiv.classList.remove('show');
		messageDiv.classList.remove('alert-success');
		messageDiv.classList.remove('alert-danger');
		sandboxSave.disabled = false;
	}

	
	return {
		registerSandboxEvent: registerSandboxEvent
	}
	
})();