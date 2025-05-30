package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    
    /**
     * Найти трудовой договор по его уникальному идентификатору
     * 
     * @param contractId уникальный идентификатор трудового договора
     * @return трудовой договор, если найден
     */
    Optional<Contract> findByContractId(String contractId);
    
    List<Contract> findByEmployeeIin(String iin);
    
    List<Contract> findByEmployerEmployerId(String employerId);
    
    List<Contract> findByIsActiveTrue();
    
    List<Contract> findByIsActiveTrueAndEndDateBefore(LocalDate date);
    
    List<Contract> findByTerminationDateBetween(LocalDate startDate, LocalDate endDate);
} 