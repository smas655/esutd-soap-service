package kz.gov.example.esutd.soap.controller;

import kz.gov.example.esutd.soap.model.entity.ProfessionReference;
import kz.gov.example.esutd.soap.model.entity.ProfessionalSkill;
import kz.gov.example.esutd.soap.model.entity.ProfessionalArea;
import kz.gov.example.esutd.soap.service.ProfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/professions")
@RequiredArgsConstructor
public class ProfessionController {
    private final ProfessionService professionService;
    private static final Logger log = LoggerFactory.getLogger(ProfessionController.class);

    @GetMapping
    public ResponseEntity<List<ProfessionReference>> getAllProfessions() {
        log.info("Запрос на получение всех профессий");
        List<ProfessionReference> professions = professionService.getAllSelectableProfessions();
        return ResponseEntity.ok(professions);
    }

    @GetMapping("/by-area/{areaCode}")
    public ResponseEntity<List<ProfessionReference>> getProfessionsByArea(@PathVariable String areaCode) {
        log.info("Запрос на получение профессий для профессиональной области: {}", areaCode);
        List<ProfessionReference> professions = professionService.getProfessionsByProfArea(areaCode);
        return ResponseEntity.ok(professions);
    }

    @GetMapping("/{professionCode}/skills")
    public ResponseEntity<List<ProfessionalSkill>> getSkillsByProfession(@PathVariable String professionCode) {
        log.info("Запрос на получение навыков для профессии: {}", professionCode);
        List<ProfessionalSkill> skills = professionService.getSkillsByProfession(professionCode);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/areas")
    public ResponseEntity<List<ProfessionalArea>> getAllProfessionalAreas() {
        log.info("Запрос на получение всех профессиональных областей");
        List<ProfessionalArea> areas = professionService.getAllProfessionalAreas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/validate/{professionCode}")
    public ResponseEntity<Boolean> validateProfessionCode(@PathVariable String professionCode) {
        log.info("Запрос на валидацию кода профессии: {}", professionCode);
        boolean isValid = professionService.isProfessionValid(professionCode);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/areas/validate/{areaCode}")
    public ResponseEntity<Boolean> validateAreaCode(@PathVariable String areaCode) {
        log.info("Запрос на валидацию кода профессиональной области: {}", areaCode);
        boolean isValid = professionService.isProfessionalAreaValid(areaCode);
        return ResponseEntity.ok(isValid);
    }
}