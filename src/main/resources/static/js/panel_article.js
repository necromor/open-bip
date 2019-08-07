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
		const special = [' ', 'ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'ź', 'ż', '#'];
	    const plain = ['-', 'a', 'c', 'e', 'l', 'n', 'o', 's', 'z', 'z', ''];
	    
	    let str = title.toLowerCase();

        special.forEach((value, i) =>
            str = str.replace(new RegExp(special[i], 'g'), plain[i])
        );

        return str;
	}
	
	return {
		registerCreateLinkEvent: registerCreateLinkEvent
	}
	
})();