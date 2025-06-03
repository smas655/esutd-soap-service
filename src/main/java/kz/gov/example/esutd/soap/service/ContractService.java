package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.entity.Contract;
import kz.gov.example.esutd.soap.model.entity.Employee;
import kz.gov.example.esutd.soap.model.entity.Employer;
import kz.gov.example.esutd.soap.repository.ContractRepository;
import kz.gov.example.esutd.soap.repository.EmployeeRepository;
import kz.gov.example.esutd.soap.repository.EmployerRepository;
import kz.gov.example.esutd.soap.service.validator.ContractValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {
    
    private static final Logger log = LoggerFactory.getLogger(ContractService.class);
    
    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployerRepository employerRepository;
    private final ContractValidator contractValidator;
    private final ReferenceService referenceService;
    
    @Transactional
    public Contract createContract(Contract contract, Employee employee, Employer employer) {
        log.info("Creating contract with number: {}", contract.getContractNumber());
        
        validateEmployeeData(employee);
        validateEmployerData(employer);
        contractValidator.validateContract(contract);
        
        Optional<Employee> existingEmployee = employeeRepository.findByIin(employee.getIin());
        if (existingEmployee.isPresent()) {
            employee = existingEmployee.get();
        } else {
            employee = employeeRepository.save(employee);
        }
        
        Optional<Employer> existingEmployer = employerRepository.findByBin(employer.getBin());
        if (existingEmployer.isPresent()) {
            employer = existingEmployer.get();
        } else {
            employer = employerRepository.save(employer);
        }
        
        contract.setEmployee(employee);
        contract.setEmployer(employer);
        contract.setIsActive(true);
        contract.setCreatedAt(LocalDate.now());
        
        return contractRepository.save(contract);
    }
    
    @Transactional
    public Contract updateContract(Contract contract) {
        log.info("Updating contract with ID: {}", contract.getContractId());
        
        Optional<Contract> existingContract = contractRepository.findById(contract.getContractId());
        if (existingContract.isEmpty()) {
            throw new IllegalArgumentException("Contract not found with ID: " + contract.getContractId());
        }
        
        Contract updatedContract = existingContract.get();
        
        // Validate updated data
        contractValidator.validateContract(contract);
        
        // Update only allowed fields
        updatedContract.setPosition(contract.getPosition());
        updatedContract.setPositionCode(contract.getPositionCode());
        updatedContract.setWorkConditions(contract.getWorkConditions());
        updatedContract.setWorkHours(contract.getWorkHours());
        updatedContract.setDepartment(contract.getDepartment());
        updatedContract.setEndDate(contract.getEndDate());
        updatedContract.setUpdatedAt(LocalDate.now());
        
        return contractRepository.save(updatedContract);
    }
    
    @Transactional
    public Contract terminateContract(String contractId, LocalDate terminationDate, 
                                    String terminationReason, String terminationReasonCode) {
        log.info("Terminating contract with ID: {}", contractId);
        
        Optional<Contract> existingContract = contractRepository.findById(contractId);
        if (existingContract.isEmpty()) {
            throw new IllegalArgumentException("Contract not found with ID: " + contractId);
        }
        
        Contract contract = existingContract.get();
        
        if (!contract.getIsActive()) {
            throw new IllegalStateException("Contract is already terminated");
        }
        
        contractValidator.validateTermination(terminationReasonCode, terminationDate);
        
        if (terminationDate.isBefore(contract.getStartDate())) {
            throw new ValidationException("Termination date cannot be before contract start date");
        }
        
        contract.setIsActive(false);
        contract.setTerminationDate(terminationDate);
        contract.setTerminationReason(terminationReason);
        contract.setTerminationReasonCode(terminationReasonCode);
        contract.setUpdatedAt(LocalDate.now());
        
        return contractRepository.save(contract);
    }
    
    private void validateEmployeeData(Employee employee) {
        if (employee == null) {
            throw new ValidationException("Employee data is required");
        }
        
        if (!StringUtils.hasText(employee.getIin()) || employee.getIin().length() != 12) {
            throw new ValidationException("Employee IIN must be 12 digits");
        }
        
        if (!StringUtils.hasText(employee.getFirstName())) {
            throw new ValidationException("Employee first name is required");
        }
        
        if (!StringUtils.hasText(employee.getLastName())) {
            throw new ValidationException("Employee last name is required");
        }
        
        if (employee.getBirthDate() == null) {
            throw new ValidationException("Employee birth date is required");
        }
        
        if (employee.getGender() == null) {
            throw new ValidationException("Employee gender is required");
        }
    }
    
    private void validateEmployerData(Employer employer) {
        if (employer == null) {
            throw new ValidationException("Employer data is required");
        }
        
        if (!StringUtils.hasText(employer.getBin()) || employer.getBin().length() != 12) {
            throw new ValidationException("Employer BIN must be 12 digits");
        }
        
        if (!StringUtils.hasText(employer.getName())) {
            throw new ValidationException("Employer name is required");
        }
        
        if (StringUtils.hasText(employer.getAddressKatoCode()) && 
            !referenceService.isKatoCodeValid(employer.getAddressKatoCode())) {
            throw new ValidationException("Invalid KATO code: " + employer.getAddressKatoCode());
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Contract> getContractById(String contractId) {
        log.info("Getting contract with ID: {}", contractId);
        return contractRepository.findById(contractId);
    }
    
    @Transactional(readOnly = true)
    public List<Contract> getAllContracts() {
        log.info("Getting all contracts");
        return contractRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Contract> getActiveContracts() {
        log.info("Getting active contracts");
        return contractRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Contract> getContractsByEmployee(String iin) {
        log.info("Getting contracts for employee with IIN: {}", iin);
        return contractRepository.findByEmployeeIin(iin);
    }
    
    @Transactional(readOnly = true)
    public List<Contract> getContractsByEmployer(String employerId) {
        log.info("Getting contracts for employer with ID: {}", employerId);
        return contractRepository.findByEmployerEmployerId(employerId);
    }
} 