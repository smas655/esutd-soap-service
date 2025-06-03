package kz.gov.example.esutd.soap.service.validator;

import kz.gov.example.esutd.soap.model.entity.Contract;
import kz.gov.example.esutd.soap.service.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Period;

/**
 * Валидатор данных трудового договора
 */
@Component
public class ContractValidator {
    
    @Autowired
    private ReferenceService referenceService;
    
    /**
     * Валидирует данные трудового договора
     * 
     * @param contract данные трудового договора
     * @throws ValidationException если данные не прошли валидацию
     */
    public void validateContract(Contract contract) {
        if (contract == null) {
            throw new ValidationException("Contract cannot be null");
        }
        
        validateContractDates(contract);
        validatePosition(contract);
        validateWorkingHours(contract);
        validateWorkConditions(contract);
    }
    
    /**
     * Валидирует даты в данных трудового договора
     * 
     * @param contract данные трудового договора
     * @throws ValidationException если даты не прошли валидацию
     */
    private void validateContractDates(Contract contract) {
        if (contract.getStartDate() == null) {
            throw new ValidationException("Contract start date is required");
        }
        
        LocalDate now = LocalDate.now();
        
        // Проверка даты начала контракта
        if (contract.getStartDate().isBefore(now.minusYears(1))) {
            throw new ValidationException("Contract start date cannot be more than 1 year in the past");
        }
        
        if (contract.getStartDate().isAfter(now.plusYears(1))) {
            throw new ValidationException("Contract start date cannot be more than 1 year in the future");
        }
        
        // Проверка даты окончания контракта, если она указана
        if (contract.getEndDate() != null) {
            if (contract.getEndDate().isBefore(contract.getStartDate())) {
                throw new ValidationException("Contract end date cannot be before start date");
            }
            
            // Проверка максимальной длительности контракта (5 лет)
            Period period = Period.between(contract.getStartDate(), contract.getEndDate());
            if (period.getYears() > 5) {
                throw new ValidationException("Contract duration cannot exceed 5 years");
            }
        }
        
        // Проверка даты заключения контракта
        if (contract.getContractDate() == null) {
            throw new ValidationException("Contract date is required");
        }
        
        if (contract.getContractDate().isAfter(now)) {
            throw new ValidationException("Contract date cannot be in the future");
        }
        
        if (contract.getContractDate().isAfter(contract.getStartDate())) {
            throw new ValidationException("Contract date cannot be after start date");
        }
    }
    
    /**
     * Валидирует справочные данные в данных трудового договора
     * 
     * @param contract данные трудового договора
     * @throws ValidationException если справочные данные не прошли валидацию
     */
    private void validatePosition(Contract contract) {
        if (!StringUtils.hasText(contract.getPosition())) {
            throw new ValidationException("Position name is required");
        }
        
        if (!StringUtils.hasText(contract.getPositionCode())) {
            throw new ValidationException("Position code is required");
        }
        
        if (!referenceService.isPositionValid(contract.getPositionCode())) {
            throw new ValidationException("Invalid position code: " + contract.getPositionCode());
        }
        
        // Проверка соответствия кода и названия должности
        String expectedPositionName = referenceService.getPositionName(contract.getPositionCode());
        if (expectedPositionName != null && !expectedPositionName.equalsIgnoreCase(contract.getPosition())) {
            throw new ValidationException(String.format(
                "Position name '%s' does not match the code '%s' (expected: '%s')",
                contract.getPosition(), contract.getPositionCode(), expectedPositionName
            ));
        }
    }
    
    private void validateWorkingHours(Contract contract) {
        if (StringUtils.hasText(contract.getWorkHours()) && 
            !referenceService.isWorkingHoursValid(contract.getWorkHours())) {
            throw new ValidationException("Invalid working hours code: " + contract.getWorkHours());
        }
    }
    
    private void validateWorkConditions(Contract contract) {
        if (StringUtils.hasText(contract.getWorkConditions()) && 
            !referenceService.isWorkConditionsValid(contract.getWorkConditions())) {
            throw new ValidationException("Invalid work conditions code: " + contract.getWorkConditions());
        }
    }
    
    public void validateTermination(String terminationReasonCode, LocalDate terminationDate) {
        if (!StringUtils.hasText(terminationReasonCode)) {
            throw new ValidationException("Termination reason code is required");
        }
        
        if (!referenceService.isTerminationReasonValid(terminationReasonCode)) {
            throw new ValidationException("Invalid termination reason code: " + terminationReasonCode);
        }
        
        if (terminationDate == null) {
            throw new ValidationException("Termination date is required");
        }
        
        LocalDate now = LocalDate.now();
        if (terminationDate.isAfter(now.plusMonths(1))) {
            throw new ValidationException("Termination date cannot be more than 1 month in the future");
        }
        
        if (terminationDate.isBefore(now.minusYears(1))) {
            throw new ValidationException("Termination date cannot be more than 1 year in the past");
        }
    }
}
