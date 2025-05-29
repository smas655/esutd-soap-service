package kz.gov.example.esutd.soap.controller;

import kz.gov.example.esutd.soap.model.entity.Reference;
import kz.gov.example.esutd.soap.repository.ReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/references")
@RequiredArgsConstructor
@Slf4j
public class ReferenceExportController {

    private final ReferenceRepository referenceRepository;

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllReferenceTypes() {
        List<String> types = referenceRepository.findAll().stream()
                .map(Reference::getReferenceType)
                .distinct()
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{type}/json")
    public ResponseEntity<List<Reference>> exportReferenceAsJson(@PathVariable String type) {
        List<Reference> references = referenceRepository.findByReferenceTypeAndIsActiveTrue(type);
        
        if (references.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(references);
    }

    @GetMapping("/{type}/csv")
    public ResponseEntity<byte[]> exportReferenceAsCsv(@PathVariable String type) {
        List<Reference> references = referenceRepository.findByReferenceTypeAndIsActiveTrue(type);
        
        if (references.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        StringBuilder csv = new StringBuilder();
        csv.append("code,name_ru,name_kz,description\n");
        
        for (Reference ref : references) {
            csv.append(escapeCsvField(ref.getCode())).append(",");
            csv.append(escapeCsvField(ref.getNameRu())).append(",");
            csv.append(escapeCsvField(ref.getNameKz())).append(",");
            csv.append(escapeCsvField(ref.getDescription())).append("\n");
        }
        
        byte[] csvBytes = csv.toString().getBytes(StandardCharsets.UTF_8);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", type + ".csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvBytes.length)
                .body(csvBytes);
    }
    
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            field = field.replace("\"", "\"\"");
            field = "\"" + field + "\"";
        }
        
        return field;
    }
}