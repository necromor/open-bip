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

$('#deleteAttachmentModal').on('show.bs.modal', function (event) {
	const button = $(event.relatedTarget)
	const displayName = button.data('name')
	const articleLink = button.data('a-link')
	const fileName = button.data('link')
	const deleteLink = '/panel/usun/zalacznik/' + fileName + '/' + articleLink;
	const displayData = displayName + ' [' + fileName + ']'
		
	const modal = $(this)
	modal.find('.file-name').text(displayData)
	modal.find('#att-delete-button').attr('href', deleteLink)
})