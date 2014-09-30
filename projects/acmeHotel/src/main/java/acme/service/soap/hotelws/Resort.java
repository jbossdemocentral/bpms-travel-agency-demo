
package acme.service.soap.hotelws;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import acme.service.soap.hotelws.impl.adapters.Adapter1;


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
 *         &lt;element name="targetDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="availability" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resort", propOrder = {
    "hotelId",
    "hotelName",
    "ratePerPerson",
    "hotelCity",
    "targetDate",
    "breakfastIncluded",
    "roomType",
    "holDuration"
})
public class Resort implements Serializable {

    protected Integer hotelId;
    @XmlElement(required = true)
    protected String hotelName;
    protected double ratePerPerson;
    @XmlElement(required = true)
    protected String hotelCity;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "date")
    protected Date targetDate;
    protected Boolean breakfastIncluded;
    @XmlElement(required = true)
    protected String roomType;
    protected Integer holDuration;

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
     * Gets the value of the targetDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getTargetDate() {
        return targetDate;
    }

    /**
     * Sets the value of the targetDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTargetDate(Date value) {
        this.targetDate = value;
    }

	public Boolean getBreakfastIncluded() {
		return breakfastIncluded;
	}

	public void setBreakfastIncluded(Boolean value) {
		this.breakfastIncluded = value;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String value) {
		this.roomType = value;
	}
	
    public Integer getHolDuration() {
		return holDuration;
	}

	public void setHolDuration(Integer holDuration) {
		this.holDuration = holDuration;
	}

}
