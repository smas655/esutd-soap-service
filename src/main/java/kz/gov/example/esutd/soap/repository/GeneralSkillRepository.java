package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.GeneralSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы со справочником общих навыков
 */
@Repository
public interface GeneralSkillRepository extends JpaRepository<GeneralSkill, Long> {
    
    /**
     * Находит все активные общие навыки
     * 
     * @return список активных общих навыков
     */
    List<GeneralSkill> findByIsActiveTrue();
    
    /**
     * Находит общий навык по коду
     * 
     * @param code код общего навыка
     * @return общий навык
     */
    GeneralSkill findByCode(String code);
    
    /**
     * Находит активный общий навык по коду
     * 
     * @param code код общего навыка
     * @return активный общий навык
     */
    GeneralSkill findByCodeAndIsActiveTrue(String code);
}
