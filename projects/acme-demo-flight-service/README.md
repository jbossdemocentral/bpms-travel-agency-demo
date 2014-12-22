Flight Web Service
====================

A project that provides flight web services consumed by the Special Trips Agency. The details provided on this page are just a description of the service in case you are interested in modifying it for your own use case. Interaction with the web service is done through BPMS and the instructions on how to do it are given in the scenario documentation (Demo Guide). 

Special Trips Agency will connect to The Flight Service through web services using WSDL and SOAP messages. The webservice WSDL is located (once the BPMS server is up and running) here: http://localhost:8080/acme-flight-service-1.0/AcmeDemoInterface?wsdl.
There are three services that are provided by this web service:
List Flights
Book Flight
Cancel Flight 

One consideration to make is that, the web service is not pulling data from any data store as this is out of scope for this demo. Returned result is auto generated.

1. List Flight Operation
--------------------------
This service returns a list of flights when invoked. Currently, I have not yet figured out how to handle a list [array] object that is returned from a service so I am only returning one flight instead of two flights (outbound and inbound). Later on, I will look to implement a service that handles a return list result.

The WSDL has the following entry for List Flight operation

<xs:complexType name="listAvailablePlanes">
	<xs:sequence>
		<xs:element minOccurs="0" name="in" type="tns:flightRequest"/>
	</xs:sequence>
</xs:complexType>
<xs:complexType name="flightRequest">
	<xs:sequence>
		<xs:element name="startCity" type="xs:string"/>
		<xs:element name="endCity" type="xs:string"/>
		<xs:element name="startDate" type="xs:string"/>
		<xs:element name="endDate" type="xs:string"/>
	</xs:sequence>
</xs:complexType>
<xs:complexType name="listAvailablePlanesResponse">
	<xs:sequence>
		<xs:element minOccurs="0" name="return" type="tns:flight"/>
	</xs:sequence>
</xs:complexType>
<xs:complexType name="flight">
	<xs:sequence>
		<xs:element name="company" type="xs:string"/>
		<xs:element name="planeId" type="xs:int"/>
		<xs:element name="ratePerPerson" type="xs:decimal"/>
		<xs:element name="startCity" type="xs:string"/>
		<xs:element name="targetCity" type="xs:string"/>
		<xs:element name="travelDate" type="xs:string"/>
	</xs:sequence>
</xs:complexType>

The snippet above describes the request (listAvailablePlanes) and response (listAvailablePlanesResponse). 
"listAvailablePlanes" requires an object called "flightRequest" and the web service responds "listAvailablePlanesResponse" with a "flight" object. It is a simple implementation at the moment... but it is useful to show how objects are handled in BPMS.

As already mentioned... there is no complex logic that has been implemented on the web service. If you send a flightRequest... the service will always return a flight with the same start and end city you specified; the same start and end date you specified... plus a flat fee of 525 per seat... The idea is that this is a wholesale price and it is up to Special Trips Agency to apply their own business rules to determine a profitable price for their business.


2. Book Flight Operation
--------------------------
The book flight operation consumes a string variable and produces another string variable as the booking confirmation.

The WSDL has the following entry for Book Flight operation

<xs:element name="bookFlights">
	<xs:complexType>
		<xs:sequence>
			<xs:element name="in" type="xs:string"/>
		</xs:sequence>
	</xs:complexType>
</xs:element>
<xs:element name="bookFlightsResponse">
	<xs:complexType>
		<xs:sequence>
			<xs:element name="out" type="xs:string"/>
		</xs:sequence>
	</xs:complexType>
</xs:element>

The bookFlightsResponse is the confirmation string that the customer needs to use, in the event they want to cancel the service.

3. Cancel Flight Operation
---------------------------
Cancellation of the service requires the booking confirmation ID that was generated from the Book Flights operation. The Cancel Flight operation will return a percentage value (integer between 5 and 10) that represents the percentage that will be deducted from the price of flights Note: this is not the total price of the ticket but the portion allocated to flights

The WSDL has the following entry for Cancel Flight operation

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

