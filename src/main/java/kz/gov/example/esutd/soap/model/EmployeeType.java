package kz.gov.example.esutd.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for EmployeeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EmployeeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EmployeeId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IIN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MiddleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BirthDate" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="Gender" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}GenderType"/&gt;
 *         &lt;element name="Citizenship" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Address" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}AddressType" minOccurs="0"/&gt;
 *         &lt;element name="Contacts" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}ContactsType" minOccurs="0"/&gt;
 *         &lt;element name="DocumentInfo" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}DocumentInfoType" minOccurs="0"/&gt;
 *         &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BIK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HrPhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmployeeType", propOrder = {
    "employeeId",
    "iin",
    "lastName",
    "firstName",
    "middleName",
    "birthDate",
    "gender",
    "citizenship",
    "address",
    "bankName",
    "bik",
    "accountNumber",
    "phoneNumber",
    "hrPhoneNumber"
})
public class EmployeeType {
    @XmlElement(name = "EmployeeId", required = true)
    protected String employeeId;
    
    @XmlElement(name = "IIN", required = true)
    protected String iin;
    
    @XmlElement(name = "LastName", required = true)
    protected String lastName;
    
    @XmlElement(name = "FirstName", required = true)
    protected String firstName;
    
    @XmlElement(name = "MiddleName")
    protected String middleName;
    
    @XmlElement(name = "BirthDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar birthDate;
    
    @XmlElement(name = "Gender", required = true)
    @XmlSchemaType(name = "string")
    protected GenderType gender;
    
    @XmlElement(name = "Citizenship", required = true)
    protected String citizenship;
    
    @XmlElement(name = "Address")
    protected AddressType address;
    
    @XmlElement(name = "BankName")
    protected String bankName;
    
    @XmlElement(name = "BIK")
    protected String bik;
    
    @XmlElement(name = "AccountNumber")
    protected String accountNumber;
    
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    
    @XmlElement(name = "HRPhoneNumber")
    protected String hrPhoneNumber;
}
