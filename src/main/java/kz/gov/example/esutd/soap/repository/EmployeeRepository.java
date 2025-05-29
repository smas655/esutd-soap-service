package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    Optional<Employee> findByIin(String iin);
    
    boolean existsByIin(String iin);
} 