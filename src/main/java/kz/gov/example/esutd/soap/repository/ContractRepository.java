package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    
    List<Contract> findByEmployeeIin(String iin);
    
    List<Contract> findByEmployerEmployerId(String employerId);
    
    List<Contract> findByIsActiveTrue();
    
    List<Contract> findByIsActiveTrueAndEndDateBefore(LocalDate date);
    
    List<Contract> findByTerminationDateBetween(LocalDate startDate, LocalDate endDate);
} 