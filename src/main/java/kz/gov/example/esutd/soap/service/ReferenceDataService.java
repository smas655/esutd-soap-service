package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.entity.NkzClassifier;
import kz.gov.example.esutd.soap.model.entity.ProfessionalArea;
import kz.gov.example.esutd.soap.model.entity.ProfessionalSkill;
import kz.gov.example.esutd.soap.model.entity.GeneralSkill;
import kz.gov.example.esutd.soap.repository.NkzClassifierRepository;
import kz.gov.example.esutd.soap.repository.ProfessionalAreaRepository;
import kz.gov.example.esutd.soap.repository.ProfessionalSkillRepository;
import kz.gov.example.esutd.soap.repository.GeneralSkillRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Сервис для загрузки справочных данных из Excel файлов
 */
@Service
public class ReferenceDataService {
    
    private static final Logger log = LoggerFactory.getLogger(ReferenceDataService.class);
    
    @Autowired
    private NkzClassifierRepository nkzClassifierRepository;
    
    @Autowired
    private ProfessionalAreaRepository professionalAreaRepository;
    
    @Autowired
    private ProfessionalSkillRepository professionalSkillRepository;
    
    @Autowired
    private GeneralSkillRepository generalSkillRepository;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Value("${reference.data.nkz.file:classpath:reference/sprprf_2021.xlsx}")
    private String nkzClassifierFile;
    
    @Value("${reference.data.professional.areas.file:classpath:reference/professional_areas.xlsx}")
    private String professionalAreasFile;
    
    @Value("${reference.data.professional.skills.file:classpath:reference/professional_skills.xlsx}")
    private String professionalSkillsFile;
    
    @Value("${reference.data.general.skills.file:classpath:reference/general_skills.xlsx}")
    private String generalSkillsFile;
    
    @Value("${reference.data.load.on.startup:false}")
    private boolean loadOnStartup;
    
    /**
     * Загружает справочные данные при запуске приложения, если включена соответствующая настройка
     */
    @PostConstruct
    public void init() {
        if (loadOnStartup) {
            log.info("Загрузка справочных данных при запуске приложения...");
            loadReferenceData();
        }
    }
    
    /**
     * Загружает все справочные данные
     */
    public void loadReferenceData() {
        try {
            loadNkzClassifier();
            loadProfessionalAreas();
            loadProfessionalSkills();
            loadGeneralSkills();
            log.info("Загрузка справочных данных завершена успешно");
        } catch (Exception e) {
            log.error("Ошибка при загрузке справочных данных", e);
        }
    }
    
