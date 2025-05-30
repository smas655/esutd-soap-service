
package kz.gov.example.esutd.soap.model;

import java.math.BigDecimal;
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
@XmlType(name = "SubsidiaryContractType", propOrder = {
    "contractId",
    "subsidiaryContractId",
    "subsidiaryContractNumber",
    "subsidiaryContractDate",
    "subsidiaryContractType",
    "subsidiaryContractReason",
    "startDate",
    "endDate",
    "position",
    "positionCode",
    "workType",
    "remoteWork",
    "workPlaceAddress",
    "workPlaceKato",
    "workPlaceCountry",
    "workHours",
    "tariffRate",
    "workConditions",
    "workConditionCode"
})
public class SubsidiaryContractType extends BaseJaxbType {

    @XmlElement(name = "ContractId", required = true)
    protected String contractId;
    @XmlElement(name = "SubsidiaryContractId", required = true)
    protected String subsidiaryContractId;
    @XmlElement(name = "SubsidiaryContractNumber", required = true)
    protected String subsidiaryContractNumber;
    @XmlElement(name = "SubsidiaryContractDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar subsidiaryContractDate;
    @XmlElement(name = "SubsidiaryContractType")
    protected String subsidiaryContractType;
    @XmlElement(name = "SubsidiaryContractReason")
    protected String subsidiaryContractReason;
    @XmlElement(name = "StartDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    @XmlElement(name = "Position")
    protected String position;
    @XmlElement(name = "PositionCode")
    protected String positionCode;
    @XmlElement(name = "WorkType")
    protected String workType;
    @XmlElement(name = "RemoteWork")
    protected Boolean remoteWork;
    @XmlElement(name = "WorkPlaceAddress")
    protected String workPlaceAddress;
    @XmlElement(name = "WorkPlaceKato")
    protected String workPlaceKato;
    @XmlElement(name = "WorkPlaceCountry")
    protected String workPlaceCountry;
    @XmlElement(name = "WorkHours")
    protected String workHours;
    @XmlElement(name = "TariffRate")
    protected BigDecimal tariffRate;
    @XmlElement(name = "WorkConditions")
    protected String workConditions;
    @XmlElement(name = "WorkConditionCode")
    protected String workConditionCode;

}
