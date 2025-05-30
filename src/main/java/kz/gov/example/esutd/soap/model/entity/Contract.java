package kz.gov.example.esutd.soap.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@NoArgsConstructor
@Data
@Slf4j
public class Contract {

    @Id
    @Column(name = "contract_id")
    private String contractId;

    @Column(name = "contract_number", nullable = false, length = 60)
    private String contractNumber;

    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @Column(name = "contract_duration_type", length = 100)
    private String contractDurationType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "position_code", nullable = false, length = 1024)
    private String positionCode;

    @Column(name = "work_type", length = 100)
    private String workType;

    @Column(name = "remote_work")
    private Boolean remoteWork;

    @Column(name = "work_place_address")
    private String workPlaceAddress;

    @Column(name = "work_place_kato")
    private String workPlaceKato;

    @Column(name = "work_place_country")
    private String workPlaceCountry;

    @Column(name = "work_hours", length = 100)
    private String workHours;

    @Column(name = "tariff_rate")
    private Double tariffRate;

    @Column(name = "work_conditions")
    private String workConditions;

    @Column(name = "work_condition_code")
    private String workConditionCode;

    @Column(name = "contract_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    
    @Column(name = "general_skills")
    private String generalSkills;

    @Column(name = "professional_skills")
    private String professionalSkills;

    @Column(name = "department")
    private String department;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "termination_reason")
    private String terminationReason;

    @Column(name = "termination_reason_code")
    private String terminationReasonCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    public enum ContractType {
        /**
         * На неопределенный срок
         */
        INDEFINITE,
        
        /**
         * На определенный срок не менее одного года
         */
        FIXED_TERM_ONE_YEAR_PLUS,
        
        /**
         * На определенный срок для выполнения сезонных работ
         */
        SEASONAL,
        
        /**
         * На время выполнения определенной работы
         */
        SPECIFIC_WORK,
        
        /**
         * На время замещения временно отсутствующего работника
         */
        TEMPORARY_REPLACEMENT,
        
        /**
         * На время исполнения сезонной работы
         */
        SEASONAL_WORK,
        
        /**
         * В пределах срока ИРС
         */
        FOREIGN_LABOR_PERMIT,
        
        /**
         * По дням социального отпуска по уходу за ребенком
         */
        CHILD_CARE_LEAVE
    }
}