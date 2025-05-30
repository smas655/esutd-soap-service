
package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для представления информации о работодателе
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmployerType", propOrder = {
    "employerId",
    "bin",
    "name",
    "katoCode",
    "employerType",
    "legalAddress",
    "contacts",
    "bankName",
    "bik",
    "accountNumber"
})
public class EmployerType extends BaseJaxbType {

    @XmlElement(name = "EmployerId", required = true)
    protected String employerId;
    @XmlElement(name = "BIN", required = true)
    protected String bin;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "KatoCode")
    protected String katoCode;
    @XmlElement(name = "EmployerType")
    protected String employerType;
    @XmlElement(name = "LegalAddress", required = true)
    protected AddressType legalAddress;
    @XmlElement(name = "Contacts")
    protected ContactsType contacts;
    @XmlElement(name = "BankName")
    protected String bankName;
    @XmlElement(name = "BIK")
    protected String bik;
    @XmlElement(name = "AccountNumber")
    protected String accountNumber;

}
