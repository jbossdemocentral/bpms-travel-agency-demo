package com.jboss.soap.service.acmedemo.clientsample;

import com.jboss.soap.service.acmedemo.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        AcmeDemoService service1 = new AcmeDemoService();
	        System.out.println("Create Web Service...");
	        AcmeDemoInterface port1 = service1.getAcmeDemoInterfaceImplPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.listAvailablePlanes(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        AcmeDemoInterface port2 = service1.getAcmeDemoInterfaceImplPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.listAvailablePlanes(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
