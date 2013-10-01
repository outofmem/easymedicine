/**
 * This javascript file contains the jquery code
 * for distributor home page. Please remember the following notes:
 * 1) The server is communicated only to send/get data via AJAX
 * 2) The different part(divs) of distributor home tab are kept in a single page.They are
 *    toggled depending on the server response.
 * 3) Please put a proper comment in each and every section for future maintainability
 * 
 * @author pribiswas
 */
$(function() {
	//the section wrapper (includes all sections)
	var distributorSectionWrapper   = $('#distributorSectionWrapper'),	 
	//the active section value is set in hidden field
	activeSectionVal = $('#activeSection').val();
	//Hide those sections who does not have a class equal to the activeSectionVal.
	//At a time just one section is active and visible
	distributorSectionWrapper.children('div').each(function(i){
	    var theSectionDiv    = $(this);
	    //solve the inline display none problem when using fadeIn/fadeOut
	    if(!theSectionDiv.hasClass(activeSectionVal)) {
	    	theSectionDiv.hide();	
	    } else {
	    	theSectionDiv.addClass('active');
	    }	    
	});
	//the current section is the one with class active
	var activeSection    = distributorSectionWrapper.children('div.active'),   
	//the switch section links
	linkSections       = distributorSectionWrapper.find('.linkSection');
	//attach the click handler with the section switch links
	linkSections.bind('click',function(e){
		var link   = $(this);
		//Need to capture the parent form because I need to reset the 
		//current form back to normal before toggling to new form
		var currentForm = link.parent().parent();
		//first hide all the error divs of the current form
		currentForm.find('div').each(function(i){
			$(this).css("display","none");
		});
		//then reset the current form
		currentForm[0].reset();
		//now do the toggling
	    var target  = link.attr('rel');
	    toggleSection(target);
	   	e.preventDefault();
	});
	
	//attach event handler on change of the sign up e-mail id, an AJAX request
	//is sent to server to check whether given e-mail is already registered or not
	var emailValid = true;
	$("#signUpEmailId").change(function() {
		var param = "emailId="+$(this).val();
		$.ajax({ url: "./distributor/isEmailAlreadyRegistered",
	        data: param,
	        type: "GET",
	        success: function(response){
	           	if(response) {
	           		$("#signUpEmailIdErrDiv").css("display","block");
	           		emailValid = false;
	           	} else {
	           		$("#signUpEmailIdErrDiv").css("display","none");
	           		emailValid = true;
	           	}
	        }
        });
	});
	
	//attach event handler on change of the password fields to match both the passwords
	var passwordValid = true;
	$("#signUpPassword").change(function(){
		validatePassword($(this).val(), $("#signUpConfirmPassword").val(), $("#signUpPasswordErrDiv"), $("#signUpConfirmPasswordErrDiv"));
	});
	$("#signUpConfirmPassword").change(function(){
		validateConfirmPassword($("#signUpPassword").val(), $(this).val(), $("#signUpPasswordErrDiv"), $("#signUpConfirmPasswordErrDiv"));
	});
	
	//attach event handler on change of the sign up mobile number, an AJAX request
	//is sent to server to check whether given mobile is already registered or not
	var mobileValid = true;
	$("#signUpMobileNumber").change(function() {
		var param = "mobileNumber="+$(this).val();
		$.ajax({ url: "./distributor/isMobileAlreadyRegistered",
	        data: param,
	        type: "GET",
	        success: function(response){
	           	if(response) {
	           		$("#signUpMobileNumberErrDiv").css("display","block");
	           		mobileValid = false;
	           	} else {
	           		$("#signUpMobileNumberErrDiv").css("display","none");
	           		mobileValid = true;
	           	}
	        }
        });
	});
	
	//attach event handler on change of the store name, locality, city and pin, an AJAX request
	//is sent to server to check whether given store is already registered or not
	var storeValid = true;
	$("#signUpStoreName").change(function() {
		checkStoreExistence();
	});
	$("#signUpStoreLocality").change(function() {
		checkStoreExistence();
	});
	$("#signUpStoreCity").change(function() {
		checkStoreExistence();
	});
	$("#signUpStorePin").change(function() {
		checkStoreExistence();
	});
	
	//attach submit event handler for registration form
	$("#registerForm").submit(function(e){
		e.preventDefault();
		var form = $(this);
		var params = form.serialize();
		if(emailValid && passwordValid && mobileValid && storeValid) {
			$.ajax({ url: "./distributor/register",
                data: params,
                type: "POST",
                success: function(response){
	               	if(response.code == '201') {
	               		toggleSection("loggedInDistributor");
	               		$("#emailId").val(response.id);
	               		form[0].reset();
	               	}
                }
			});
		}
	});
	
	//attach submit event handler for login form
	$("#loginForm").submit(function(e){
		e.preventDefault();
		var form = $(this);
		var params = form.serialize();
		$.ajax({ url: "./distributor/login",
            data: params,
            type: "POST",
            success: function(response){
            	form[0].reset();
               	if(response.code == '200') {
               		$("#loginErrDiv").css("display","none");
               		toggleSection("loggedInDistributor");
               		$("#emailId").val(response.id);
               	} else {
               		$("#loginErrDiv").css("display","block");
               	}
            }
		});
	});
	
	//attach submit event handler for forgot password form
	$("#forgotPasswordForm").submit(function(e){
		e.preventDefault();
		var form = $(this);
		var params = form.serialize();
		$.ajax({ url: "./distributor/resetPassword",
            data: params,
            type: "POST",
            success: function(response){
            	form[0].reset();
               	if(response.code == '200') {
               		$("#forgotPwdErrDiv").css("display","none");
               		toggleSection("login");
               	} else {
               		$("#forgotPwdErrDiv").css("display","block");
               	}
            }
		});
	});
	
	//attach logout event handler
	$("#logoutButton").click(function(){
		logout();
	});
	
	//enable tabs for distributor home section
	$( "#distributorTabs" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
	$( "#distributorTabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
	
	//Attach the event handlers for change password tab section
	$("#changePwdNewPassword").change(function(){
		validatePassword($(this).val(), $("#changePwdConfirmNewPassword").val(), $("#changePwdNewPasswordErrDiv"), $("#changePwdConfirmNewPasswordErrDiv"));
	});
	$("#changePwdConfirmNewPassword").change(function(){
		validateConfirmPassword($("#changePwdNewPassword").val(), $(this).val(), $("#changePwdNewPasswordErrDiv"), $("#changePwdConfirmNewPasswordErrDiv"));
	});
	
	//attach submit event handler for change password form
	$("#changePasswordForm").submit(function(e){
		e.preventDefault();
		var form = $(this);
		var params = form.serialize()+"&emailId="+$("#emailId").val();
		if(passwordValid) {
			$.ajax({ url: "./distributor/changePassword",
                data: params,
                type: "POST",
                success: function(response){
                	form[0].reset();
	               	if(response.code == '200') {
	               		$("#changePwdErrDiv").css("display","none");
	               		logout();
	               	} else {
	               		$("#changePwdErrDiv").css("display","block");
	               	}
                }
			});
		}
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
	        activeSection= distributorSectionWrapper.children('div.'+target);
	        //animate the wrapper
	        distributorSectionWrapper.stop()
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
	 * The below function fires an AJAX request to check whether the given store is already registered or not
	 */
	function checkStoreExistence() {
		var param = "storePin="+$("#signUpStorePin").val()+"&storeCity="+$("#signUpStoreCity").val()+"&storeLocality="+$("#signUpStoreLocality").val()+"&storeName="+$("#signUpStoreName").val();
		$.ajax({ url: "./distributor/isStoreAlreadyRegistered",
	        data: param,
	        type: "GET",
	        success: function(response){
	           	if(response) {
	           		$("#signUpStoreNameErrDiv").css("display","block");
	           		storeValid = false;
	           	} else {
	           		$("#signUpStoreNameErrDiv").css("display","none");
	           		storeValid = true;
	           	}
	        }
        });
	}
	
	/**
	 * The below function check the password and confirmPassword value and 
	 * show/hide the error divs depending on the validation.
	 * 
	 * @param pwd The value of the password
	 * @param confirmPwd The value of the confirm password
	 * @param pwdErrDiv The jquery object of password error div
	 * @param confirmPwdErrDiv The jquery object of confirm password error div
	 */
	function validatePassword(pwd, confirmPwd, pwdErrDiv, confirmPwdErrDiv) {
		if(pwd != "" && confirmPwd != "" && pwd != confirmPwd) {
			pwdErrDiv.css("display","block");
			passwordValid = false;
		} else {
			pwdErrDiv.css("display","none");
			confirmPwdErrDiv.css("display","none");
			passwordValid = true;
		}
	}
	
	/**
	 * The below function check the password and confirmPassword value and 
	 * show/hide the error divs depending on the validation.
	 * 
	 * @param pwd The value of the password
	 * @param confirmPwd The value of the confirm password
	 * @param pwdErrDiv The jquery object of password error div
	 * @param confirmPwdErrDiv The jquery object of confirm password error div
	 */
	function validateConfirmPassword(pwd, confirmPwd, pwdErrDiv, confirmPwdErrDiv) {
		if(confirmPwd != "" && pwd != "" && confirmPwd != pwd) {
			confirmPwdErrDiv.css("display","block");
			passwordValid = false;
		} else {
			confirmPwdErrDiv.css("display","none");
			pwdErrDiv.css("display","none");
			passwordValid = true;
		}
	}
	
	/**
	 * This function fires AJAX request to logout from the system
	 */
	function logout() {
		var param = "emailId="+$("#emailId").val();
		$.ajax({ url: "./distributor/logout",
			data: param,
            type: "POST",
            success: function(){
            	//distributor is logged out, so I need to reset all the distributor
            	//logged in sections(forms, divs)
            	$("#loggedInDistributor").find("form").each(function(i){
            		var form = $(this);
            		form.find('div').each(function(i){
            			$(this).css("display","none");
            		});
            		form[0].reset();
            	});
            	$( "#distributorTabs" ).tabs( "option", "active", 0 );
            	//now toggle it back to login section
            	toggleSection("login");
            }
		});
	}
});