package kz.gov.example.esutd.soap.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Сущность для хранения информации о дополнительных соглашениях к трудовому договору
 */
@Entity
@Table(name = "subsidiary_contracts")
@NoArgsConstructor
@Data
public class SubsidiaryContract {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "subsidiary_contract_id", nullable = false)
    private String subsidiaryContractId;
    
    @Column(name = "subsidiary_contract_number", nullable = false, length = 60)
    private String subsidiaryContractNumber;
    
    @Column(name = "subsidiary_contract_date", nullable = false)
    private LocalDate subsidiaryContractDate;
    
    @Column(name = "subsidiary_contract_type", length = 100)
    private String subsidiaryContractType;
    
    @Column(name = "subsidiary_contract_reason", length = 255)
    private String subsidiaryContractReason;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "position")
    private String position;
    
    @Column(name = "position_code", length = 1024)
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
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
}
