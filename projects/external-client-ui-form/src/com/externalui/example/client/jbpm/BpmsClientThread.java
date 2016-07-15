package com.externalui.example.client.jbpm;


import static javax.xml.xpath.XPathConstants.STRING;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BpmsClientThread {

	// BPMS connection values
    public String server = "http://localhost:8080/business-central/";
    public String username = "erics";
    public String password = "bpmsuite1!";
    
    // Default sample input values
	private String applicantName = "Niraj";
	private String emailAddress = "npatel@redhat.com";
	private String numberOfTravelers = "2i";
	private String otherDetails = "NA";
	private String fromDestination = "London";
	private String toDestination = "Edinburgh";
	private String preferredDateOfArrival = "2014-06-06";
	private String preferredDateOfDeparture = "2014-06-12";
	
    private Jbpm6ClientImpl client;
    private String processid = null;

   public BpmsClientThread() {

    }
   
   private void init() {
       client = new Jbpm6ClientImpl(server, username, password, true);
//       new Jbpm6ClientObjects(server, username, password);
   }
   
	private int generateRandomNumber() {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(100);
	}

    public String starBusinessProcess(HashMap<String, String> hm) {

    	String response = null;
    	
    	Thread t = Thread.currentThread();
    	String threadName = "thread-no-"+generateRandomNumber();
    	t.setName(threadName);
    	
        init();
        System.out.println(Thread.currentThread());
        
    	applicantName = hm.get("applicantName");
    	emailAddress = hm.get("emailAddress");
    	numberOfTravelers = hm.get("numberOfTravelers");
    	fromDestination = hm.get("fromDestination");
    	toDestination = hm.get("toDestination");
    	preferredDateOfArrival = hm.get("preferredDateOfArrival");
    	preferredDateOfDeparture = hm.get("preferredDateOfDeparture");
    	otherDetails = hm.get("otherDetails");

        try {
			response = client.startProcess("org.specialtripsagency:specialtripsagencyproject:2.0.1",
                    "org.specialtripsagency.specialtripsagencyprocess",
                    "applicantName="+applicantName+
                    ",emailAddress="+emailAddress+
                    ",numberOfTravelers="+numberOfTravelers+
                    ",preferredDateOfArrival="+preferredDateOfArrival+
                    ",preferredDateOfDeparture="+preferredDateOfDeparture+
                    ",otherDetails="+otherDetails+
                    ",fromDestination="+fromDestination+
                    ",toDestination="+toDestination);

            System.out.println("Process started 1: " + response);

            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response.toString().getBytes()));
                System.out.println("Process  2: " + doc.getDoctype());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            XPath xpath = XPathFactory.newInstance().newXPath();

            try {
                processid = (String) xpath.evaluate("/process-instance-response/id", doc, STRING);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }

        } catch (HttpException e) {
            e.printStackTrace();
        }
        
        System.out.println("Process 3: [" + processid + "]"); // Display the string.  
        return processid;
    }
}
