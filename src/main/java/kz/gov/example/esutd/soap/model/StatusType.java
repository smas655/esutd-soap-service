
package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "StatusType")
@XmlEnum
public enum StatusType {

    OK,
    ERROR;

    public String value() {
        return name();
    }

    public static StatusType fromValue(String v) {
        return valueOf(v);
    }
}
