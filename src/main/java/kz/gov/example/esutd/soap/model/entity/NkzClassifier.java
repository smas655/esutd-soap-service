package kz.gov.example.esutd.soap.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Справочник НКЗ (профессий)
 */
@Entity
@Table(name = "nkz_classifier")
@NoArgsConstructor
@Data
public class NkzClassifier {
    
    @Id
    @Column(name = "code", length = 10)
    private String code; // 10-значный код
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "action", length = 20)
    private String action;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "parent_code", length = 10)
    private String parentCode;
    
    @Column(name = "level")
    private Integer level;
}
