package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.ProfessionalSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы со справочником профессиональных навыков
 */
@Repository
public interface ProfessionalSkillRepository extends JpaRepository<ProfessionalSkill, Long> {
    
    /**
     * Находит все активные профессиональные навыки
     * 
     * @return список активных профессиональных навыков
     */
    List<ProfessionalSkill> findByIsActiveTrue();
    
    /**
     * Находит все выбираемые профессиональные навыки
     * 
     * @return список выбираемых профессиональных навыков
     */
    List<ProfessionalSkill> findByIsSelectableTrue();
    
    /**
     * Находит все активные и выбираемые профессиональные навыки
     * 
     * @return список активных и выбираемых профессиональных навыков
     */
    List<ProfessionalSkill> findByIsActiveTrueAndIsSelectableTrue();
    
    /**
     * Находит профессиональный навык по коду
     * 
     * @param code код профессионального навыка
     * @return профессиональный навык
     */
    ProfessionalSkill findByCode(String code);
}
