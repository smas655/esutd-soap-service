package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.dto.ContractData;
import kz.gov.example.esutd.soap.model.entity.AdditionalDocument;
import kz.gov.example.esutd.soap.model.entity.Contract;
import kz.gov.example.esutd.soap.model.entity.SubsidiaryContract;
import kz.gov.example.esutd.soap.repository.AdditionalDocumentRepository;
import kz.gov.example.esutd.soap.repository.ContractRepository;
import kz.gov.example.esutd.soap.repository.SubsidiaryContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с дополнительными соглашениями и документами к трудовому договору
 */
@Service
public class ContractDocumentService {
    
    private static final Logger log = LoggerFactory.getLogger(ContractDocumentService.class);
    
    @Autowired
    private ContractRepository contractRepository;
    
    @Autowired
    private SubsidiaryContractRepository subsidiaryContractRepository;
    
    @Autowired
    private AdditionalDocumentRepository additionalDocumentRepository;
    
    /**
     * Создает дополнительное соглашение к трудовому договору
     * 
     * @param contractId идентификатор трудового договора
     * @param subsidiaryContractData данные дополнительного соглашения
     * @return созданное дополнительное соглашение
     */
    @Transactional
    public SubsidiaryContract createSubsidiaryContract(String contractId, ContractData.SubsidiaryContractData subsidiaryContractData) {
        Optional<Contract> contractOpt = contractRepository.findByContractId(contractId);
        if (contractOpt.isEmpty()) {
            throw new IllegalArgumentException("Трудовой договор с ID " + contractId + " не найден");
        }
        
        Contract contract = contractOpt.get();
        
        SubsidiaryContract subsidiaryContract = new SubsidiaryContract();
        subsidiaryContract.setSubsidiaryContractId(UUID.randomUUID().toString());
        subsidiaryContract.setSubsidiaryContractNumber(subsidiaryContractData.getSubsidiaryContractNumber());
        subsidiaryContract.setSubsidiaryContractDate(subsidiaryContractData.getSubsidiaryContractDate());
        subsidiaryContract.setSubsidiaryContractType(subsidiaryContractData.getSubsidiaryContractType());
        subsidiaryContract.setSubsidiaryContractReason(subsidiaryContractData.getSubsidiaryContractReason());
        subsidiaryContract.setStartDate(subsidiaryContractData.getStartDate());
        subsidiaryContract.setEndDate(subsidiaryContractData.getEndDate());
        subsidiaryContract.setPosition(subsidiaryContractData.getPosition());
        subsidiaryContract.setPositionCode(subsidiaryContractData.getPositionCode());
        subsidiaryContract.setWorkType(subsidiaryContractData.getWorkType());
        subsidiaryContract.setRemoteWork(subsidiaryContractData.getRemoteWork());
        subsidiaryContract.setWorkPlaceAddress(subsidiaryContractData.getWorkPlaceAddress());
        subsidiaryContract.setWorkPlaceKato(subsidiaryContractData.getWorkPlaceKato());
        subsidiaryContract.setWorkPlaceCountry(subsidiaryContractData.getWorkPlaceCountry());
        subsidiaryContract.setWorkHours(subsidiaryContractData.getWorkHours());
        subsidiaryContract.setTariffRate(subsidiaryContractData.getTariffRate());
        subsidiaryContract.setWorkConditions(subsidiaryContractData.getWorkConditions());
        subsidiaryContract.setWorkConditionCode(subsidiaryContractData.getWorkConditionCode());
        subsidiaryContract.setCreatedAt(LocalDate.now());
        subsidiaryContract.setContract(contract);
        
        return subsidiaryContractRepository.save(subsidiaryContract);
    }
    
    /**
     * Создает дополнительный документ к трудовому договору
     * 
     * @param contractId идентификатор трудового договора
     * @param documentData данные дополнительного документа
     * @return созданный дополнительный документ
     */
    @Transactional
    public AdditionalDocument createAdditionalDocument(String contractId, ContractData.AdditionalDocumentData documentData) {
        Optional<Contract> contractOpt = contractRepository.findByContractId(contractId);
        if (contractOpt.isEmpty()) {
            throw new IllegalArgumentException("Трудовой договор с ID " + contractId + " не найден");
        }
        
        AdditionalDocument document = new AdditionalDocument();
        document.setDocumentId(UUID.randomUUID().toString());
        document.setDocumentType(documentData.getDocumentType());
        document.setDocumentNumber(documentData.getDocumentNumber());
        document.setDocumentDate(documentData.getDocumentDate());
        document.setDocumentName(documentData.getDocumentName());
        document.setDocumentDescription(documentData.getDocumentDescription());
        document.setDocumentData(documentData.getDocumentData());
        document.setFileName(documentData.getFileName());
        document.setFileMimeType(documentData.getFileMimeType());
        document.setFileSize(documentData.getFileSize());
        document.setCreatedAt(LocalDate.now());
        document.setContract(contractOpt.get());
        
        return additionalDocumentRepository.save(document);
    }
    
