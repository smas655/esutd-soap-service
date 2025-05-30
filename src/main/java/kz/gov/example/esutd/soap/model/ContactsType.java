
package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для представления контактной информации
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactsType", propOrder = {
    "phone",
    "email"
})
public class ContactsType extends BaseJaxbType {

    @XmlElement(name = "Phone")
    protected String phone;
    
    @XmlElement(name = "Email")
    protected String email;

}
