/**
 * 
 */
package com.outofmemory.easymedicine.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.outofmemory.easymedicine.form.DistributorRegistrationForm;
import com.outofmemory.easymedicine.model.OperatedCities;
import com.outofmemory.easymedicine.service.DistributorService;

/**
 * Handles the requests from dealers home page
 * 
 * @author pribiswas
 * 
 */
@Controller
@RequestMapping(value = "/distributor")
public class DistributorHomeController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DistributorHomeController.class);

	@Autowired
	private DistributorService distributorService;

	/**
	 * Forward to distributor's home page if active session is found, forward to
	 * tab home page otherwise
	 * 
	 * @param model
	 *            The model map contains the request parameters to the page
	 * @param session
	 *            The HTTP session
	 * @return The name of the html page to render
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model, HttpSession session) {
		if (session.getAttribute("sessionId") == null) {
			LOGGER.info("Forwarding to home page.");
			// Preface prompt will be opened when there is no active session
			model.addAttribute("activeSection", "preface");
		} else {
			LOGGER.info("Forwarding to distributor home page.");
			model.addAttribute("activeSection", "loggedInDistributor");
			model.addAttribute("sessionId", session.getAttribute("sessionId"));
		}
		// populate the cities in register prompt
		model.addAttribute("cities", OperatedCities.values());
		return "distributorHome";
	}

	/**
	 * Check whether given e-mail is already registered or not
	 * 
	 * @param emailId
	 * @return true if provided e-mail id is already registered, false otherwise
	 */
	@RequestMapping(value = "/isEmailAlreadyRegistered", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	boolean isEmailAlreadyRegistered(@RequestParam String emailId) {
		if (StringUtils.isEmpty(emailId)) {
			return false;
		}
		return distributorService.isEmailAlreadyRegistered(emailId);
	}

	/**
	 * Check whether given mobile number is already registered or not
	 * 
	 * @param mobileNumber
	 * @return true if provided mobile number is already registered, false
	 *         otherwise
	 */
	@RequestMapping(value = "/isMobileAlreadyRegistered", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	boolean isMobileAlreadyRegistered(@RequestParam String mobileNumber) {
		if (StringUtils.isEmpty(mobileNumber)) {
			return false;
		}
		return distributorService.isMobileAlreadyRegistered(mobileNumber);
	}

	/**
	 * Check whether given store is already registered or not
	 * 
	 * @param request
	 *            The HttpServletRequest
	 * @return true if provided store is already registered, false otherwise
	 */
	@RequestMapping(value = "/isStoreAlreadyRegistered", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	boolean isStoreAlreadyRegistered(HttpServletRequest request) {
		String storePin = request.getParameter("storePin");
		String storeCity = request.getParameter("storeCity");
		String storeLocality = request.getParameter("storeLocality");
		String storeName = request.getParameter("storeName");

		if (StringUtils.isEmpty(storeName)
				|| StringUtils.isEmpty(storeLocality)
				|| StringUtils.isEmpty(storeCity)
				|| StringUtils.isEmpty(storePin)) {
			return false;
		}
		return distributorService.isStoreAlreadyRegistered(storeName,
				storeLocality, storeCity, storePin);
	}

	/**
	 * Register a distributor to the system
	 * 
	 * @param request
	 * @return The JSON response
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String register(HttpServletRequest request) {
		JSONObject response = new JSONObject();
		DistributorRegistrationForm form = getDistributorRegistrationFormFromRequest(request);

		if (distributorService.addDistributor(form)) {
			request.getSession().setAttribute("sessionId",
					RandomStringUtils.randomAlphanumeric(15));
			response.put("code", HttpStatus.SC_CREATED);
			response.put("id", form.getEmailAddress());
		}

		return response.toString();
	}

	/**
	 * login to the system
	 * 
	 * @param request
	 * @return The JSON response
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String login(HttpServletRequest request) {
		String emailId = request.getParameter("loginEmailId");
		String password = request.getParameter("loginPassword");

		JSONObject response = new JSONObject();

		if (distributorService.authenticateDistributor(emailId, password)) {
			request.getSession().setAttribute("sessionId",
					RandomStringUtils.randomAlphanumeric(15));
			response.put("code", HttpStatus.SC_OK);
			response.put("id", emailId);
		} else {
			response.put("code", HttpStatus.SC_UNAUTHORIZED);
		}

		return response.toString();
	}

	/**
	 * Reset the password for requested distributor
	 * 
	 * @param request
	 * @return The JSON response
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String resetPassword(HttpServletRequest request) {
		String emailId = request.getParameter("forgotPwdEmailId");
		JSONObject response = new JSONObject();
		if (distributorService.isEmailAlreadyRegistered(emailId)) {
			distributorService.resetDistributorPassword(emailId);
			response.put("code", HttpStatus.SC_OK);
		} else {
			response.put("code", HttpStatus.SC_UNAUTHORIZED);
		}
		return response.toString();
	}

	/**
	 * Change the password for requested distributor
	 * 
	 * @param request
	 * @return The JSON response
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String changePassword(HttpServletRequest request) {
		String emailId = request.getParameter("emailId");
		String oldPassword = request.getParameter("changePwdOldPassword");
		String newPassword = request.getParameter("changePwdNewPassword");
		
		JSONObject response = new JSONObject();
		if (distributorService.authenticateDistributor(emailId, oldPassword)) {
			distributorService.changeDistributorPassword(emailId, newPassword);
			response.put("code", HttpStatus.SC_OK);
		} else {
			response.put("code", HttpStatus.SC_UNAUTHORIZED);
		}
		return response.toString();
	}

	/**
	 * Logged out the distributor from the application
	 * 
	 * @param session
	 *            The HTTP session
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody
	void logout(HttpSession session) {
		session.removeAttribute("sessionId");
		session.invalidate();
		LOGGER.info("Distributor is logged out");
	}

	/**
	 * Generate distributor registration form from Http request
	 * 
	 * @param request
	 *            The {@link HttpServletRequest}
	 * @return An instance of {@link DistributorRegistrationForm}
	 */
	private DistributorRegistrationForm getDistributorRegistrationFormFromRequest(
			HttpServletRequest request) {
		DistributorRegistrationForm form = new DistributorRegistrationForm();
		form.setEmailAddress(request.getParameter("signUpEmailId"));
		form.setPassword(request.getParameter("signUpPassword"));
		form.setMobileNumber(request.getParameter("signUpMobileNumber"));
		form.setMedicineStoreName(request.getParameter("signUpStoreName"));
		form.setMedicineStoreAddress(request.getParameter("signUpStoreAddress"));
		form.setMedicineStoreLocality(request
				.getParameter("signUpStoreLocality"));
		form.setMedicineStoreCity(request.getParameter("signUpStoreCity"));
		form.setMedicineStorePin(request.getParameter("signUpStorePin"));
		return form;
	}

}
