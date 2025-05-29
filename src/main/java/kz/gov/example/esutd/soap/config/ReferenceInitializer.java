package kz.gov.example.esutd.soap.config;

import kz.gov.example.esutd.soap.service.ReferenceImportService;
import kz.gov.example.esutd.soap.service.SpecializedReferenceImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ReferenceInitializer {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReferenceInitializer.class);

    private final ReferenceImportService referenceImportService;
    private final SpecializedReferenceImportService specializedReferenceImportService;
    
    @Value("${app.references.directory:./references}")
    private String referencesDirectory;
    
    @Value("${app.references.import-on-startup:false}")
    private boolean importOnStartup;
    
    @EventListener(ApplicationReadyEvent.class)
    public void loadReferencesOnStartup() {
        if (!importOnStartup) {
            log.info("Автоматический импорт справочников отключен. Для включения установите app.references.import-on-startup=true");
            return;
        }
        
        File directory = new File(referencesDirectory);
        
        if (directory.exists() && directory.isDirectory()) {
            int standardImportedCount = 0;
            int specializedImportedCount = 0;
            
            try {
                log.info("Начинаем импорт стандартных справочников из директории: {}", referencesDirectory);
                standardImportedCount = importStandardReferences(directory);
                log.info("Импортировано {} записей стандартных справочников", standardImportedCount);
            } catch (Exception e) {
                log.error("Ошибка при импорте стандартных справочников: {}", e.getMessage(), e);
            }
            
            try {
                log.info("Начинаем импорт специализированных справочников из директории: {}", referencesDirectory);
                Map<String, Integer> importResults = specializedReferenceImportService.importFromDirectory(referencesDirectory);
                specializedImportedCount = importResults.values().stream().mapToInt(Integer::intValue).sum();
                log.info("Импортировано {} записей специализированных справочников", specializedImportedCount);
            } catch (Exception e) {
                log.error("Ошибка при импорте специализированных справочников: {}", e.getMessage(), e);
            }
            
            log.info("Всего импортировано {} записей справочников при запуске приложения", 
                    standardImportedCount + specializedImportedCount);
        } else {
            log.warn("Директория справочников не найдена: {}. Импорт справочников при запуске не выполнен.", 
                    referencesDirectory);
        }
    }
    
    private int importStandardReferences(File directory) {
        int totalImported = 0;
        
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"));
        if (files == null || files.length == 0) {
            log.warn("В директории {} не найдено файлов Excel", referencesDirectory);
            return 0;
        }
        
        Arrays.sort(files, Comparator.comparing(File::getName));
        
        for (File file : files) {
            try {
                int imported = referenceImportService.importReferenceFromFile(file);
                totalImported += imported;
                log.info("Импортировано {} записей из файла {}", imported, file.getName());
            } catch (Exception e) {
                log.error("Ошибка при импорте файла {}: {}", file.getName(), e.getMessage(), e);
            }
        }
        
        return totalImported;
    }
} 