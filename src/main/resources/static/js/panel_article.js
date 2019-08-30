'use strict';

let panel_article = (function () {
	
	const titleField = document.getElementById('title');
	const linkField = document.getElementById('link');
	const mainMenuSymbol = 'main-menu-elements';
	const childrenSymbol = 'children';
	const attachmentsSymbol = 'attachments';
	const mainMenuPositions = '/api/main-menu-positions';
	const articleChildrenPositions = '/api/children-positions/';
	const articleAttachmentsPositions = '/api/attachments-positions/';
	
	function registerCreateLinkEvent() {
		if (titleField) {
			titleField.addEventListener('keyup', showCreatedLink);
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
	
	function extractArticleLink(address) {
		let splitted = address.split('/');
		return splitted[splitted.length - 1];
	}
	
	function saveNewPositions(name, links) {
		const link = returnLinkBasedOnName(name);
		console.log(name);
		console.log(link);
		console.log(links);
	}

	function returnLinkBasedOnName(name) {
		if (name === mainMenuSymbol) {
			return mainMenuPositions;
		} else {
			const splitted = name.split('#');
			if (splitted[1] === childrenSymbol) {
				return articleChildrenPositions + splitted[0];
			} else {
				return articleAttachmentsPositions + splitted[0];
			}
		}
	}
	
	
	return {
		registerCreateLinkEvent: registerCreateLinkEvent,
		extractArticleLink: extractArticleLink,
		saveNewPositions: saveNewPositions
	}
	
})();