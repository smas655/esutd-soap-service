package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class SpecializedReferenceImportService {

    private static final Logger log = LoggerFactory.getLogger(SpecializedReferenceImportService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private static final String PROFESSION_FILE_PREFIX = "_IZM_SPRPRF";
    private static final String PROFESSIONAL_SKILLS_FILE_PREFIX = "PROFNAVYKI";
    private static final String PROFESSIONAL_AREA_FILE_PREFIX = "PROFOBL";
    private static final String LINK_PROF_AREA_TO_PROFESSION_FILE_PREFIX = "LINK_PROFOBL_NKZ";
    private static final String LINK_PROFESSION_TO_SKILLS_FILE_PREFIX = "LINK_NKZ_PROFNAVYKI";
    private static final String GENERAL_SKILLS_FILE_PREFIX = "OBSCHNAVYKI";

    private static final Set<String> EXCLUDED_COLUMNS = new HashSet<>(Arrays.asList(
        "UPDATED", "VERSION", "CREATE_DATE", "UPDATE_DATE", 
        "TYPE", "SIZE", "FLG_OBL_FILL", "VISIBLE_IN_LIST",
        "EXCLUSION_REASON"
    ));


    @Transactional
    public Map<String, Integer> importFromDirectory(String directoryPath) {
        Map<String, Integer> results = new HashMap<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Указанный путь не существует или не является директорией: " + directoryPath);
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return results;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".xlsx")) {
                String fileName = file.getName().toUpperCase();
                
                try {
                    if (fileName.contains(PROFESSION_FILE_PREFIX)) {
                        results.put("Профессии", importProfessions(file));
                    } else if (fileName.contains(PROFESSIONAL_SKILLS_FILE_PREFIX)) {
                        results.put("Профессиональные навыки", importProfessionalSkills(file));
                    } else if (fileName.contains(PROFESSIONAL_AREA_FILE_PREFIX)) {
                        results.put("Профессиональные области", importProfessionalAreas(file));
                    } else if (fileName.contains(LINK_PROF_AREA_TO_PROFESSION_FILE_PREFIX)) {
                        results.put("Связи проф. областей и профессий", importProfAreaToProfessionLinks(file));
                    } else if (fileName.contains(LINK_PROFESSION_TO_SKILLS_FILE_PREFIX)) {
                        results.put("Связи профессий и навыков", importProfessionToSkillsLinks(file));
                    } else if (fileName.contains(GENERAL_SKILLS_FILE_PREFIX)) {
                        results.put("Общие навыки", importGeneralSkills(file));
                    }
                } catch (Exception e) {
                    log.error("Ошибка при импорте файла {}: {}", file.getName(), e.getMessage(), e);
                }
            }
        }

        return results;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int importProfessions(File file) {
        log.info("Начало импорта профессий из файла: {}", file.getName());
        
        List<Map<String, String>> records = readExcelFile(file);
        int importedCount = 0;
        
        for (Map<String, String> record : records) {
            String code = record.get("CODE");
            if (code == null || code.trim().isEmpty()) {
                continue;
            }
            
            ProfessionReference profession = findEntityByCode(ProfessionReference.class, code);
            
            if (profession == null) {
                profession = new ProfessionReference();
            }
            
            profession.setCode(code);
            profession.setNameRu(record.get("NAME_RU"));
            
            if (record.containsKey("NAME_KZ") && record.get("NAME_KZ") != null) {
                profession.setNameKz(record.get("NAME_KZ"));
            }
            
            if (record.containsKey("ACTION_TYPE")) {
                profession.setActionType(record.getOrDefault("ACTION_TYPE", null));
            }
            
            if (record.containsKey("PARENT_CODE")) {
                profession.setParentCode(record.getOrDefault("PARENT_CODE", null));
            }
            
            if (record.containsKey("GROUP_CODE")) {
                profession.setGroupCode(record.getOrDefault("GROUP_CODE", null));
            }
            
            profession.setIsActive(true);
            profession.setCreatedAt(LocalDate.now());
            
            entityManager.merge(profession);
            importedCount++;
            
            if (importedCount % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        
        log.info("Импорт профессий завершен. Импортировано {} записей", importedCount);
        return importedCount;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int importProfessionalSkills(File file) {
        log.info("Начало импорта профессиональных навыков из файла: {}", file.getName());
        
        List<Map<String, String>> records = readExcelFile(file);
        int importedCount = 0;
        
        for (Map<String, String> record : records) {
            String code = record.get("CODE");
            if (code == null || code.trim().isEmpty()) {
                continue;
            }
            
            ProfessionalSkill skill = findEntityByCode(ProfessionalSkill.class, code);
            
            if (skill == null) {
                skill = new ProfessionalSkill();
            }
            
            skill.setCode(code);
            skill.setNameRu(record.get("NAME_RU"));
            
            if (record.containsKey("NAME_KZ") && record.get("NAME_KZ") != null) {
                skill.setNameKz(record.get("NAME_KZ"));
            }
            
            skill.setDescription(record.getOrDefault("DESCRIPTION", null));
            
            if (record.containsKey("FL_CHECK")) {
                skill.setFlCheck(record.get("FL_CHECK"));
                skill.setIsSelectable(!"x".equalsIgnoreCase(skill.getFlCheck()));
            }
            
            skill.setIsActive(true);
            skill.setCreatedAt(LocalDate.now());
            
            entityManager.merge(skill);
            importedCount++;
            
            if (importedCount % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        
        log.info("Импорт профессиональных навыков завершен. Импортировано {} записей", importedCount);
        return importedCount;
    }
    

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int importProfessionalAreas(File file) {
        log.info("Начало импорта профессиональных областей из файла: {}", file.getName());
        
        List<Map<String, String>> records = readExcelFile(file);
        int importedCount = 0;
        
        for (Map<String, String> record : records) {
            String code = record.get("CODE");
            if (code == null || code.trim().isEmpty()) {
                continue;
            }
            
            ProfessionalArea area = findEntityByCode(ProfessionalArea.class, code);
            
            if (area == null) {
                area = new ProfessionalArea();
            }
            
            area.setCode(code);
            area.setNameRu(record.get("NAME_RU"));
            
            if (record.containsKey("NAME_KZ") && record.get("NAME_KZ") != null) {
                area.setNameKz(record.get("NAME_KZ"));
            }
            
            area.setDescription(record.getOrDefault("DESCRIPTION", null));
            
            area.setIsActive(true);
            area.setCreatedAt(LocalDate.now());
            
            entityManager.merge(area);
            importedCount++;
            
            if (importedCount % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        
        log.info("Импорт профессиональных областей завершен. Импортировано {} записей", importedCount);
        return importedCount;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int importProfAreaToProfessionLinks(File file) {
        log.info("Начало импорта связей профессиональных областей и профессий из файла: {}", file.getName());
        
        List<Map<String, String>> records = readExcelFile(file);
        int importedCount = 0;
        
        for (Map<String, String> record : records) {
            String profAreaCode = record.get("PROFOBL_CODE");
            String professionCode = record.get("NKZ_CODE");
            
            if (profAreaCode == null || profAreaCode.trim().isEmpty() || 
                professionCode == null || professionCode.trim().isEmpty()) {
                continue;
            }
            
            ProfAreaToProfession link = findLinkByAreaAndProfession(profAreaCode, professionCode);
            
            if (link == null) {
                link = new ProfAreaToProfession();
            }
            
            link.setProfAreaCode(profAreaCode);
            link.setProfessionCode(professionCode);
            link.setIsActive(true);
            link.setCreatedAt(LocalDate.now());
            
            entityManager.merge(link);
            importedCount++;
            
            if (importedCount % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        
        log.info("Импорт связей профессиональных областей и профессий завершен. Импортировано {} записей", importedCount);
        return importedCount;
    }
    

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int importProfessionToSkillsLinks(File file) {
        log.info("Начало импорта связей профессий и навыков из файла: {}", file.getName());
        
        List<Map<String, String>> records = readExcelFile(file);
        int importedCount = 0;
        
        for (Map<String, String> record : records) {
            String professionCode = record.get("NKZ_CODE");
            String skillCode = record.get("PROFNAVYK_CODE");
            
            if (professionCode == null || professionCode.trim().isEmpty() || 
                skillCode == null || skillCode.trim().isEmpty()) {
                continue;
            }
            
            ProfessionToSkill link = findLinkByProfessionAndSkill(professionCode, skillCode);
            
            if (link == null) {
                link = new ProfessionToSkill();
            }
            
            link.setProfessionCode(professionCode);
            link.setSkillCode(skillCode);
            link.setIsActive(true);
            link.setCreatedAt(LocalDate.now());
            
            entityManager.merge(link);
            importedCount++;
            
            if (importedCount % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        
        log.info("Импорт связей профессий и навыков завершен. Импортировано {} записей", importedCount);
        return importedCount;
    }
    

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int importGeneralSkills(File file) {
        log.info("Начало импорта общих навыков из файла: {}", file.getName());
        
        List<Map<String, String>> records = readExcelFile(file);
        int importedCount = 0;
        
        for (Map<String, String> record : records) {
            String code = record.get("CODE");
            if (code == null || code.trim().isEmpty()) {
                continue;
            }
            
            GeneralSkill skill = findEntityByCode(GeneralSkill.class, code);
            
            if (skill == null) {
                skill = new GeneralSkill();
            }
            
            skill.setCode(code);
            skill.setNameRu(record.get("NAME_RU"));
            
            if (record.containsKey("NAME_KZ") && record.get("NAME_KZ") != null) {
                skill.setNameKz(record.get("NAME_KZ"));
            }
            
            skill.setDescription(record.getOrDefault("DESCRIPTION", null));
            
            skill.setIsActive(true);
            skill.setCreatedAt(LocalDate.now());
            
            entityManager.merge(skill);
            importedCount++;
            
            if (importedCount % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        
        log.info("Импорт общих навыков завершен. Импортировано {} записей", importedCount);
        return importedCount;
    }
    
    private List<Map<String, String>> readExcelFile(File file) {
        List<Map<String, String>> records = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0); 
            
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            
            for (Cell cell : headerRow) {
                String headerName = cell.getStringCellValue().trim().toUpperCase();
                headers.add(headerName);
            }
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Map<String, String> record = new HashMap<>();
                boolean hasData = false;
                
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) continue;
                    
                    String columnName = headers.get(j);
                    if (EXCLUDED_COLUMNS.contains(columnName)) {
                        continue;
                    }
                    
                    String value = getCellValueAsString(cell);
                    if (value != null && !value.trim().isEmpty()) {
                        record.put(columnName, value);
                        hasData = true;
                    }
                }
                
                if (hasData) {
                    records.add(record);
                }
            }
            
        } catch (IOException e) {
            log.error("Ошибка при чтении Excel-файла: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при чтении Excel-файла", e);
        }
        
        return records;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    double value = cell.getNumericCellValue();
                    if (value == (long) value) {
                        return String.valueOf((long) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    try {
                        return cell.getStringCellValue();
                    } catch (Exception ex) {
                        return null;
                    }
                }
            default:
                return null;
        }
    }
    

    private <T> T findEntityByCode(Class<T> entityClass, String code) {
        try {
            return entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.code = :code", 
                entityClass
            ).setParameter("code", code)
             .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    

    private ProfAreaToProfession findLinkByAreaAndProfession(String profAreaCode, String professionCode) {
        try {
            return entityManager.createQuery(
                "SELECT l FROM ProfAreaToProfession l " +
                "WHERE l.profAreaCode = :profAreaCode AND l.professionCode = :professionCode",
                ProfAreaToProfession.class
            ).setParameter("profAreaCode", profAreaCode)
             .setParameter("professionCode", professionCode)
             .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    

    private ProfessionToSkill findLinkByProfessionAndSkill(String professionCode, String skillCode) {
        try {
            return entityManager.createQuery(
                "SELECT l FROM ProfessionToSkill l " +
                "WHERE l.professionCode = :professionCode AND l.skillCode = :skillCode",
                ProfessionToSkill.class
            ).setParameter("professionCode", professionCode)
             .setParameter("skillCode", skillCode)
             .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
