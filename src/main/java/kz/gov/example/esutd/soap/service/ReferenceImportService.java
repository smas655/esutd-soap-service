package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.entity.Reference;
import kz.gov.example.esutd.soap.repository.ReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ReferenceImportService {
    private static final Logger log = LoggerFactory.getLogger(ReferenceImportService.class);

    private final ReferenceRepository referenceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int importReferencesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Указанный путь не является директорией: " + directoryPath);
        }

        int totalImported = 0;
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"));
        
        if (files == null || files.length == 0) {
            log.warn("В директории {} не найдено файлов Excel", directoryPath);
            return 0;
        }

        for (File file : files) {
            try {
                int imported = importReferenceFromFile(file);
                totalImported += imported;
                log.info("Импортировано {} записей из файла {}", imported, file.getName());
            } catch (Exception e) {
                log.error("Ошибка при импорте файла {}: {}", file.getName(), e.getMessage(), e);
                if (entityManager.isOpen()) {
                    entityManager.clear();
                }
            }
        }

        return totalImported;
    }

    @Transactional
    public int importReferenceFromFile(File file) {
        String fileName = file.getName();
        log.info("Импорт справочника из файла: {}", fileName);
        
        int importedCount = 0;
        String referenceType = getReferenceTypeFromFileName(fileName);
        
        if (referenceType == null) {
            log.warn("Не удалось определить тип справочника для файла: {}", fileName);
            return 0;
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            
            if (headerRow == null) {
                log.warn("Файл {} не содержит строку заголовка", fileName);
                return 0;
            }
            
            int codeColumnIndex = findColumnIndex(headerRow, "CODE", "КОД");
            int nameRuColumnIndex = findColumnIndex(headerRow, "NAME_RU", "НАИМЕНОВАНИЕ", "НАИМЕНОВАНИЕ_РУС");
            int nameKzColumnIndex = findColumnIndex(headerRow, "NAME_KZ", "НАИМЕНОВАНИЕ_КАЗ");
            int descriptionColumnIndex = findColumnIndex(headerRow, "DESCRIPTION", "ОПИСАНИЕ");
            
            if (codeColumnIndex == -1 || nameRuColumnIndex == -1) {
                log.error("В файле {} не найдены необходимые колонки (код и наименование)", fileName);
                return 0;
            }
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                try {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    
                    Cell codeCell = row.getCell(codeColumnIndex);
                    Cell nameRuCell = row.getCell(nameRuColumnIndex);
                    
                    if (codeCell == null || nameRuCell == null) continue;
                    
                    String code = getCellValueAsString(codeCell);
                    String nameRu = getCellValueAsString(nameRuCell);
                    
                    if (code.isEmpty() || nameRu.isEmpty()) continue;
                    
                    Reference reference = new Reference();
                    reference.setReferenceType(referenceType);
                    reference.setCode(code);
                    reference.setNameRu(nameRu);
                    
                    if (nameKzColumnIndex != -1) {
                        Cell nameKzCell = row.getCell(nameKzColumnIndex);
                        if (nameKzCell != null) {
                            reference.setNameKz(getCellValueAsString(nameKzCell));
                        }
                    }
                    
                    if (descriptionColumnIndex != -1) {
                        Cell descriptionCell = row.getCell(descriptionColumnIndex);
                        if (descriptionCell != null) {
                            reference.setDescription(getCellValueAsString(descriptionCell));
                        }
                    }
                    
                    Optional<Reference> existingReference = referenceRepository.findByReferenceTypeAndCodeAndIsActiveTrue(
                            referenceType, code);
                    
                    if (existingReference.isPresent()) {
                        Reference existingRef = existingReference.get();
                        existingRef.setNameRu(nameRu);
                        existingRef.setNameKz(reference.getNameKz());
                        existingRef.setDescription(reference.getDescription());
                        importSingleReference(existingRef);
                    } else {
                        importSingleReference(reference);
                    }
                    
                    importedCount++;
                } catch (Exception e) {
                    log.warn("Ошибка при обработке строки {} в файле {}: {}", i, fileName, e.getMessage());
                    continue;
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при импорте файла {}: {}", fileName, e.getMessage(), e);
        }
        
        return importedCount;
    }
    
    private String getReferenceTypeFromFileName(String fileName) {
        String name = fileName.toUpperCase()
                .replace(".XLSX", "")
                .replace("D_", "")
                .replace("_", "");
        
        return name;
    }
    
    private int findColumnIndex(Row headerRow, String... possibleNames) {
        if (headerRow == null) return -1;
        
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String cellValue = getCellValueAsString(cell).toUpperCase();
                for (String name : possibleNames) {
                    if (cellValue.contains(name.toUpperCase())) {
                        return i;
                    }
                }
            }
        }
        
        return -1;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    double value = cell.getNumericCellValue();
                    if (value == Math.floor(value)) {
                        return String.format("%.0f", value);
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
                        return "";
                    }
                }
            default:
                return "";
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Reference importSingleReference(Reference reference) {
        try {
            return referenceRepository.save(reference);
        } catch (Exception e) {
            log.warn("Ошибка при сохранении справочника типа {} с кодом {}: {}", 
                    reference.getReferenceType(), reference.getCode(), e.getMessage());
            throw e;
        }
    }
} 