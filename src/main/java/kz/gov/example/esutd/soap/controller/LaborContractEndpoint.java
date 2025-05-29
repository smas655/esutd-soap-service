package kz.gov.example.esutd.soap.controller;

import kz.gov.example.esutd.soap.model.*;
import kz.gov.example.esutd.soap.model.entity.Contract;
import kz.gov.example.esutd.soap.model.entity.Employee;
import kz.gov.example.esutd.soap.model.entity.Employer;
import kz.gov.example.esutd.soap.repository.EmployeeRepository;
import kz.gov.example.esutd.soap.repository.EmployerRepository;
import kz.gov.example.esutd.soap.service.ContractService;
import kz.gov.example.esutd.soap.service.ReferenceService;
import kz.gov.example.esutd.soap.util.SignatureVerifier;
import lombok.RequiredArgsConstructor;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;
import java.util.Optional;

@Endpoint
@RequiredArgsConstructor
public class LaborContractEndpoint {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LaborContractEndpoint.class);
    
    private static final String NAMESPACE_URI = "http://example.gov.kz/esutd/soap/contract";
    
    private final ContractService contractService;
    private final ReferenceService referenceService;
    private final SignatureVerifier signatureVerifier;
    private final EmployeeRepository employeeRepository;
    private final EmployerRepository employerRepository;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ContractSyncRequest")
    @ResponsePayload
    public ContractSyncResponse processContractRequest(@RequestPayload ContractSyncRequest request) {
        log.info("Received SOAP request: {}", request.getRequestInfo().getRequestId());
        
        ContractSyncResponse response = new ContractSyncResponse();
        
        try {
            if (!signatureVerifier.verify(request)) {
               throw new SecurityException("Invalid digital signature");
            }
            
            processOperation(request.getContractData());
            
            response.setStatus(StatusType.OK);
        } catch (Exception e) {
            log.error("Error processing request", e);

            response.setStatus(StatusType.ERROR);
            response.setErrorMessage(e.getMessage());
        }
        
        return response;
    }
    
    private void processOperation(ContractDataType contractData) {
        if (contractData.getOperation() == null) {
            throw new IllegalArgumentException("Operation must be specified in the request");
        }
        
        switch (contractData.getOperation()) {
            case CREATE_CONTRACT:
                processCreateContract(contractData);
                break;
            case UPDATE_CONTRACT:
                processUpdateContract(contractData);
                break;
            case TERMINATE_CONTRACT:
                processTerminateContract(contractData);
                break;
            case UPDATE_EMPLOYEE:
                processUpdateEmployee(contractData);
                break;
            case UPDATE_EMPLOYER:
                processUpdateEmployer(contractData);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + contractData.getOperation());
        }
    }
    
    private void processCreateContract(ContractDataType contractData) {
        ContractType contractType = contractData.getContract();
        EmployeeType employeeType = contractData.getEmployee();
        EmployerType employerType = contractData.getEmployer();
        
        if (contractType == null || employeeType == null || employerType == null) {
            throw new IllegalArgumentException("Contract, employee, and employer data must be provided for CREATE_CONTRACT operation");
        }
        
        Contract contract = mapToContractEntity(contractType);
        Employee employee = mapToEmployeeEntity(employeeType);
        Employer employer = mapToEmployerEntity(employerType);
        
        validateContractData(contract);
    
        contractService.createContract(contract, employee, employer);
    }
    
    private void processUpdateContract(ContractDataType contractData) {
        ContractType contractType = contractData.getContract();
        
        if (contractType == null || contractType.getContractId() == null) {
            throw new IllegalArgumentException("Contract data with ID must be provided for UPDATE_CONTRACT operation");
        }
        
        Contract contract = mapToContractEntity(contractType);
        
        contractService.updateContract(contract);
    }
    
    private void processTerminateContract(ContractDataType contractData) {
        TerminationInfoType terminationInfo = contractData.getTerminationInfo();
        
        if (terminationInfo == null) {
            throw new IllegalArgumentException("Termination info must be provided for TERMINATE_CONTRACT operation");
        }
        
        if (!referenceService.isTerminationReasonValid(terminationInfo.getTerminationReasonCode())) {
            throw new IllegalArgumentException("Invalid termination reason code: " + terminationInfo.getTerminationReasonCode());
        }
        
        LocalDate terminationDate = LocalDate.parse(terminationInfo.getTerminationDate().toString());
        
        contractService.terminateContract(
                terminationInfo.getContractId(),
                terminationDate,
                terminationInfo.getTerminationReason(),
                terminationInfo.getTerminationReasonCode()
        );
    }
    
    private void processUpdateEmployee(ContractDataType contractData) {
        EmployeeType employeeType = contractData.getEmployee();
        
        if (employeeType == null || employeeType.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee data with ID must be provided for UPDATE_EMPLOYEE operation");
        }
        
        Employee employee = mapToEmployeeEntity(employeeType);
        
        Optional<Employee> existingEmployee = employeeRepository.findById(employee.getEmployeeId());
        if (existingEmployee.isEmpty()) {
            throw new IllegalArgumentException("Employee not found with ID: " + employee.getEmployeeId());
        }
        
        employeeRepository.save(employee);
        
        log.info("Employee with ID: {} has been updated", employee.getEmployeeId());
    }
    
    private void processUpdateEmployer(ContractDataType contractData) {
        EmployerType employerType = contractData.getEmployer();
        
        if (employerType == null || employerType.getEmployerId() == null) {
            throw new IllegalArgumentException("Employer data with ID must be provided for UPDATE_EMPLOYER operation");
        }
        
        Employer employer = mapToEmployerEntity(employerType);
        
        Optional<Employer> existingEmployer = employerRepository.findById(employer.getEmployerId());
        if (existingEmployer.isEmpty()) {
            throw new IllegalArgumentException("Employer not found with ID: " + employer.getEmployerId());
        }
        
        employerRepository.save(employer);
        
        log.info("Employer with ID: {} has been updated", employer.getEmployerId());
    }
    
    private Contract mapToContractEntity(ContractType contractType) {
        Contract contract = new Contract();
        contract.setContractId(contractType.getContractId());
        contract.setContractNumber(contractType.getContractNumber());
        
        contract.setContractDate(LocalDate.parse(contractType.getContractDate().toString()));
        contract.setStartDate(LocalDate.parse(contractType.getStartDate().toString()));
        
        if (contractType.getEndDate() != null) {
            contract.setEndDate(LocalDate.parse(contractType.getEndDate().toString()));
        }
        
        contract.setContractType(Contract.ContractType.valueOf(contractType.getContractType().toString()));
        
        contract.setPosition(contractType.getPosition());
        contract.setPositionCode(contractType.getPositionCode());
        
        contract.setWorkConditions(contractType.getWorkConditions());
        contract.setWorkHours(contractType.getWorkHours());
        contract.setDepartment(contractType.getDepartment());
        
        return contract;
    }
    
    private Employee mapToEmployeeEntity(EmployeeType employeeType) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeType.getEmployeeId());
        employee.setIin(employeeType.getIin());
        employee.setLastName(employeeType.getLastName());
        employee.setFirstName(employeeType.getFirstName());
        employee.setMiddleName(employeeType.getMiddleName());
        
        employee.setBirthDate(LocalDate.parse(employeeType.getBirthDate().toString()));
        
        employee.setGender(Employee.Gender.valueOf(employeeType.getGender().toString()));
        
        employee.setCitizenship(employeeType.getCitizenship());
        
        if (employeeType.getAddress() != null) {
            AddressType address = employeeType.getAddress();
            employee.setAddressCountry(address.getCountryCode());
            employee.setAddressKatoCode(address.getKatoCode());
            employee.setAddressCity(address.getCity());
            employee.setAddressStreet(address.getStreet());
            employee.setAddressBuilding(address.getBuilding());
            employee.setAddressApartment(address.getApartment());
            employee.setAddressPostalCode(address.getPostalCode());
        }
        
        return employee;
    }
    
    private Employer mapToEmployerEntity(EmployerType employerType) {
        Employer employer = new Employer();
        employer.setEmployerId(employerType.getEmployerId());
        employer.setBin(employerType.getBin());
        employer.setName(employerType.getName());
        
        if (employerType.getLegalAddress() != null) {
            AddressType address = employerType.getLegalAddress();
            employer.setAddressCountry(address.getCountryCode());
            employer.setAddressKatoCode(address.getKatoCode());
            employer.setAddressCity(address.getCity());
            employer.setAddressStreet(address.getStreet());
            employer.setAddressBuilding(address.getBuilding());
            employer.setAddressPostalCode(address.getPostalCode());
        }
        
        return employer;
    }
    
    private void validateContractData(Contract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Contract cannot be null");
        }
        
        if (contract.getPositionCode() == null || contract.getPositionCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Position code is required");
        }
        
        if (!referenceService.isPositionValid(contract.getPositionCode())) {
            throw new IllegalArgumentException("Invalid position code: " + contract.getPositionCode());
        }
        
        if (contract.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        
        if (contract.getEndDate() != null && contract.getEndDate().isBefore(contract.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        
        if (contract.getContractType() == null) {
            throw new IllegalArgumentException("Contract type is required");
        }
    }
}
