
package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;


/**
 * <p>Java class for ContractDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContractDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Operation" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}OperationType"/&gt;
 *         &lt;element name="Contract" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}ContractType" minOccurs="0"/&gt;
 *         &lt;element name="Employee" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}EmployeeType" minOccurs="0"/&gt;
 *         &lt;element name="Employer" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}EmployerType" minOccurs="0"/&gt;
 *         &lt;element name="TerminationInfo" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}TerminationInfoType" minOccurs="0"/&gt;
 *         &lt;element name="SubsidiaryContract" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}SubsidiaryContractType" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalDocument" type="{http://10.61.40.133/shep/bip-sync-wss-gost/}AdditionalDocumentType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractDataType", propOrder = {
    "operation",
    "contract",
    "employee",
    "employer",
    "terminationInfo",
    "subsidiaryContract",
    "additionalDocument"
})
public class ContractDataType extends BaseJaxbType {

    @XmlElement(name = "Operation", required = true)
    @XmlSchemaType(name = "string")
    protected OperationType operation;
    
    @XmlElement(name = "Contract")
    protected ContractType contract;
    
    @XmlElement(name = "Employee")
    protected EmployeeType employee;
    
    @XmlElement(name = "Employer")
    protected EmployerType employer;
    
    @XmlElement(name = "TerminationInfo")
    protected TerminationInfoType terminationInfo;
    
    @XmlElement(name = "SubsidiaryContract")
    protected SubsidiaryContractType subsidiaryContract;
    
    @XmlElement(name = "AdditionalDocument")
    protected AdditionalDocumentType additionalDocument;
}
