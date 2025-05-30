
package kz.gov.example.esutd.soap.model;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Java class for AdditionalDocumentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionalDocumentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ContractId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DocumentId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DocumentType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DocumentNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="DocumentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FileMimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FileSize" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalDocumentType", propOrder = {
    "contractId",
    "documentId",
    "documentType",
    "documentNumber",
    "documentDate",
    "documentName",
    "documentDescription",
    "documentData",
    "fileName",
    "fileMimeType",
    "fileSize"
})
public class AdditionalDocumentType extends BaseJaxbType {

    @XmlElement(name = "ContractId", required = true)
    protected String contractId;
    
    @XmlElement(name = "DocumentId", required = true)
    protected String documentId;
    
    @XmlElement(name = "DocumentType", required = true)
    protected String documentType;
    
    @XmlElement(name = "DocumentNumber")
    protected String documentNumber;
    
    @XmlElement(name = "DocumentDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar documentDate;
    
    @XmlElement(name = "DocumentName")
    protected String documentName;
    
    @XmlElement(name = "DocumentDescription")
    protected String documentDescription;
    
    @XmlElement(name = "DocumentData")
    protected String documentData;
    
    @XmlElement(name = "FileName")
    protected String fileName;
    
    @XmlElement(name = "FileMimeType")
    protected String fileMimeType;
    
    @XmlElement(name = "FileSize")
    protected Long fileSize;

}
