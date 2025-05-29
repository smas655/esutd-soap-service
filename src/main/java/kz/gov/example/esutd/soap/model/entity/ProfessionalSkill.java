package kz.gov.example.esutd.soap.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "professional_skills")
@Data
public class ProfessionalSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_kz")
    private String nameKz;

    @Column(name = "description")
    private String description;

    @Column(name = "fl_check")
    private String flCheck;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_selectable")
    private Boolean isSelectable;

    @Column(name = "created_at")
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