package acme.service.soap.hotelws.clientsample;

import acme.service.soap.hotelws.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        HotelWS_Service service1 = new HotelWS_Service();
	        System.out.println("Create Web Service...");
	        HotelWS port1 = service1.getHotelWSImplPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.getAvailableHotel(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        HotelWS port2 = service1.getHotelWSImplPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.getAvailableHotel(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
