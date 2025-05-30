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

/**
 * Класс для представления информации о контракте
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractType", propOrder = {
    "contractId",
    "contractNumber",
    "contractDate",
    "contractDurationType",
    "startDate",
    "endDate",
    "contractType",
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
    "workConditionCode",
    "generalSkills",
    "professionalSkills",
    "department"
})
public class ContractType extends BaseJaxbType {

    @XmlElement(name = "ContractId", required = true)
    protected String contractId;
    @XmlElement(name = "ContractNumber", required = true)
    protected String contractNumber;
    @XmlElement(name = "ContractDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar contractDate;
    @XmlElement(name = "ContractDurationType")
    protected String contractDurationType;
    @XmlElement(name = "StartDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    @XmlElement(name = "ContractType", required = true)
    @XmlSchemaType(name = "string")
    protected ContractTypeEnum contractType;
    @XmlElement(name = "Position", required = true)
    protected String position;
    @XmlElement(name = "PositionCode", required = true)
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
    @XmlElement(name = "GeneralSkills")
    protected String generalSkills;
    @XmlElement(name = "ProfessionalSkills")
    protected String professionalSkills;
    @XmlElement(name = "Department")
    protected String department;
}
