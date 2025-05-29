package kz.gov.example.esutd.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

/**
 * <p>Java class for ContractType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContractType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ContractId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ContractNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ContractDate" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="ContractDurationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="ContractType" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}ContractTypeEnum"/&gt;
 *         &lt;element name="Position" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PositionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="WorkType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RemoteWork" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="WorkPlaceAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WorkPlaceKato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WorkPlaceCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WorkHours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TariffRate" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="WorkConditions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WorkConditionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GeneralSkills" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProfessionalSkills" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Department" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractType", propOrder = {
    "contractId",
    "contractNumber",
    "contractDate",
    "contractDurationType",
    "startDate",
    "endDate",
    "contractType",
    "position",
    "positionCode",
    "workType",
    "remoteWork",
    "workPlaceAddress",
    "workPlaceKato",
    "workPlaceCountry",
    "workHours",
    "tariffRate",
    "workConditions",
    "workConditionCode",
    "generalSkills",
    "professionalSkills",
    "department"
})
public class ContractType {
    @XmlElement(name = "ContractId", required = true)
    protected String contractId;
    
    @XmlElement(name = "ContractNumber", required = true)
    protected String contractNumber;
    
    @XmlElement(name = "ContractDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar contractDate;
    
    @XmlElement(name = "ContractDurationType")
    protected String contractDurationType;
    
    @XmlElement(name = "StartDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    
    @XmlElement(name = "EndDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    
    @XmlElement(name = "ContractType", required = true)
    @XmlSchemaType(name = "string")
    protected ContractTypeEnum contractType;
    
    @XmlElement(name = "Position", required = true)
    protected String position;
    
    @XmlElement(name = "PositionCode", required = true)
    protected String positionCode;
    
    @XmlElement(name = "WorkType")
    protected String workType;
    
    @XmlElement(name = "RemoteWork")
    protected Boolean remoteWork;
    
    @XmlElement(name = "WorkPlaceAddress")
    protected String workPlaceAddress;
    
    @XmlElement(name = "WorkPlaceKato")
    protected String workPlaceKato;
    
    @XmlElement(name = "WorkPlaceCountry")
    protected String workPlaceCountry;
    
    @XmlElement(name = "WorkHours")
    protected String workHours;
    
    @XmlElement(name = "TariffRate")
    protected BigDecimal tariffRate;
    
    @XmlElement(name = "WorkConditions")
    protected String workConditions;
    
    @XmlElement(name = "WorkConditionCode")
    protected String workConditionCode;
    
    @XmlElement(name = "GeneralSkills")
    protected String generalSkills;
    
    @XmlElement(name = "ProfessionalSkills")
    protected String professionalSkills;
    
    @XmlElement(name = "Department")
    protected String department;
}
