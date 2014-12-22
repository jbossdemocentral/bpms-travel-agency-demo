
package acme.service.soap.hotelws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resort complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resort">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hotelId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="hotelName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ratePerPerson" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="hotelCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="availableFrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="availableTo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resort", propOrder = {
    "hotelId",
    "hotelName",
    "ratePerPerson",
    "hotelCity",
    "availableFrom",
    "availableTo"
})
public class Resort implements Serializable {

    protected Integer hotelId;
    @XmlElement(required = true)
    protected String hotelName;
    protected double ratePerPerson;
    @XmlElement(required = true)
    protected String hotelCity;
    @XmlElement(required = true)
    protected String availableFrom;
    @XmlElement(required = true)
    protected String availableTo;

    /**
     * Gets the value of the hotelId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHotelId() {
        return hotelId;
    }

    /**
     * Sets the value of the hotelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHotelId(Integer value) {
        this.hotelId = value;
    }

    /**
     * Gets the value of the hotelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Sets the value of the hotelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHotelName(String value) {
        this.hotelName = value;
    }

    /**
     * Gets the value of the ratePerPerson property.
     * 
     */
    public double getRatePerPerson() {
        return ratePerPerson;
    }

    /**
     * Sets the value of the ratePerPerson property.
     * 
     */
    public void setRatePerPerson(double value) {
        this.ratePerPerson = value;
    }

    /**
     * Gets the value of the hotelCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHotelCity() {
        return hotelCity;
    }

    /**
     * Sets the value of the hotelCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHotelCity(String value) {
        this.hotelCity = value;
    }

    /**
     * Gets the value of the availableFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvailableFrom() {
        return availableFrom;
    }

    /**
     * Sets the value of the availableFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvailableFrom(String value) {
        this.availableFrom = value;
    }

    /**
     * Gets the value of the availableTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvailableTo() {
        return availableTo;
    }

    /**
     * Sets the value of the availableTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvailableTo(String value) {
        this.availableTo = value;
    }

}
