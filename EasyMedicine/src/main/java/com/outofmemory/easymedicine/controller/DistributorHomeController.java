/**
 * 
 */
package com.outofmemory.easymedicine.controller;

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
	 * Check whether the given store is already registered or not
	 * 
	 * @param storePin
	 * @param storeCity
	 * @param storeLocality
	 * @param storeName
	 * @return true if provided store is already registered, false otherwise
	 */
	@RequestMapping(value = "/isStoreAlreadyRegistered", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	boolean isStoreAlreadyRegistered(@RequestParam String storePin,
			@RequestParam String storeCity, @RequestParam String storeLocality,
			@RequestParam String storeName) {

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
	 * Register a distributor to the system.
	 * 
	 * @param form
	 *            An instance of {@link DistributorRegistrationForm}
	 * @param session
	 *            An instance of {@link HttpSession}
	 * 
	 * @return The JSON response
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String register(DistributorRegistrationForm form, HttpSession session) {
		JSONObject response = new JSONObject();

		if (distributorService.addDistributor(form)) {
			session.setAttribute("sessionId",
					RandomStringUtils.randomAlphanumeric(15));
			response.put("code", HttpStatus.SC_CREATED);
			response.put("id", form.getEmailAddress());
		}

		return response.toString();
	}

	/**
	 * Logged the distributor in to the system
	 * 
	 * @param loginEmailId
	 * @param loginPassword
	 * @param session
	 * @return The JSON response
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String login(@RequestParam String loginEmailId,
			@RequestParam String loginPassword, HttpSession session) {

		JSONObject response = new JSONObject();

		if (distributorService.authenticateDistributor(loginEmailId,
				loginPassword)) {
			session.setAttribute("sessionId",
					RandomStringUtils.randomAlphanumeric(15));
			response.put("code", HttpStatus.SC_OK);
			response.put("id", loginEmailId);
		} else {
			response.put("code", HttpStatus.SC_UNAUTHORIZED);
		}

		return response.toString();
	}

	/**
	 * Reset the password for requested distributor
	 * 
	 * @param emailId
	 * @return JSON response
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String resetPassword(@RequestParam String emailId) {
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
	 * @param emailId
	 *            The e-mail id of the requested distributor
	 * @param oldPassword
	 *            The old password
	 * @param newPassword
	 *            The new password
	 * @return A JSON response
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String changePassword(@RequestParam String emailId,
			@RequestParam String oldPassword, @RequestParam String newPassword) {

		JSONObject response = new JSONObject();
		if (distributorService.authenticateDistributor(emailId, oldPassword)) {
			distributorService.changeDistributorPassword(emailId, newPassword);
			response.put("code", HttpStatus.SC_OK);
			LOGGER.debug("The password for " + emailId
					+ " is changed successfully.");
		} else {
			response.put("code", HttpStatus.SC_UNAUTHORIZED);
		}
		return response.toString();
	}

	/**
	 * Logged out the distributor from the system
	 * 
	 * @param emailId
	 *            The e-mail id of the distributor
	 * @param session
	 *            An instance of {@link HttpSession}
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody
	void logout(@RequestParam String emailId, HttpSession session) {
		session.removeAttribute("sessionId");
		session.invalidate();
		LOGGER.debug(emailId + " is logged out.");
	}

}
