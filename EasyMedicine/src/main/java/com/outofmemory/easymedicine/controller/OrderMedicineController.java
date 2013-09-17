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
 * Handle requests coming from order medicine page
 * 
 * @author pribiswas
 * 
 */
@Controller
@RequestMapping(value = "/orderMedicine")
public class OrderMedicineController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderMedicineController.class);

	/**
	 * Forward user to order medicine home page
	 * 
	 * @param The
	 *            model map contains the request parameters from the page
	 * @return The name of the html page to render
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		LOGGER.info("Forwarding to order medicine page.");
		return "orderMedicineHome";
	}
}
