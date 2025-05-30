
package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс запроса синхронизации контракта
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "requestInfo",
    "contractData"
})
@XmlRootElement(name = "ContractSyncRequest")
public class ContractSyncRequest extends BaseJaxbType {

    @XmlElement(name = "RequestInfo", required = true)
    protected RequestInfoType requestInfo;
    
    @XmlElement(name = "ContractData", required = true)
    protected ContractDataType contractData;

}
