package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.NkzClassifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы со справочником НКЗ (профессий)
 */
@Repository
public interface NkzClassifierRepository extends JpaRepository<NkzClassifier, String> {
    
    /**
     * Находит все активные записи справочника НКЗ
     * 
     * @return список активных записей
     */
    List<NkzClassifier> findByIsActiveTrue();
    
    /**
     * Находит все записи справочника НКЗ по уровню
     * 
     * @param level уровень классификации
     * @return список записей
     */
    List<NkzClassifier> findByLevel(Integer level);
    
    /**
     * Находит все активные записи справочника НКЗ по уровню
     * 
     * @param level уровень классификации
     * @return список активных записей
     */
    List<NkzClassifier> findByLevelAndIsActiveTrue(Integer level);
    
    /**
     * Находит все записи справочника НКЗ по родительскому коду
     * 
     * @param parentCode родительский код
     * @return список записей
     */
    List<NkzClassifier> findByParentCode(String parentCode);
    
    /**
     * Находит все активные записи справочника НКЗ по родительскому коду
     * 
     * @param parentCode родительский код
     * @return список активных записей
     */
    List<NkzClassifier> findByParentCodeAndIsActiveTrue(String parentCode);
}
