package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для представления информации о документе
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentInfoType", propOrder = {
    "documentType",
    "documentNumber",
    "issueDate",
    "issuedBy",
    "expiryDate"
})
public class DocumentInfoType extends BaseJaxbType {

    @XmlElement(name = "DocumentType", required = true)
    protected String documentType;
    
    @XmlElement(name = "DocumentNumber", required = true)
    protected String documentNumber;
    
    @XmlElement(name = "IssueDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar issueDate;
    
    @XmlElement(name = "IssuedBy", required = true)
    protected String issuedBy;
    
    @XmlElement(name = "ExpiryDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar expiryDate;
}
