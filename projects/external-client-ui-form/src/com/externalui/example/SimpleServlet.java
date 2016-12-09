package com.externalui.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
		
		response.setCharacterEncoding("ISO-8859-1");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String content = "";
		
		if (processId == null) {
			content = getContent(getServletContext().getRealPath(File.separator) + File.separator + "error.html");
		} else {
			content = getContent(getServletContext().getRealPath(File.separator) + File.separator + "success_response.html");
			content = content.replace("CHANGEMEPROCESSID", processId);
		}
		
		out.println (content); 
	}

	private String getContent(String file) {
		String tempContent = ""; 
		try {
	        BufferedReader in = new BufferedReader(new FileReader(file));
	        String str;
	        
	        while ((str = in.readLine()) != null) {
	            tempContent +=str;
	        }
	        in.close();
	    } catch (IOException e) {
	    	System.out.println("IOException caught: ");
	    	e.printStackTrace();
	    }
		return tempContent;
	}
}
