package kz.gov.example.esutd.soap.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO для данных трудового договора
 */
@Data
public class ContractData {
    
    private String contractId;
    private String contractNumber;
    private LocalDate contractDate;
    private String contractDurationType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String position;
    private String positionCode;
    private String workType;
    private Boolean remoteWork;
    private String workPlaceAddress;
    private String workPlaceKato;
    private String workPlaceCountry;
    private String workHours;
    private Double tariffRate;
    private String workConditions;
    private String workConditionCode;
    private EmployerData employer;
    private EmployeeData employee;
    private String generalSkills;
    private String professionalSkills;
    private String department;
    private List<SubsidiaryContractData> subsidiaryContracts;
    private List<AdditionalDocumentData> additionalDocuments;
    
    /**
     * DTO для данных работодателя
     */
    @Data
    public static class EmployerData {
        private String bin;
        private String name;
        private String addressKato;
        private String addressCountry;
        private String addressText;
    }
    
    /**
     * DTO для данных работника
     */
    @Data
    public static class EmployeeData {
        private String iin;
        private String firstName;
        private String lastName;
        private String middleName;
        private LocalDate birthDate;
        private String gender;
        private String citizenship;
    }
    
    /**
     * DTO для данных дополнительного соглашения
     */
    @Data
    public static class SubsidiaryContractData {
        private String subsidiaryContractId;
        private String subsidiaryContractNumber;
        private LocalDate subsidiaryContractDate;
        private String subsidiaryContractType;
        private String subsidiaryContractReason;
        private LocalDate startDate;
        private LocalDate endDate;
        private String position;
        private String positionCode;
        private String workType;
        private Boolean remoteWork;
        private String workPlaceAddress;
        private String workPlaceKato;
        private String workPlaceCountry;
        private String workHours;
        private Double tariffRate;
        private String workConditions;
        private String workConditionCode;
    }
    
    /**
     * DTO для данных дополнительного документа
     */
    @Data
    public static class AdditionalDocumentData {
        private String documentId;
        private String documentType;
        private String documentNumber;
        private LocalDate documentDate;
        private String documentName;
        private String documentDescription;
        private String documentData;
        private String fileName;
        private String fileMimeType;
        private Long fileSize;
    }
}
