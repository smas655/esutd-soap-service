package kz.gov.example.esutd.soap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.gov.example.esutd.soap.util.SignatureVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract API", description = "API для работы с трудовыми договорами")
public class ContractApiController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContractApiController.class);

    private final SignatureVerifier signatureVerifier;

    @GetMapping("/samples/create")
    @Operation(summary = "Получить пример XML для создания договора", 
              description = "Возвращает шаблон XML для создания нового трудового договора")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "XML шаблон", 
                   content = @Content(mediaType = "application/xml"))
    })
    public ResponseEntity<String> getCreateContractSample() {
        try {
            String xml = readFile("samples/create-contract.xml");
            return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xml);
        } catch (Exception e) {
            log.error("Ошибка при чтении шаблона XML", e);
            return ResponseEntity.badRequest().body("Ошибка при чтении шаблона: " + e.getMessage());
        }
    }

    @GetMapping("/samples/update")
    @Operation(summary = "Получить пример XML для обновления договора", 
              description = "Возвращает шаблон XML для обновления существующего трудового договора")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "XML шаблон", 
                   content = @Content(mediaType = "application/xml"))
    })
    public ResponseEntity<String> getUpdateContractSample() {
        try {
            String xml = readFile("samples/update-contract.xml");
            return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xml);
        } catch (Exception e) {
            log.error("Ошибка при чтении шаблона XML", e);
            return ResponseEntity.badRequest().body("Ошибка при чтении шаблона: " + e.getMessage());
        }
    }

    @GetMapping("/samples/terminate")
    @Operation(summary = "Получить пример XML для расторжения договора", 
              description = "Возвращает шаблон XML для расторжения трудового договора")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "XML шаблон", 
                   content = @Content(mediaType = "application/xml"))
    })
    public ResponseEntity<String> getTerminateContractSample() {
        try {
            String xml = readFile("samples/terminate-contract.xml");
            return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xml);
        } catch (Exception e) {
            log.error("Ошибка при чтении шаблона XML", e);
            return ResponseEntity.badRequest().body("Ошибка при чтении шаблона: " + e.getMessage());
        }
    }

    @PostMapping("/sign")
    @Operation(summary = "Подписать XML запрос", 
              description = "Подписывает XML запрос для трудового договора без отправки в ШЭП")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Подписанный XML", 
                   content = @Content(mediaType = "application/xml")),
        @ApiResponse(responseCode = "400", description = "Ошибка при подписании", 
                   content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<String> signXml(
            @Parameter(description = "XML для подписи", required = true) 
            @RequestBody String xml) {
        try {
            log.info("Получен запрос на подпись XML (размер: {} байт)", xml.length());
            String signedXml = signatureVerifier.sign(xml);
            return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(signedXml);
        } catch (Exception e) {
            log.error("Ошибка при подписании XML", e);
            return ResponseEntity.badRequest().body("Ошибка при подписании XML: " + e.getMessage());
        }
    }
    
    private String readFile(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        if (resource.exists()) {
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        }
    }
} 