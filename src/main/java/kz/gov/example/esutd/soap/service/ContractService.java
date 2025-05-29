package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.entity.Contract;
import kz.gov.example.esutd.soap.model.entity.Employee;
import kz.gov.example.esutd.soap.model.entity.Employer;
import kz.gov.example.esutd.soap.repository.ContractRepository;
import kz.gov.example.esutd.soap.repository.EmployeeRepository;
import kz.gov.example.esutd.soap.repository.EmployerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ReferenceService referenceService;

    @Transactional
    public Contract createContract(Contract contract, Employee employee, Employer employer) {
        log.info("Creating contract with number: {}", contract.getContractNumber());
        
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
        
        updatedContract.setPosition(contract.getPosition());
        updatedContract.setPositionCode(contract.getPositionCode());
        updatedContract.setWorkConditions(contract.getWorkConditions());
        updatedContract.setWorkHours(contract.getWorkHours());
        updatedContract.setDepartment(contract.getDepartment());
        updatedContract.setEndDate(contract.getEndDate());
        
        return contractRepository.save(updatedContract);
    }
    
    @Transactional
    public Contract terminateContract(String contractId, LocalDate terminationDate, String terminationReason, String terminationReasonCode) {
        log.info("Terminating contract with ID: {}", contractId);
        
        Optional<Contract> existingContract = contractRepository.findById(contractId);
        if (existingContract.isEmpty()) {
            throw new IllegalArgumentException("Contract not found with ID: " + contractId);
        }
        
        Contract contract = existingContract.get();
        
        if (!referenceService.isTerminationReasonValid(terminationReasonCode)) {
            throw new IllegalArgumentException("Invalid termination reason code: " + terminationReasonCode);
        }
        
        contract.setIsActive(false);
        contract.setTerminationDate(terminationDate);
        contract.setTerminationReason(terminationReason);
        contract.setTerminationReasonCode(terminationReasonCode);
        
        return contractRepository.save(contract);
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
} 