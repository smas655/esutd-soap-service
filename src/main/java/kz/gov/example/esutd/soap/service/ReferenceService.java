package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.entity.Reference;
import kz.gov.example.esutd.soap.repository.ReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReferenceService {

    private final ReferenceRepository referenceRepository;
    
    private static final String TERMINATION_REASON_TYPE = "TERMINATION_REASON";
    private static final String POSITION_TYPE = "POSITION";
    private static final String KATO_TYPE = "KATO";

    @Transactional(readOnly = true)
    public List<Reference> getTerminationReasons() {
        return referenceRepository.findByReferenceTypeAndIsActiveTrue(TERMINATION_REASON_TYPE);
    }

    @Transactional(readOnly = true)
    public List<Reference> getPositions() {
        return referenceRepository.findByReferenceTypeAndIsActiveTrue(POSITION_TYPE);
    }

    @Transactional(readOnly = true)
    public List<Reference> getKatoCodes() {
        return referenceRepository.findByReferenceTypeAndIsActiveTrue(KATO_TYPE);
    }

    @Transactional(readOnly = true)
    public boolean isTerminationReasonValid(String code) {
        return referenceRepository.existsByReferenceTypeAndCode(TERMINATION_REASON_TYPE, code);
    }

    @Transactional(readOnly = true)
    public boolean isPositionValid(String code) {
        return referenceRepository.existsByReferenceTypeAndCode(POSITION_TYPE, code);
    }

    @Transactional(readOnly = true)
    public boolean isKatoCodeValid(String code) {
        return referenceRepository.existsByReferenceTypeAndCode(KATO_TYPE, code);
    }

    @Transactional
    public Reference saveReference(Reference reference) {
        log.info("Saving reference: type={}, code={}", reference.getReferenceType(), reference.getCode());
        return referenceRepository.save(reference);
    }

    @Transactional
    public void updateReferences(List<Reference> references) {
        log.info("Updating {} references", references.size());
        for (Reference reference : references) {
            Optional<Reference> existingReference = referenceRepository.findByReferenceTypeAndCodeAndIsActiveTrue(
                    reference.getReferenceType(), reference.getCode());

            if (existingReference.isPresent()) {
                Reference ref = existingReference.get();
                ref.setNameRu(reference.getNameRu());
                ref.setNameKz(reference.getNameKz());
                ref.setDescription(reference.getDescription());
                referenceRepository.save(ref);
            } else {
                referenceRepository.save(reference);
            }
        }
    }

    @Transactional
    public void deactivateReference(String referenceType, String code) {
        log.info("Deactivating reference: type={}, code={}", referenceType, code);
        Optional<Reference> reference = referenceRepository.findByReferenceTypeAndCodeAndIsActiveTrue(referenceType, code);
        reference.ifPresent(ref -> {
            ref.setIsActive(false);
            referenceRepository.save(ref);
        });
    }
} 