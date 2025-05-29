package kz.gov.example.esutd.soap.controller;

import kz.gov.example.esutd.soap.service.ReferenceImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/references")
@RequiredArgsConstructor
public class ReferenceImportController {
    private static final Logger log = LoggerFactory.getLogger(ReferenceImportController.class);

    private final ReferenceImportService referenceImportService;

    @PostMapping("/import-directory")
    public ResponseEntity<?> importFromDirectory(@RequestParam String directoryPath) {
        try {
            int imported = referenceImportService.importReferencesFromDirectory(directoryPath);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("importedCount", imported);
            response.put("message", "Импортировано " + imported + " записей справочников");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при импорте справочников: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/import-file")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Файл не загружен"
            ));
        }

        try {
            Path tempDir = Files.createTempDirectory("references");
            Path tempFile = Paths.get(tempDir.toString(), file.getOriginalFilename());
            Files.write(tempFile, file.getBytes());

            int imported = referenceImportService.importReferenceFromFile(tempFile.toFile());

            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(tempDir);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "importedCount", imported,
                    "message", "Импортировано " + imported + " записей из файла " + file.getOriginalFilename()
            ));
        } catch (IOException e) {
            log.error("Ошибка при импорте файла справочника: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}