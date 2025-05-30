package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = {
    "countryCode",
    "katoCode",
    "postalCode",
    "city",
    "street",
    "building",
    "apartment"
})
public class AddressType extends BaseJaxbType {

    @XmlElement(name = "CountryCode", required = true)
    protected String countryCode;
    
    @XmlElement(name = "KATOCode")
    protected String katoCode;
    
    @XmlElement(name = "PostalCode")
    protected String postalCode;
    
    @XmlElement(name = "City")
    protected String city;
    
    @XmlElement(name = "Street")
    protected String street;
    
    @XmlElement(name = "Building")
    protected String building;
    
    @XmlElement(name = "Apartment")
    protected String apartment;
}
