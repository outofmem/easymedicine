/**
 * 
 */
package com.outofmemory.easymedicine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles the requests from dealers home page
 * 
 * @author pribiswas
 * 
 */
@Controller
public class DealersHomeController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DealersHomeController.class);

	/**
	 * Forward user to dealers home page
	 * 
	 * @param The
	 *            model map contains the request parameters from the page
	 * @return The name of the html page to render
	 */
	@RequestMapping(value = "/dealersHome", method = RequestMethod.GET)
	public String dealersHome(Model model) {
		LOGGER.info("Forwarding to dealers home page.");
		return "dealersHome";
	}
}