    /**
     * Получает список всех дополнительных соглашений для указанного трудового договора
     * 
     * @param contractId идентификатор трудового договора
     * @return список дополнительных соглашений
     */
    public List<SubsidiaryContract> getSubsidiaryContractsByContractId(String contractId) {
        Optional<Contract> contractOpt = contractRepository.findByContractId(contractId);
        if (contractOpt.isEmpty()) {
            throw new IllegalArgumentException("Трудовой договор с ID " + contractId + " не найден");
        }
        
        return subsidiaryContractRepository.findByContract(contractOpt.get());
    }
    
    /**
     * Получает дополнительное соглашение по его идентификатору
     * 
     * @param subsidiaryContractId идентификатор дополнительного соглашения
     * @return дополнительное соглашение, если найдено
     */
    public Optional<SubsidiaryContract> getSubsidiaryContractById(String subsidiaryContractId) {
        return subsidiaryContractRepository.findBySubsidiaryContractId(subsidiaryContractId);
    }
    
    /**
     * Обновляет дополнительное соглашение
     * 
     * @param subsidiaryContractId идентификатор дополнительного соглашения
     * @param subsidiaryContractData новые данные дополнительного соглашения
     * @return обновленное дополнительное соглашение
     */
    @Transactional
    public SubsidiaryContract updateSubsidiaryContract(String subsidiaryContractId, ContractData.SubsidiaryContractData subsidiaryContractData) {
        Optional<SubsidiaryContract> subsidiaryContractOpt = subsidiaryContractRepository.findBySubsidiaryContractId(subsidiaryContractId);
        if (subsidiaryContractOpt.isEmpty()) {
            throw new IllegalArgumentException("Дополнительное соглашение с ID " + subsidiaryContractId + " не найдено");
        }
        
        SubsidiaryContract subsidiaryContract = subsidiaryContractOpt.get();
        
        // Обновляем только те поля, которые переданы в запросе
        if (subsidiaryContractData.getSubsidiaryContractNumber() != null) {
            subsidiaryContract.setSubsidiaryContractNumber(subsidiaryContractData.getSubsidiaryContractNumber());
        }
        if (subsidiaryContractData.getSubsidiaryContractDate() != null) {
            subsidiaryContract.setSubsidiaryContractDate(subsidiaryContractData.getSubsidiaryContractDate());
        }
        if (subsidiaryContractData.getSubsidiaryContractType() != null) {
            subsidiaryContract.setSubsidiaryContractType(subsidiaryContractData.getSubsidiaryContractType());
        }
        if (subsidiaryContractData.getSubsidiaryContractReason() != null) {
            subsidiaryContract.setSubsidiaryContractReason(subsidiaryContractData.getSubsidiaryContractReason());
        }
        if (subsidiaryContractData.getStartDate() != null) {
            subsidiaryContract.setStartDate(subsidiaryContractData.getStartDate());
        }
        if (subsidiaryContractData.getEndDate() != null) {
            subsidiaryContract.setEndDate(subsidiaryContractData.getEndDate());
        }
        if (subsidiaryContractData.getPosition() != null) {
            subsidiaryContract.setPosition(subsidiaryContractData.getPosition());
        }
        if (subsidiaryContractData.getPositionCode() != null) {
            subsidiaryContract.setPositionCode(subsidiaryContractData.getPositionCode());
        }
        if (subsidiaryContractData.getWorkType() != null) {
            subsidiaryContract.setWorkType(subsidiaryContractData.getWorkType());
        }
        if (subsidiaryContractData.getRemoteWork() != null) {
            subsidiaryContract.setRemoteWork(subsidiaryContractData.getRemoteWork());
        }
        if (subsidiaryContractData.getWorkPlaceAddress() != null) {
            subsidiaryContract.setWorkPlaceAddress(subsidiaryContractData.getWorkPlaceAddress());
        }
        if (subsidiaryContractData.getWorkPlaceKato() != null) {
            subsidiaryContract.setWorkPlaceKato(subsidiaryContractData.getWorkPlaceKato());
        }
        if (subsidiaryContractData.getWorkPlaceCountry() != null) {
            subsidiaryContract.setWorkPlaceCountry(subsidiaryContractData.getWorkPlaceCountry());
        }
        if (subsidiaryContractData.getWorkHours() != null) {
            subsidiaryContract.setWorkHours(subsidiaryContractData.getWorkHours());
        }
        if (subsidiaryContractData.getTariffRate() != null) {
            subsidiaryContract.setTariffRate(subsidiaryContractData.getTariffRate());
        }
        if (subsidiaryContractData.getWorkConditions() != null) {
            subsidiaryContract.setWorkConditions(subsidiaryContractData.getWorkConditions());
        }
        if (subsidiaryContractData.getWorkConditionCode() != null) {
            subsidiaryContract.setWorkConditionCode(subsidiaryContractData.getWorkConditionCode());
        }
        
        return subsidiaryContractRepository.save(subsidiaryContract);
    }
}