    /**
     * Загружает справочник НКЗ (профессий) из Excel файла
     */
    public void loadNkzClassifier() {
        log.info("Загрузка справочника НКЗ из файла: {}", nkzClassifierFile);
        try {
            Resource resource = resourceLoader.getResource(nkzClassifierFile);
            try (InputStream is = resource.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                List<NkzClassifier> nkzClassifiers = new ArrayList<>();
                
                Iterator<Row> rowIterator = sheet.iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }
                
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    
                    String code = getStringCellValue(row, 0);
                    String name = getStringCellValue(row, 1);
                    String action = getStringCellValue(row, 2);
                    
                    // Пропускаем записи с действием "del" или "close"
                    if ("del".equalsIgnoreCase(action) || "close".equalsIgnoreCase(action)) {
                        continue;
                    }
                    
                    NkzClassifier nkzClassifier = new NkzClassifier();
                    nkzClassifier.setCode(code);
                    nkzClassifier.setName(name);
                    nkzClassifier.setAction(action);
                    nkzClassifier.setIsActive(true);
                    
                    // Определяем уровень и родительский код
                    if (code != null && code.length() >= 2) {
                        int level = 1;
                        String parentCode = null;
                        
                        if (code.length() >= 4) {
                            level = 2;
                            parentCode = code.substring(0, 2) + "00000000".substring(0, 8);
                        }
                        
                        if (code.length() >= 6) {
                            level = 3;
                            parentCode = code.substring(0, 4) + "000000".substring(0, 6);
                        }
                        
                        if (code.length() >= 8) {
                            level = 4;
                            parentCode = code.substring(0, 6) + "0000".substring(0, 4);
                        }
                        
                        nkzClassifier.setLevel(level);
                        nkzClassifier.setParentCode(parentCode);
                    }
                    
                    nkzClassifiers.add(nkzClassifier);
                }
                
                nkzClassifierRepository.saveAll(nkzClassifiers);
                log.info("Загружено {} записей справочника НКЗ", nkzClassifiers.size());
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке справочника НКЗ", e);
        }
    }
    
    /**
     * Загружает справочник профессиональных областей из Excel файла
     */
    public void loadProfessionalAreas() {
        log.info("Загрузка справочника профессиональных областей из файла: {}", professionalAreasFile);
        try {
            Resource resource = resourceLoader.getResource(professionalAreasFile);
            try (InputStream is = resource.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                List<ProfessionalArea> professionalAreas = new ArrayList<>();
                
                Iterator<Row> rowIterator = sheet.iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next(); // Пропускаем заголовок
                }
                
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    
                    String code = getStringCellValue(row, 0);
                    String name = getStringCellValue(row, 1);
                    
                    if (code == null || name == null) {
                        continue;
                    }
                    
                    ProfessionalArea area = new ProfessionalArea();
                    area.setCode(code);
                    area.setNameRu(name);
                    area.setIsActive(true);
                    
                    professionalAreas.add(area);
                }
                
                professionalAreaRepository.saveAll(professionalAreas);
                log.info("Загружено {} записей справочника профессиональных областей", professionalAreas.size());
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке справочника профессиональных областей", e);
        }
    }
    
    /**
     * Загружает справочник профессиональных навыков из Excel файла
     */
    public void loadProfessionalSkills() {
        log.info("Загрузка справочника профессиональных навыков из файла: {}", professionalSkillsFile);
        try {
            Resource resource = resourceLoader.getResource(professionalSkillsFile);
            try (InputStream is = resource.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                List<ProfessionalSkill> professionalSkills = new ArrayList<>();
                
                Iterator<Row> rowIterator = sheet.iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next(); // Пропускаем заголовок
                }
                
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    
                    String code = getStringCellValue(row, 0);
                    String name = getStringCellValue(row, 1);
                    String areaCode = getStringCellValue(row, 2);
                    
                    if (code == null || name == null) {
                        continue;
                    }
                    
                    ProfessionalSkill skill = new ProfessionalSkill();
                    skill.setCode(code);
                    skill.setNameRu(name);
                    // Store area code in description field since there's no specific field for it
                    skill.setDescription("Area code: " + areaCode);
                    skill.setIsActive(true);
                    
                    professionalSkills.add(skill);
                }
                
                professionalSkillRepository.saveAll(professionalSkills);
                log.info("Загружено {} записей справочника профессиональных навыков", professionalSkills.size());
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке справочника профессиональных навыков", e);
        }
    }
    
    /**
     * Загружает справочник общих навыков из Excel файла
     */
    public void loadGeneralSkills() {
        log.info("Загрузка справочника общих навыков из файла: {}", generalSkillsFile);
        try {
            Resource resource = resourceLoader.getResource(generalSkillsFile);
            try (InputStream is = resource.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                List<GeneralSkill> generalSkills = new ArrayList<>();
                
                Iterator<Row> rowIterator = sheet.iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next(); // Пропускаем заголовок
                }
                
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    
                    String code = getStringCellValue(row, 0);
                    String name = getStringCellValue(row, 1);
                    String category = getStringCellValue(row, 2);
                    
                    if (code == null || name == null) {
                        continue;
                    }
                    
                    GeneralSkill skill = new GeneralSkill();
                    skill.setCode(code);
                    skill.setNameRu(name);
                    // Store category in description field since there's no specific field for it
                    skill.setDescription("Category: " + category);
                    skill.setIsActive(true);
                    
                    generalSkills.add(skill);
                }
                
                generalSkillRepository.saveAll(generalSkills);
                log.info("Загружено {} записей справочника общих навыков", generalSkills.size());
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке справочника общих навыков", e);
        }
    }
    
    /**
     * Получает строковое значение ячейки
     * 
     * @param row номер строки
     * @param cellIndex индекс ячейки
     * @return строковое значение ячейки или null, если ячейка пуста
     */
    private String getStringCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
