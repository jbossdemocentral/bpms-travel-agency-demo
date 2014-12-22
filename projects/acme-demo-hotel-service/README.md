Hotel Web Service
====================

A project that provides hotel web services consumed by the Special Trips Agency. The details provided on this page are just a description of the service in case you are interested in modifying it for your own use case. Interaction with the web service is done through BPMS and the instructions on how to do it are given in the scenario documentation (Demo Guide). 

Special Trips Agency will connect to The Hotel Service through web services using WSDL and SOAP messages. The webservice WSDL is located (once the BPMS server is up and running) here: http://localhost:8080/acme-hotel-service-1.0/HotelWS?wsdl.
There are three services that are provided by this web service:
List Hotels
Book Hotel
Cancel Hotel 

One consideration to make is that, the web service is not pulling data from any data store as this is out of scope for this demo. Returned result is auto generated.

1. List Hotel Operation
--------------------------
This service returns a list of hotels when invoked. Currently, I have not yet figured out how to handle a list [array] object that is returned from a service so I am only returning one hotel instead of two hotels (outbound and inbound). Later on, I will look to implement a service that handles a return list result.

The WSDL has the following entry for List Hotel operation

<xs:complexType name="getAvailableHotel">
	<xs:sequence>
		<xs:element minOccurs="0" name="in" type="tns:hotelRequest"/>
	</xs:sequence>
</xs:complexType>
<xs:complexType name="hotelRequest">
	<xs:sequence>
		<xs:element name="targetCity" type="xs:string"/>
		<xs:element name="startDate" type="xs:string"/>
		<xs:element name="endDate" type="xs:string"/>
	</xs:sequence>
</xs:complexType>
<xs:complexType name="getAvailableHotelResponse">
	<xs:sequence>
		<xs:element minOccurs="0" name="return" type="tns:resort"/>
	</xs:sequence>
</xs:complexType>
<xs:complexType name="resort">
	<xs:sequence>
		<xs:element minOccurs="0" name="hotelId" type="xs:int"/>
		<xs:element name="hotelName" type="xs:string"/>
		<xs:element name="ratePerPerson" type="xs:double"/>
		<xs:element name="hotelCity" type="xs:string"/>
		<xs:element name="availableFrom" type="xs:string"/>
		<xs:element name="availableTo" type="xs:string"/>
	</xs:sequence>
</xs:complexType>


The snippet above describes the request (getAvailableHotel) and response (getAvailableHotelResponse). 
"getAvailableHotel" requires an object called "hotelRequest" and the web service responds "getAvailableHotelResponse" with a "resort" object. It is a simple implementation at the moment... but it is useful to show how objects are handled in BPMS.

As already mentioned... there is no complex logic that has been implemented on the web service. If you send a hotelRequest... the service will always return a hotel with the same start and end city you specified; the same start and end date you specified... plus a flat fee of 200 per day per person... The idea is that this is a wholesale price and it is up to Special Trips Agency to apply their own business rules to determine a profitable price for their business.


2. Book Hotel Operation
--------------------------
The book hotel operation consumes a string variable and produces another string variable as the booking confirmation.

The WSDL has the following entry for Book Hotel operation

<xs:element name="bookHotel">
	<xs:complexType>
		<xs:sequence>
			<xs:element name="in" type="xs:string"/>
		</xs:sequence>
	</xs:complexType>
</xs:element>
<xs:element name="bookHotelResponse">
	<xs:complexType>
		<xs:sequence>
			<xs:element name="out" type="xs:string"/>
		</xs:sequence>
	</xs:complexType>
</xs:element>

The bookHotelResponse is the confirmation string that the customer needs to use, in the event they want to cancel the service.

3. Cancel Hotel Operation
---------------------------
Cancellation of the service requires the booking confirmation ID that was generated from the Book Hotels operation. The Cancel Hotel operation will return a percentage value (integer between 5 and 10) that represents the percentage that will be deducted from the price of hotels Note: this is not the total price of the ticket but the portion allocated to hotels

The WSDL has the following entry for Cancel Hotel operation

<xs:element name="cancelBooking">
	<xs:complexType>
		<xs:sequence>
			<xs:element name="in" type="xs:string"/>
		</xs:sequence>
	</xs:complexType>
</xs:element>
<xs:element name="cancelBookingResponse">
	<xs:complexType>
		<xs:sequence>
			<xs:element name="out" type="xs:int"/>
		</xs:sequence>
	</xs:complexType>
</xs:element>

There is no logic in the web service to handle wrong booking confirmation ID. In future releases, it will be considered to introduce some sort of validation that checks if the Cancellation input is valid. This will be useful because the BPMS process will need to handle the error that is generated, hence showing one of its capabilities that is not yet showcased in the demo.

