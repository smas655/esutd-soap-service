package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.ProfessionalArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы со справочником профессиональных областей
 */
@Repository
public interface ProfessionalAreaRepository extends JpaRepository<ProfessionalArea, Long> {
    
    /**
     * Находит все активные профессиональные области
     * 
     * @return список активных профессиональных областей
     */
    List<ProfessionalArea> findByIsActiveTrue();
    
    /**
     * Находит профессиональную область по коду
     * 
     * @param code код профессиональной области
     * @return профессиональная область
     */
    ProfessionalArea findByCode(String code);
    
    /**
     * Находит активную профессиональную область по коду
     * 
     * @param code код профессиональной области
     * @return активная профессиональная область
     */
    ProfessionalArea findByCodeAndIsActiveTrue(String code);
}
