
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
 * Класс для представления информации о сотруднике
 */
@Getter
@Setter
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
    "contacts",
    "documentInfo",
    "bankName",
    "bik",
    "accountNumber",
    "phoneNumber",
    "hrPhoneNumber"
})
public class EmployeeType extends BaseJaxbType {

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
    @XmlElement(name = "Contacts")
    protected ContactsType contacts;
    @XmlElement(name = "DocumentInfo")
    protected DocumentInfoType documentInfo;
    @XmlElement(name = "BankName")
    protected String bankName;
    @XmlElement(name = "BIK")
    protected String bik;
    @XmlElement(name = "AccountNumber")
    protected String accountNumber;
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    @XmlElement(name = "HrPhoneNumber")
    protected String hrPhoneNumber;

}
