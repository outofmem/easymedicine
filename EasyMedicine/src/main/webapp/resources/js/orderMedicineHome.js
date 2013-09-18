/**
 * This javascript file contains the jquery code
 * for order medicine home page. Please remember the following notes:
 * 1) The server is communicated only to send/get data via AJAX
 * 2) The different part(divs) of this page are kept in a single page.They are
 *    toggled depending on the server response.
 * 3) Please put a proper comment in each and every section for future maintainability
 * 
 * @author pribiswas
 */
$(function(){
	//the section wrapper (includes all sections)
	var orderMedicineSectionWrapper   = $('#orderMedicineSectionWrapper'),	 
	//the active section value is set in hidden field
	activeSectionVal = $('#activeSection').val();

	//Hide those sections who does not have a class equal to the activeSectionVal.
	//At a time just one section is active and visible
	orderMedicineSectionWrapper.children('div').each(function(i){
	    var theSectionDiv    = $(this);
	    //solve the inline display none problem when using fadeIn/fadeOut
	    if(!theSectionDiv.hasClass(activeSectionVal)) {
	    	theSectionDiv.hide();	
	    } else {
	    	theSectionDiv.addClass('active');
	    }	    
	});
	//the current section is the one with class active
	var activeSection    = orderMedicineSectionWrapper.children('div.active'),   
	//the switch section links
	linkSections       = orderMedicineSectionWrapper.find('.linkSection');
	//attach the click handler with the section switch links
	linkSections.bind('click',function(e){
		var link   = $(this);
	    var target  = link.attr('rel');
	    toggleSection(target);
	   	e.preventDefault();
	});
	
	
	//--------------------------------------------------------------------------------------------
	//The below section is for reusable functions. Please add all these type of functions below.
	//--------------------------------------------------------------------------------------------
	
	/**
	 * This function is getting reused to toggle between sections
	 * @param target The name of the targetted div
	 */
	function toggleSection(target) {
		activeSection.fadeOut(400,function(){
	        //remove class "active" from current form
	        activeSection.removeClass('active');
	        //new current form
	        activeSection= orderMedicineSectionWrapper.children('div.'+target);
	        //animate the wrapper
	        orderMedicineSectionWrapper.stop()
	                     .animate({
	                        //as of now nothing needed
	                     },500,function(){
	                        //new form gets class "active"
	                        activeSection.addClass('active');
	                        //show the new form
	                        activeSection.fadeIn(400);
	                     });
	    });
	}
});