package kz.gov.example.esutd.soap.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "general_skills")
@NoArgsConstructor
@Data
public class GeneralSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name_ru", nullable = false)
    private String nameRu;

    @Column(name = "name_kz")
    private String nameKz;

    @Column(name = "description")
    private String description;

    @Column(name = "fl_check")
    private String flCheck;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_selectable")
    private Boolean isSelectable = true;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        if ("x".equalsIgnoreCase(flCheck)) {
            isSelectable = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
        if ("x".equalsIgnoreCase(flCheck)) {
            isSelectable = false;
        }
    }
} 