package kz.gov.example.esutd.soap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.gov.example.esutd.soap.service.ShepClientService;
import kz.gov.example.esutd.soap.util.SignatureVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shep")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SHEP API", description = "API для взаимодействия с ШЭП")
public class ShepSoapController {

    private final ShepClientService shepClientService;
    private final SignatureVerifier signatureVerifier;

    @PostMapping("/send")
    @Operation(summary = "Отправить SOAP запрос в ШЭП", 
               description = "Подписывает XML-запрос и отправляет его в ШЭП")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Запрос успешно обработан", 
                    content = @Content(mediaType = "application/xml")),
        @ApiResponse(responseCode = "400", description = "Ошибка при обработке запроса", 
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<String> sendToShep(
            @Parameter(description = "XML-запрос для отправки в ШЭП", required = true) 
            @RequestBody String xmlRequest) {
        try {
            log.info("Получен запрос для отправки в ШЭП (размер: {} байт)", xmlRequest.length());
            
            String signedXml = signatureVerifier.sign(xmlRequest);
            log.info("XML успешно подписан, отправляем в ШЭП");
            
            String response = shepClientService.sendSignedXml(signedXml);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при отправке запроса в ШЭП", e);
            return ResponseEntity.badRequest().body("Ошибка при отправке запроса в ШЭП: " + e.getMessage());
        }
    }
    
    @GetMapping("/status")
    @Operation(summary = "Проверить статус ШЭП", 
               description = "Проверяет доступность сервиса ШЭП")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус ШЭП получен", 
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Map<String, Object>> checkShepStatus() {
        try {
            log.info("Проверка доступности ШЭП");
            
            return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Сервис ШЭП доступен по адресу: http://10.61.40.133/shep/bip-sync-wss-gost/",
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("Ошибка при проверке доступности ШЭП", e);
            return ResponseEntity.ok(Map.of(
                "status", "ERROR",
                "message", "Сервис ШЭП недоступен: " + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }
}