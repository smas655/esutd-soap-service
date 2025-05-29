package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, String> {
    
    Optional<Employer> findByBin(String bin);
    
    boolean existsByBin(String bin);
} 