package kz.gov.example.esutd.soap.model;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestInfoType", propOrder = {
    "requestId",
    "timestamp",
    "sourceSystem"
})
public class RequestInfoType extends BaseJaxbType {

    @XmlElement(name = "RequestId", required = true)
    protected String requestId;
    
    @XmlElement(name = "Timestamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;
    
    @XmlElement(name = "SourceSystem", required = true)
    protected String sourceSystem;
}
