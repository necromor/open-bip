'use strict';

let panel_article = (function () {
	
	const titleField = document.getElementById('title');
	const linkField = document.getElementById('link');
	
	function registerCreateLinkEvent() {
		titleField.addEventListener('keyup', showCreatedLink);
	}
	
	function showCreatedLink() {
		linkField.value = crateLinkFromTitle(titleField.value);
	}
	
	function crateLinkFromTitle(title) {
		return "aaa";
	}
	
	return {
		registerCreateLinkEvent: registerCreateLinkEvent
	}
	
})();