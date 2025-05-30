package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс ответа на запрос синхронизации контракта
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "status",
    "errorMessage"
})
@XmlRootElement(name = "ContractSyncResponse")
public class ContractSyncResponse extends BaseJaxbType {

    @XmlElement(name = "Status", required = true)
    @XmlSchemaType(name = "string")
    protected StatusType status;
    
    @XmlElement(name = "ErrorMessage")
    protected String errorMessage;
}
