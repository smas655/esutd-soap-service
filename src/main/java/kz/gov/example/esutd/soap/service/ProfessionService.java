package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfessionService {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional(readOnly = true)
    public List<ProfessionReference> getAllSelectableProfessions() {
        TypedQuery<ProfessionReference> query = entityManager.createQuery(
                "SELECT p FROM ProfessionReference p " +
                "WHERE p.isActive = true AND p.isSelectable = true " +
                "ORDER BY p.nameRu", ProfessionReference.class);
        
        return query.getResultList();
    }
    
    @Transactional(readOnly = true)
    public List<ProfessionReference> getProfessionsByProfArea(String profAreaCode) {
        if (profAreaCode == null || profAreaCode.isEmpty()) {
            return Collections.emptyList();
        }
        
        TypedQuery<String> codesQuery = entityManager.createQuery(
                "SELECT l.professionCode FROM ProfAreaToProfession l " +
                "WHERE l.profAreaCode = :profAreaCode AND l.isActive = true", String.class);
        codesQuery.setParameter("profAreaCode", profAreaCode);
        
        List<String> professionCodes = codesQuery.getResultList();
        
        if (professionCodes.isEmpty()) {
            return Collections.emptyList();
        }
        
        TypedQuery<ProfessionReference> query = entityManager.createQuery(
                "SELECT p FROM ProfessionReference p " +
                "WHERE p.code IN :codes AND p.isActive = true AND p.isSelectable = true " +
                "ORDER BY p.nameRu", ProfessionReference.class);
        query.setParameter("codes", professionCodes);
        
        return query.getResultList();
    }
    
    @Transactional(readOnly = true)
    public List<ProfessionalSkill> getSkillsByProfession(String professionCode) {
        if (professionCode == null || professionCode.isEmpty()) {
            return Collections.emptyList();
        }
        
        TypedQuery<String> codesQuery = entityManager.createQuery(
                "SELECT l.skillCode FROM ProfessionToSkill l " +
                "WHERE l.professionCode = :professionCode AND l.isActive = true", String.class);
        codesQuery.setParameter("professionCode", professionCode);
        
        List<String> skillCodes = codesQuery.getResultList();
        
        if (skillCodes.isEmpty()) {
            return Collections.emptyList();
        }
        
        TypedQuery<ProfessionalSkill> query = entityManager.createQuery(
                "SELECT s FROM ProfessionalSkill s " +
                "WHERE s.code IN :codes AND s.isActive = true AND s.isSelectable = true " +
                "ORDER BY s.nameRu", ProfessionalSkill.class);
        query.setParameter("codes", skillCodes);
        
        return query.getResultList();
    }
    
    @Transactional(readOnly = true)
    public List<ProfessionalArea> getAllProfessionalAreas() {
        TypedQuery<ProfessionalArea> query = entityManager.createQuery(
                "SELECT a FROM ProfessionalArea a " +
                "WHERE a.isActive = true " +
                "ORDER BY a.nameRu", ProfessionalArea.class);
        
        return query.getResultList();
    }
    
    @Transactional(readOnly = true)
    public boolean isProfessionValid(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM ProfessionReference p " +
                "WHERE p.code = :code AND p.isActive = true", Long.class);
        query.setParameter("code", code);
        
        return query.getSingleResult() > 0;
    }
    
    @Transactional(readOnly = true)
    public boolean isProfessionalAreaValid(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(a) FROM ProfessionalArea a " +
                "WHERE a.code = :code AND a.isActive = true", Long.class);
        query.setParameter("code", code);
        
        return query.getSingleResult() > 0;
    }
} 