
package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="OperationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CREATE_CONTRACT"/&gt;
 *     &lt;enumeration value="UPDATE_CONTRACT"/&gt;
 *     &lt;enumeration value="TERMINATE_CONTRACT"/&gt;
 *     &lt;enumeration value="UPDATE_EMPLOYEE"/&gt;
 *     &lt;enumeration value="UPDATE_EMPLOYER"/&gt;
 *     &lt;enumeration value="ADD_SUBSIDIARY_CONTRACT"/&gt;
 *     &lt;enumeration value="UPDATE_SUBSIDIARY_CONTRACT"/&gt;
 *     &lt;enumeration value="ADD_DOCUMENT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "OperationType")
@XmlEnum
public enum OperationType {

    CREATE_CONTRACT,
    UPDATE_CONTRACT,
    TERMINATE_CONTRACT,
    UPDATE_EMPLOYEE,
    UPDATE_EMPLOYER,
    ADD_SUBSIDIARY_CONTRACT,
    UPDATE_SUBSIDIARY_CONTRACT,
    ADD_DOCUMENT;

    public String value() {
        return name();
    }

    public static OperationType fromValue(String v) {
        return valueOf(v);
    }

}
