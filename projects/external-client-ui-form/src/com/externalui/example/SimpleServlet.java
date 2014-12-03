package com.externalui.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.externalui.example.client.jbpm.BpmsClientThread;

public class SimpleServlet extends HttpServlet { 
	
	/**
	 * Auto generated code
	 */
	private static final long serialVersionUID = 1L;
	private String processId = null;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException { 
		
		String color = null; 
		
		// reading user inputs 
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("applicantName", request.getParameter("applicantName"));
		hm.put("emailAddress", request.getParameter("emailAddress"));
		hm.put("numberOfTravelers", request.getParameter("numberOfTravelers") + "i"); // special case where the integer value should be submitted by appending "i" in the end to avoid java.lang.ClassCastException
		hm.put("fromDestination", request.getParameter("fromDestination"));
		hm.put("toDestination", request.getParameter("toDestination"));
		hm.put("preferredDateOfArrival", request.getParameter("preferredDateOfArrival"));
		hm.put("preferredDateOfDeparture", request.getParameter("preferredDateOfDeparture"));
		hm.put("otherDetails", request.getParameter("otherDetails"));
		
		System.out.println("=====> Before sending request: HashMap values are: \n" + hm);
		BpmsClientThread t = new BpmsClientThread();
		processId = t.starBusinessProcess(hm);
		System.out.println("=====> After sending request: ");
		PrintWriter out = response.getWriter();
		
		if (processId == null) {
			
			color = "red";
			
			out.println ( 
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" + 
					"<html> \n" + 
					  "<head> \n" + 
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n" + 
					  "<title> ERROR!!! </title> \n" + 
					"</head> \n" + 
					  "<body> \n" + 
					"<font size=\"12px\" color=\"" + color + "\">" + 
					  "Failed to start Business Process!!! <br>Please contact BPMS SYSTEMS ADMINISTRATOR for more details" +
					"</font> \n" + 
					  "</body> \n" + 
					"</html>" 
			);
		} else {
			
			color = "green";
			
			out.println ( 
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" + 
					"<html> \n" + 
					  "<head> \n" + 
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n" + 
					  "<title> My first jsp </title> \n" + 
					"</head> \n" + 
					  "<body> \n" + 
					"<font size=\"12px\" color=\"" + color + "\">" + 
					  "Thank you for submitting your request!!! <br>" +
					  "Your request has been processed successfully!!!<br>" +
					  "Your Unique Process ID is: [" + processId + "]<br><br>" +
					  "Our team will get in touch with you shortly with a Quote..\n" +
					"</font> \n" + 
					  "</body> \n" + 
					"</html>" 
			);
		}
	}
}