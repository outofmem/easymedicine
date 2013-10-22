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
 * Handles the requests from track order page
 * 
 * @author pribiswas
 * 
 */
@Controller
@RequestMapping(value = "/trackOrder")
public class TrackOrderController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrackOrderController.class);

	/**
	 * Forward user to track order home page
	 * 
	 * @param The
	 *            model map contains the request parameters from the page
	 * @return The name of the html page to render
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		LOGGER.debug("Forwarding to track order page.");
		return "trackOrderHome";
	}
}
