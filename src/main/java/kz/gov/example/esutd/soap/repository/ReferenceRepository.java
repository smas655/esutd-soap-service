package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    
    List<Reference> findByReferenceTypeAndIsActiveTrue(String referenceType);
    
    Optional<Reference> findByReferenceTypeAndCodeAndIsActiveTrue(String referenceType, String code);
    
    boolean existsByReferenceTypeAndCodeAndIsActiveTrue(String referenceType, String code);

    long countByReferenceTypeAndCreatedAt(String referenceType, LocalDate createdAt);
} 