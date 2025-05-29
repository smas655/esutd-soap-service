package kz.gov.example.esutd.soap.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reference_data")
@Data
@NoArgsConstructor
public class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_type", nullable = false)
    private String referenceType;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name_ru", nullable = false)
    private String nameRu;

    @Column(name = "name_kz")
    private String nameKz;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "action_type")
    private String actionType;
    
    @Column(name = "is_actual")
    private Boolean isActual = true;
    
    @Column(name = "actual_date")
    private LocalDateTime actualDate;
    
    @Column(name = "is_selectable")
    private Boolean isSelectable = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        if (actualDate == null) {
            actualDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
} 