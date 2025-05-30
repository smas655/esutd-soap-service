
package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContractTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ContractTypeEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INDEFINITE"/&gt;
 *     &lt;enumeration value="FIXED_TERM_ONE_YEAR_PLUS"/&gt;
 *     &lt;enumeration value="SEASONAL"/&gt;
 *     &lt;enumeration value="SPECIFIC_WORK"/&gt;
 *     &lt;enumeration value="TEMPORARY_REPLACEMENT"/&gt;
 *     &lt;enumeration value="SEASONAL_WORK"/&gt;
 *     &lt;enumeration value="FOREIGN_LABOR_PERMIT"/&gt;
 *     &lt;enumeration value="CHILD_CARE_LEAVE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ContractTypeEnum")
@XmlEnum
public enum ContractTypeEnum {

    INDEFINITE,
    FIXED_TERM_ONE_YEAR_PLUS,
    SEASONAL,
    SPECIFIC_WORK,
    TEMPORARY_REPLACEMENT,
    SEASONAL_WORK,
    FOREIGN_LABOR_PERMIT,
    CHILD_CARE_LEAVE;

    public String value() {
        return name();
    }

    public static ContractTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}
