'use strict';

let panel_article = (function () {
	
	const titleField = document.getElementById('title');
	const linkField = document.getElementById('link');
	const sandboxSave = document.getElementById('sandboxSaveButton');
	const sandboxSaveLink = 'http://localhost:8080/api/sandbox/save';
	const messageDiv = document.getElementById('ajax-message');
	const showMessageTime = 4000;
	
	
	function registerCreateLinkEvent() {
		if (titleField) {
			titleField.addEventListener('keyup', showCreatedLink);
		}
		
		if (sandboxSave) {
			sandboxSave.addEventListener('click', saveSandbox);
		}
	}
	
	function showCreatedLink() {
		linkField.value = crateLinkFromTitle(titleField.value);
	}
	
	function crateLinkFromTitle(title) {
		const special = [' ', 'ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'ź', 'ż', '#'];
	    const plain = ['-', 'a', 'c', 'e', 'l', 'n', 'o', 's', 'z', 'z', ''];
	    
	    let str = title.toLowerCase();

        special.forEach((value, i) =>
            str = str.replace(new RegExp(special[i], 'g'), plain[i])
        );

        return str;
	}
	
	function saveSandbox() {
		let sandbox = {};
		sandbox['title'] = $("#sandboxTitle").val();
		sandbox['content'] = $("#sandboxContent").val();
		sandbox['link'] = $("#sandboxLink").val();
		
		$.ajax({
			type : "PUT",
			contentType : "application/json",
			url : sandboxSaveLink,
			data : JSON.stringify(sandbox),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				showResult(data);				
				console.log("SUCCESS: ", data);
			},
			error : function(e) {
				showMessage('alert-danger', data.message);
				console.log("ERROR: ", e);
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
		registerCreateLinkEvent: registerCreateLinkEvent
	}
	
})();