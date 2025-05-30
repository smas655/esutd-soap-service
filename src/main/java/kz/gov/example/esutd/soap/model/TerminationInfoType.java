package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TerminationInfoType", propOrder = {
    "contractId",
    "terminationDate",
    "terminationReason",
    "terminationReasonCode",
    "orderNumber",
    "orderDate",
    "additionalInfo"
})
public class TerminationInfoType extends BaseJaxbType {

    @XmlElement(name = "ContractId", required = true)
    protected String contractId;
    
    @XmlElement(name = "TerminationDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar terminationDate;
    
    @XmlElement(name = "TerminationReason", required = true)
    protected String terminationReason;
    
    @XmlElement(name = "TerminationReasonCode", required = true)
    protected String terminationReasonCode;
    
    @XmlElement(name = "OrderNumber")
    protected String orderNumber;
    
    @XmlElement(name = "OrderDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar orderDate;
    
    @XmlElement(name = "AdditionalInfo")
    protected String additionalInfo;

}
