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
	
	//enable the medicine data table
	var medicineTable = $("#medicineTable").dataTable({
		"sDom" : '<"H"rt'
	});
	//add the first mandatory row
	addMedicineRow(false);
	//attach event handler with add medicine button
	$("#addMedicine").click(function(e){
		addMedicineRow(true);
	});
	
	//attach event handler for delete link beside each row for medicine
	$("#medicineTable").delegate("a.delete", "click", function( e ) {
        e.preventDefault();
        //finding the grandparent of the link which is a row which needs to be removed
        var tr = $(this).parent().parent()[0];
        medicineTable.fnDeleteRow(tr);
    });
	
	var customer = undefined;
	//Attach on change event handler of the customer e-mail id
	$("#customerEmailId").change(function() {
		var param = "emailId="+$(this).val();
		$.ajax({ url: "./orderMedicine/getCustomer",
	        data: param,
	        type: "GET",
	        success: function(response){
	           	if(response) {
	           		customer = response;
	           		$("#customerInfoForm").dialog("open");
	           	}
	        }
        });
	});
	
	//attach submit event handler for order medicine form
	$("#orderMedicineForm").submit(function(e){
		e.preventDefault();
		openProgressBar();
		var form = $(this);
		var params = handleMultiValuedParams(form.serialize());
		$.ajax({ url: "./orderMedicine/order",
            data: params,
            type: "POST",
            success: function(response){
               	if(response.code == '200') {
               		//reset the form for successful operation
               		form[0].reset();
               		medicineTable.$("tr").each(function(index){
               			if(index > 0) {
               				medicineTable.fnDeleteRow(this);
               			}
               		});
               	}
               	closeProgressBar();
               	$("#orderStatusMessage").text(response.message);
           		$("#orderStatusForm").dialog("open");
            }
		});
	});
	
	//initializing the existing customer confirmation dialog
	$( "#customerInfoForm" ).dialog({
		autoOpen: false,
		width: 500,
		height: 300,
		modal: true,
		buttons: {
		  "Yes,Please": function() {
			  populateCustomerInfo();
		      $( this ).dialog( "close" );
		  },
		  "No,Thank You": function() {
			  $( this ).dialog( "close" );
		  }
		}
	});
	//initializing the progress information dialog
	$('#orderProgressForm').dialog({
		autoOpen: false,
		closeOnEscape: false,
		width: 500,
		modal: true
	});
	//initializing the order status dialog
	$( "#orderStatusForm" ).dialog({
		autoOpen: false,
		width: 500,
		height: 350,
		modal: true,
		buttons: {
		  "Ok": function() {
			  $("#orderStatusMessage").text("");
		      $( this ).dialog( "close" );
		  }
		}
	});
	
	//--------------------------------------------------------------------------------------------
	//The below section is for reusable functions. Please add all these type of functions below.
	//--------------------------------------------------------------------------------------------
	
	/**
	 * This function is getting reused to toggle between sections
	 * @param target The name of the targeted div
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
	
	/**
	 * Add a new row to the data table where user can enter medicine detail.
	 * @param deletable The boolean flag to indicate whether delete link should be present beside created row or not
	 */
	function addMedicineRow(deletable) {
		var rowIndex = medicineTable.fnAddData(["","","",""])[0];
		medicineTable.$("tr").each(function(index) {
			if(index == rowIndex) {
				$(this).find("td").each(function(index){
					if(index < 2) {
						$(this).css("width", "30%");
						if(index == 0) {
							this.innerHTML = '<input type="text" name="medicineName" required="required">';
						} else {
							this.innerHTML = '<input type="text" name="medicinePower" required="required">';
						}
					} else if(index == 2) {
						$(this).css("width", "30%");
						this.innerHTML = '<input type="number" name="medicineQuantity" min="1" required="required">';
					} else {
						$(this).css("text-align", "center");
						if(deletable) {
							this.innerHTML = '<a class="delete" href="#">Delete</a>';
						}
					}
				});
			}
		});
	}
	
	/**
	 * This javscript function handles the multi valued parameter 
	 * and make its value to comma separated string.
	 * 
	 * @param params The http parameters
	 */
	function handleMultiValuedParams(params) {
		var newParams = undefined;
		var parameterMap = {};
		var paramArray = params.split("&");
		$.each(paramArray, function(key, param) {
		    var parts = param.split("=");
		    if(!parameterMap[parts[0]]) {
		    	//key not present, add it
		    	parameterMap[parts[0]] = parts[1];
		    } else {
		    	//add the next value against same key
		    	parameterMap[parts[0]] = parameterMap[parts[0]] + "," + parts[1];
		    }
		});
		for(key in parameterMap) {
			if(newParams == undefined) {
				newParams=key+"="+parameterMap[key];
			} else {
				newParams=newParams+"&"+key+"="+parameterMap[key];
			}				
		}
		return newParams;
	}
	
	/**
	 * Open the progress bar dialog
	 */
	function openProgressBar() {
		$( "#progressBar" ).progressbar({
		      value: false
		});
		$( "#orderProgressForm" ).dialog( "open" );
	}
	
	/**
	 * Close the progress bar dialog
	 */
	function closeProgressBar() {
		$( "#progressBar" ).progressbar( "destroy" );
		$( "#orderProgressForm" ).dialog( "close" );
	}
	
	/**
	 * populate the customer information
	 */
	function populateCustomerInfo() {
		if(customer) {
			$("#customerMobileNumber").val(customer.mobileNumber);
			$("#customerName").val(customer.name);
			$("#customerHouseNumber").val(customer.address.houseOrFlatNumber);
			$("#customerHouseStreetAddress").val(customer.address.streetAddress);
			$("#customerHouseLocality").val(customer.address.locality);
			$("#customerHouseCity").val(customer.address.city);
			$("#customerHousePin").val(customer.address.pin);
		}
	}
});