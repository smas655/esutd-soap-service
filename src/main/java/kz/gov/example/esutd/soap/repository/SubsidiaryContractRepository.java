package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.Contract;
import kz.gov.example.esutd.soap.model.entity.SubsidiaryContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с дополнительными соглашениями к трудовому договору
 */
@Repository
public interface SubsidiaryContractRepository extends JpaRepository<SubsidiaryContract, Long> {
    
    /**
     * Найти дополнительное соглашение по его уникальному идентификатору
     * 
     * @param subsidiaryContractId уникальный идентификатор дополнительного соглашения
     * @return дополнительное соглашение, если найдено
     */
    Optional<SubsidiaryContract> findBySubsidiaryContractId(String subsidiaryContractId);
    
    /**
     * Найти все дополнительные соглашения по номеру
     * 
     * @param subsidiaryContractNumber номер дополнительного соглашения
     * @return список дополнительных соглашений с указанным номером
     */
    List<SubsidiaryContract> findBySubsidiaryContractNumber(String subsidiaryContractNumber);
    
    /**
     * Найти все дополнительные соглашения по типу
     * 
     * @param subsidiaryContractType тип дополнительного соглашения
     * @return список дополнительных соглашений указанного типа
     */
    List<SubsidiaryContract> findBySubsidiaryContractType(String subsidiaryContractType);
    
    /**
     * Найти все дополнительные соглашения для указанного трудового договора
     * 
     * @param contract трудовой договор
     * @return список дополнительных соглашений для указанного договора
     */
    List<SubsidiaryContract> findByContract(Contract contract);
    
    /**
     * Найти все дополнительные соглашения, созданные в указанный период
     * 
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * @return список дополнительных соглашений, созданных в указанный период
     */
    List<SubsidiaryContract> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}
