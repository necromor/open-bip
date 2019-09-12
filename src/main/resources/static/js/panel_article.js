'use strict';

let panel_article = (function () {
	
	const showMessageTime = 3000;

	const mainMenuSymbol = 'main-menu-elements';
	const childrenSymbol = 'children';
	const attachmentsSymbol = 'attachments';

	const titleField = document.getElementById('title');
	const linkField = document.getElementById('link');
	const childrenMessage = document.getElementById('ajax-children-message');
	const attachmentsMessage = document.getElementById('ajax-attachments-message');
	const mainMenuMessage = document.getElementById('ajax-main-menu-message');
	const sandboxContentMessage = document.getElementById('ajax-sandbox-content-loaded');
	const articleEditContentField = document.getElementById("content");
	const insertContentButtons = document.getElementsByClassName("ajax-insert-content");
	
	const mainMenuPositions = '/api/article/main-menu-positions';
	const articleChildrenPositions = '/api/article/children-positions/';
	const articleAttachmentsPositions = '/api/article/attachments-positions/';
	const sandboxGetContent = '/api/sandbox/get-content/';
	
	function registerCreateLinkEvent() {
		if (titleField) {
			titleField.addEventListener('keyup', showCreatedLink);
		}

		Array.from(insertContentButtons).forEach( (button) => {
			button.addEventListener('click', insertSandboxContent);
		});
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
	
	function extractArticleLink(address) {
		let splitted = address.split('/');
		return splitted[splitted.length - 1];
	}
	
	function saveNewPositions(name, links) {
		const link = returnLinkBasedOnName(name);
		
		createAjax(link[0], links, link[1]);
	}
	
	function createAjax(address, data, type) {
		$.ajax({
			type : "PUT",
			contentType : "application/json",
			url : address,
			data : JSON.stringify(data),
			dataType : 'json',
			timeout : 100000,
			success : function(result) {
				showResult(result, type);				
			},
			error : function(e) {
				showResult(result, type);
			},
			done : function(e) {
				console.log("DONE");
			}
		});
	}
	
	function showResult(data, element) {
		let resultClass = 'alert-success';
		if (data.error) {
			resultClass = 'alert-danger';
		}
		showMessage(resultClass, data.message, element);
	}
	
	function showMessage(type, message, element) {
		element.classList.add('show');
		element.classList.add(type);
		element.innerText = message;
		window.setTimeout(hideMessage, showMessageTime, element);
	}
	
	function hideMessage(element) {
		element.classList.remove('show');
		element.classList.remove('alert-success');
		element.classList.remove('alert-danger');
	}

	function returnLinkBasedOnName(name) {
		if (name === mainMenuSymbol) {
			return [mainMenuPositions, mainMenuMessage];
		} else {
			const splitted = name.split('#');
			if (splitted[1] === childrenSymbol) {
				return [articleChildrenPositions + splitted[0], childrenMessage];
			} else {
				return [articleAttachmentsPositions + splitted[0], attachmentsMessage];
			}
		}
	}
	
	function insertSandboxContent() {
		const address = sandboxGetContent + this.id;
		fetchSandboxContentAjax(address);
	}
	
	function fetchSandboxContentAjax(address) {
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : address,
			timeout : 100000,
			success : function(result) {
				showSandboxResult(result);				
			},
			error : function(e) {
				showSandboxResult(result);
			}
		});
	}
	
	function showSandboxResult(result) {
		showResult(result, sandboxContentMessage);
		//articleEditContentField.value = result.content;
		tinyMCE.activeEditor.setContent(result.content);
	}
	
	
	return {
		registerCreateLinkEvent: registerCreateLinkEvent,
		extractArticleLink: extractArticleLink,
		saveNewPositions: saveNewPositions
	}
	
})();