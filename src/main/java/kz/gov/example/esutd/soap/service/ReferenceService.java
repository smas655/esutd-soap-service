package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.config.ReferenceDataConfig;
import kz.gov.example.esutd.soap.model.entity.Reference;
import kz.gov.example.esutd.soap.repository.ReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReferenceService {

    private final ReferenceRepository referenceRepository;
    private final ReferenceDataConfig config;
    private final ResourceLoader resourceLoader;
    
    private final Map<String, Map<String, String>> referenceData = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        if (config.isExcelSource() && config.getExcel().isEnabled()) {
            try {
                loadExcelData();
                log.info("Reference data loaded from Excel files successfully");
            } catch (IOException e) {
                log.error("Failed to load reference data from Excel files", e);
                throw new RuntimeException("Failed to load reference data from Excel files", e);
            }
        } else {
            log.info("Using database as reference data source");
        }
    }
    
    private void loadExcelData() throws IOException {
        if (config.getExcel().getPositionFile() != null) {
            Resource resource = resourceLoader.getResource(config.getExcel().getPositionFile());
            loadExcelFile("positions", resource, 
                config.getExcel().getPosition().getCodeColumn(), 
                config.getExcel().getPosition().getNameColumn());
        }
        
        if (config.getExcel().getTerminationReasonFile() != null) {
            Resource resource = resourceLoader.getResource(config.getExcel().getTerminationReasonFile());
            loadExcelFile("terminationReasons", resource,
                config.getExcel().getTerminationReason().getCodeColumn(),
                config.getExcel().getTerminationReason().getNameColumn());
        }
        
        if (config.getExcel().getWorkingHoursFile() != null) {
            Resource resource = resourceLoader.getResource(config.getExcel().getWorkingHoursFile());
            loadExcelFile("workingHours", resource,
                config.getExcel().getWorkingHours().getCodeColumn(),
                config.getExcel().getWorkingHours().getNameColumn());
        }
        
        if (config.getExcel().getWorkConditionsFile() != null) {
            Resource resource = resourceLoader.getResource(config.getExcel().getWorkConditionsFile());
            loadExcelFile("workConditions", resource,
                config.getExcel().getWorkConditions().getCodeColumn(),
                config.getExcel().getWorkConditions().getNameColumn());
        }
    }
    
    private void loadExcelFile(String refName, Resource resource, String codeColumn, String nameColumn) throws IOException {
        Map<String, String> data = new HashMap<>();
        try (InputStream is = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Find header row and column indices
            Row headerRow = sheet.getRow(0);
            int codeColIdx = -1;
            int nameColIdx = -1;
            
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String headerValue = cell.getStringCellValue().trim();
                    if (headerValue.equalsIgnoreCase(codeColumn)) {
                        codeColIdx = i;
                    } else if (headerValue.equalsIgnoreCase(nameColumn)) {
                        nameColIdx = i;
                    }
                }
            }
            
            if (codeColIdx == -1 || nameColIdx == -1) {
                throw new IllegalStateException(String.format(
                    "Required columns not found in %s. Looking for CODE='%s' and NAME='%s'",
                    refName, codeColumn, nameColumn));
            }
            
            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell codeCell = row.getCell(codeColIdx);
                    Cell nameCell = row.getCell(nameColIdx);
                    
                    if (codeCell != null && nameCell != null) {
                        String code = getCellValueAsString(codeCell);
                        String name = getCellValueAsString(nameCell);
                        
                        if (StringUtils.hasText(code) && StringUtils.hasText(name)) {
                            data.put(code, name);
                        }
                    }
                }
            }
        }
        
        referenceData.put(refName, data);
        log.info("Loaded {} entries for reference: {}", data.size(), refName);
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    
    public boolean isPositionValid(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }
        
        if (config.isExcelSource()) {
            Map<String, String> positions = referenceData.get("positions");
            return positions != null && positions.containsKey(code);
        } else {
            return referenceRepository.existsByReferenceTypeAndCodeAndIsActiveTrue("POSITION", code);
        }
    }
    
    public String getPositionName(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }
        
        if (config.isExcelSource()) {
            Map<String, String> positions = referenceData.get("positions");
            return positions != null ? positions.get(code) : null;
        } else {
            Optional<Reference> reference = referenceRepository.findByReferenceTypeAndCodeAndIsActiveTrue("POSITION", code);
            return reference.map(Reference::getNameRu).orElse(null);
        }
    }
    
    public boolean isTerminationReasonValid(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }
        
        if (config.isExcelSource()) {
            Map<String, String> reasons = referenceData.get("terminationReasons");
            return reasons != null && reasons.containsKey(code);
        } else {
            return referenceRepository.existsByReferenceTypeAndCodeAndIsActiveTrue("TERMINATION_REASON", code);
        }
    }
    
    public String getTerminationReasonName(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }
        
        if (config.isExcelSource()) {
            Map<String, String> reasons = referenceData.get("terminationReasons");
            return reasons != null ? reasons.get(code) : null;
        } else {
            Optional<Reference> reference = referenceRepository.findByReferenceTypeAndCodeAndIsActiveTrue("TERMINATION_REASON", code);
            return reference.map(Reference::getNameRu).orElse(null);
        }
    }
    
    public boolean isWorkingHoursValid(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }
        
        if (config.isExcelSource()) {
            Map<String, String> hours = referenceData.get("workingHours");
            return hours != null && hours.containsKey(code);
        } else {
            return referenceRepository.existsByReferenceTypeAndCodeAndIsActiveTrue("WORKING_HOURS", code);
        }
    }
    
    public boolean isWorkConditionsValid(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }
        
        if (config.isExcelSource()) {
            Map<String, String> conditions = referenceData.get("workConditions");
            return conditions != null && conditions.containsKey(code);
        } else {
            return referenceRepository.existsByReferenceTypeAndCodeAndIsActiveTrue("WORK_CONDITIONS", code);
        }
    }
    
    public Map<String, String> getAllPositions() {
        if (config.isExcelSource()) {
            return new HashMap<>(referenceData.getOrDefault("positions", Collections.emptyMap()));
        } else {
            Map<String, String> result = new HashMap<>();
            List<Reference> references = referenceRepository.findByReferenceTypeAndIsActiveTrue("POSITION");
            for (Reference ref : references) {
                result.put(ref.getCode(), ref.getNameRu());
            }
            return result;
        }
    }
    
    @Transactional(readOnly = true)
    public boolean isKatoCodeValid(String code) {
        return referenceRepository.existsByReferenceTypeAndCodeAndIsActiveTrue("KATO", code);
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