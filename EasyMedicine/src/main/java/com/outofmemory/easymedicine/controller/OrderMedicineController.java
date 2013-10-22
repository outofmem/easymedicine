/**
 * 
 */
package com.outofmemory.easymedicine.controller;

import java.util.Locale;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.outofmemory.easymedicine.form.OrderMedicineForm;
import com.outofmemory.easymedicine.model.OperatedCities;
import com.outofmemory.easymedicine.service.OrderService;

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

	@Autowired
	private OrderService orderService;
	@Autowired
	private MessageSource messageSource;

	/**
	 * Forward user to order medicine home page
	 * 
	 * @param The
	 *            model map contains the request parameters from the page
	 * @return The name of the html page to render
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		LOGGER.debug("Forwarding to order medicine page.");
		model.addAttribute("activeSection", "preface");
		model.addAttribute("cities", OperatedCities.values());
		return "orderMedicineHome";
	}

	/**
	 * Serve the order and notify the customer
	 * 
	 * @param form
	 *            An instance of {@link OrderMedicineForm}
	 * @return The JSON response
	 */
	@RequestMapping(value = "/order", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String order(OrderMedicineForm form) {
		LOGGER.debug("A new order is placed : " + form);
		JSONObject response = new JSONObject();
		String orderRef = orderService.placeOrder(form);
		if (orderRef == null) {
			response.put("code", HttpStatus.SC_PRECONDITION_FAILED);
			response.put("message", messageSource.getMessage(
					"error.message.no.nearby.distributor.found", new Object[0],
					Locale.ENGLISH));
		} else {
			response.put("code", HttpStatus.SC_OK);
			response.put("message", messageSource.getMessage(
					"order.confirmation.notification.message",
					new Object[] { orderRef }, Locale.ENGLISH));
		}
		return response.toString();
	}
}
