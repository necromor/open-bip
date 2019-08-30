'use strict';

panel_article.registerCreateLinkEvent();
panel_sandbox.registerSandboxEvent();

$( function() {
	  $( ".sortable-list" ).sortable({
	      stop: function() {
	    	  const name = this.id;
	    	  const links = [];
	    	    $(this).find('li a.link-list-element-link').each(function(){
	    	    	links.push(panel_article.extractArticleLink(this.href))
	    	    });
	    	  panel_article.saveNewPositions(name, links);
	      }
	    });
  $( ".sortable-list" ).disableSelection();
} );