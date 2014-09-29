
package acme.service.soap.hotelws;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Entity
@Table(name="resort"
    ,catalog="wsplane"
)
@NamedQueries({  
    @NamedQuery(name = "Resort.findAll", query = "SELECT r FROM Resort r"),
    @NamedQuery(name = "Resort.findFiltered", 
    				query = "SELECT r FROM Resort r "
    						+ "WHERE r.hotelCity = :hotelCity "
    						+ "AND r.targetDate = :targetDate")   })
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
    @Id @GeneratedValue(strategy=IDENTITY)    
    @Column(name="hotel_id", unique=true, nullable=false)
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
    @Column(name="hotel_name", length=45)
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
    @Column(name="rate_per_room", precision=10)
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
    @Column(name="hotel_city", length=45)
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
    @Temporal(TemporalType.DATE)
    @Column(name="target_date", length=10)
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

    @Column(name = "breakfast_included",nullable = true, columnDefinition = "TINYINT(1)")
	public Boolean getBreakfastIncluded() {
		return breakfastIncluded;
	}

	public void setBreakfastIncluded(Boolean value) {
		this.breakfastIncluded = value;
	}

	@Column(name="room_type", length=45)
	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String value) {
		this.roomType = value;
	}
	
	@Column(name="hol_duration")
    public Integer getHolDuration() {
		return holDuration;
	}

	public void setHolDuration(Integer holDuration) {
		this.holDuration = holDuration;
	}

}
