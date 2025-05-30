package kz.gov.example.esutd.soap.service.validator;

import kz.gov.example.esutd.soap.model.dto.ContractData;
import kz.gov.example.esutd.soap.model.entity.NkzClassifier;
import kz.gov.example.esutd.soap.repository.NkzClassifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Валидатор данных трудового договора
 */
@Component
public class ContractValidator {
    
    @Autowired
    private NkzClassifierRepository nkzClassifierRepository;
    
    /**
     * Валидирует данные трудового договора
     * 
     * @param contractData данные трудового договора
     * @throws ValidationException если данные не прошли валидацию
     */
    public void validateContract(ContractData contractData) {
        // Проверка обязательных полей
        if (contractData.getEmployer() == null) {
            throw new ValidationException("Данные работодателя обязательны");
        }
        
        if (contractData.getEmployer().getBin() == null || 
            contractData.getEmployer().getBin().length() != 12) {
            throw new ValidationException("БИН работодателя должен содержать 12 символов");
        }
        
        if (contractData.getEmployee() == null) {
            throw new ValidationException("Данные работника обязательны");
        }
        
        if (contractData.getEmployee().getIin() == null || 
            contractData.getEmployee().getIin().length() != 12) {
            throw new ValidationException("ИИН работника должен содержать 12 символов");
        }
        
        // Проверка дат
        validateDates(contractData);
        
        // Проверка справочных значений
        validateReferenceData(contractData);
    }
    
    /**
     * Валидирует даты в данных трудового договора
     * 
     * @param contractData данные трудового договора
     * @throws ValidationException если даты не прошли валидацию
     */
    private void validateDates(ContractData contractData) {
        LocalDate now = LocalDate.now();
        
        if (contractData.getContractDate() == null) {
            throw new ValidationException("Дата заключения договора обязательна");
        }
        
        if (contractData.getContractDate().isAfter(now)) {
            throw new ValidationException("Дата заключения договора не может быть в будущем");
        }
        
        if (contractData.getStartDate() == null) {
            throw new ValidationException("Дата начала работы обязательна");
        }
        
        if (contractData.getContractDurationType() != null && 
            contractData.getContractDurationType().equals("FIXED_TERM") && 
            contractData.getEndDate() == null) {
            throw new ValidationException("Для срочного договора дата окончания обязательна");
        }
        
        if (contractData.getEndDate() != null && 
            contractData.getStartDate().isAfter(contractData.getEndDate())) {
            throw new ValidationException("Дата начала работы не может быть позже даты окончания");
        }
    }
    
    /**
     * Валидирует справочные данные в данных трудового договора
     * 
     * @param contractData данные трудового договора
     * @throws ValidationException если справочные данные не прошли валидацию
     */
    private void validateReferenceData(ContractData contractData) {
        if (contractData.getPositionCode() != null) {
            Optional<NkzClassifier> nkzClassifier = nkzClassifierRepository.findById(contractData.getPositionCode());
            if (nkzClassifier.isEmpty() || !nkzClassifier.get().getIsActive()) {
                throw new ValidationException("Указанный код должности не найден в справочнике НКЗ или не активен");
            }
        }
    }
}
