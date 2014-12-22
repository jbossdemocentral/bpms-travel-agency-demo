
package acme.service.soap.hotelws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the acme.service.soap.hotelws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetAvailableHotel_QNAME = new QName("http://soap.service.acme/HotelWS/", "getAvailableHotel");
    private final static QName _GetAvailableHotelResponse_QNAME = new QName("http://soap.service.acme/HotelWS/", "getAvailableHotelResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: acme.service.soap.hotelws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BookHotelResponse }
     * 
     */
    public BookHotelResponse createBookHotelResponse() {
        return new BookHotelResponse();
    }

    /**
     * Create an instance of {@link CancelBookingResponse }
     * 
     */
    public CancelBookingResponse createCancelBookingResponse() {
        return new CancelBookingResponse();
    }

    /**
     * Create an instance of {@link BookHotel }
     * 
     */
    public BookHotel createBookHotel() {
        return new BookHotel();
    }

    /**
     * Create an instance of {@link GetAvailableHotel }
     * 
     */
    public GetAvailableHotel createGetAvailableHotel() {
        return new GetAvailableHotel();
    }

    /**
     * Create an instance of {@link GetAvailableHotelResponse }
     * 
     */
    public GetAvailableHotelResponse createGetAvailableHotelResponse() {
        return new GetAvailableHotelResponse();
    }

    /**
     * Create an instance of {@link CancelBooking }
     * 
     */
    public CancelBooking createCancelBooking() {
        return new CancelBooking();
    }

  
    /**
     * Create an instance of {@link Resort }
     * 
     */
    public Resort createResort() {
        return new Resort();
    }

    /**
     * Create an instance of {@link HotelRequest }
     * 
     */
    public HotelRequest createHotelRequest() {
        return new HotelRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableHotel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.acme/HotelWS/", name = "getAvailableHotel")
    public JAXBElement<GetAvailableHotel> createGetAvailableHotel(GetAvailableHotel value) {
        return new JAXBElement<GetAvailableHotel>(_GetAvailableHotel_QNAME, GetAvailableHotel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableHotelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.acme/HotelWS/", name = "getAvailableHotelResponse")
    public JAXBElement<GetAvailableHotelResponse> createGetAvailableHotelResponse(GetAvailableHotelResponse value) {
        return new JAXBElement<GetAvailableHotelResponse>(_GetAvailableHotelResponse_QNAME, GetAvailableHotelResponse.class, null, value);
    }

}
