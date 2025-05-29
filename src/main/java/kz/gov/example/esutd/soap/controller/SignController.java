package kz.gov.example.esutd.soap.controller;

import kz.gov.example.esutd.soap.model.entity.Contract;
import kz.gov.example.esutd.soap.model.entity.Employee;
import kz.gov.example.esutd.soap.model.entity.Employer;
import kz.gov.example.esutd.soap.service.ContractService;
import kz.gov.example.esutd.soap.util.SignatureVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.time.format.DateTimeParseException;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/sign")
@RequiredArgsConstructor
@Slf4j
public class SignController {

    private final ContractService contractService;
    private final SignatureVerifier signatureVerifier;
    
    @PostMapping("/soap")
    public String signSoapXml(@RequestBody String soapXml) {
        try {
            if (soapXml == null || soapXml.trim().isEmpty()) {
                throw new IllegalArgumentException("SOAP XML cannot be null or empty");
            }
            
            log.info("Received request to sign SOAP XML (length: {})", soapXml.length());
            
            try {
                extractAndSaveContractData(soapXml);
                log.info("Contract data successfully extracted and saved to database");
            } catch (Exception e) {
                log.error("Error extracting and saving contract data", e);
            }
            
            return signatureVerifier.sign(soapXml);
        } catch (Exception e) {
            log.error("Error signing SOAP XML", e);
            throw new RuntimeException("Error signing SOAP XML: " + e.getMessage(), e);
        }
    }
    
    private void extractAndSaveContractData(String soapXml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(soapXml)));
        
        log.info("Parsing SOAP XML document. Root element: {}", doc.getDocumentElement().getNodeName());
        
        log.info("Full XML document structure:");
        logDocumentStructure(doc.getDocumentElement(), "");
        
        NodeList laborContractNodes = doc.getElementsByTagName("LaborContract");
        if (laborContractNodes.getLength() == 0) {
            laborContractNodes = doc.getElementsByTagName("esutd:LaborContract");
        }
        
        if (laborContractNodes.getLength() == 0) {
            laborContractNodes = doc.getElementsByTagNameNS("*", "LaborContract");
        }
        
        if (laborContractNodes.getLength() == 0) {
            NodeList bodyNodes = doc.getElementsByTagName("Body");
            if (bodyNodes.getLength() > 0) {
                Element bodyElement = (Element) bodyNodes.item(0);
                NodeList bodyChildren = bodyElement.getChildNodes();
                for (int i = 0; i < bodyChildren.getLength(); i++) {
                    Node node = bodyChildren.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE && 
                        (node.getLocalName().equals("LaborContract") || 
                         node.getNodeName().contains("LaborContract"))) {
                        laborContractNodes = new NodeListWrapper(node);
                        break;
                    }
                }
            }
        }
        
        if (laborContractNodes.getLength() == 0) {
            log.warn("No LaborContract element found in SOAP message. Printing document structure:");
            logDocumentStructure(doc.getDocumentElement(), "");
            return;
        }
        
        log.info("Found LaborContract element: {}", laborContractNodes.item(0).getNodeName());
        Element laborContractElement = (Element) laborContractNodes.item(0);
        
        String employeeIin = getElementTextContent(laborContractElement, "employeeIin");
        String lastName = getElementTextContent(laborContractElement, "lastName");
        String firstName = getElementTextContent(laborContractElement, "firstName");
        String middleName = getElementTextContent(laborContractElement, "middleName");
        String birthDateStr = getElementTextContent(laborContractElement, "birthDate");
        String genderStr = getElementTextContent(laborContractElement, "gender");
        String citizenship = getElementTextContent(laborContractElement, "citizenship");
        
        String employeeAddressCountry = getElementTextContent(laborContractElement, "employeeAddressCountry");
        String employeeAddressKatoCode = getElementTextContent(laborContractElement, "employeeAddressKatoCode");
        String employeeAddressCity = getElementTextContent(laborContractElement, "employeeAddressCity");
        String employeeAddressStreet = getElementTextContent(laborContractElement, "employeeAddressStreet");
        String employeeAddressBuilding = getElementTextContent(laborContractElement, "employeeAddressBuilding");
        String employeeAddressApartment = getElementTextContent(laborContractElement, "employeeAddressApartment");
        String employeeAddressPostalCode = getElementTextContent(laborContractElement, "employeeAddressPostalCode");
        
        String employeeBankName = getElementTextContent(laborContractElement, "employeeBankName");
        String employeeBik = getElementTextContent(laborContractElement, "employeeBik");
        String employeeAccountNumber = getElementTextContent(laborContractElement, "employeeAccountNumber");
        String employeePhoneNumber = getElementTextContent(laborContractElement, "employeePhoneNumber");
        String employeeHrPhoneNumber = getElementTextContent(laborContractElement, "employeeHrPhoneNumber");
        
        String employerBin = getElementTextContent(laborContractElement, "employerBin");
        String employerName = getElementTextContent(laborContractElement, "employerName");
        String employerKatoCode = getElementTextContent(laborContractElement, "employerKatoCode");
        String employerType = getElementTextContent(laborContractElement, "employerType");
        
        String employerAddressCountry = getElementTextContent(laborContractElement, "employerAddressCountry");
        String employerAddressKatoCode = getElementTextContent(laborContractElement, "employerAddressKatoCode");
        String employerAddressCity = getElementTextContent(laborContractElement, "employerAddressCity");
        String employerAddressStreet = getElementTextContent(laborContractElement, "employerAddressStreet");
        String employerAddressBuilding = getElementTextContent(laborContractElement, "employerAddressBuilding");
        String employerAddressPostalCode = getElementTextContent(laborContractElement, "employerAddressPostalCode");
        
        String employerPhone = getElementTextContent(laborContractElement, "employerPhone");
        String employerEmail = getElementTextContent(laborContractElement, "employerEmail");
        String employerBankName = getElementTextContent(laborContractElement, "employerBankName");
        String employerBik = getElementTextContent(laborContractElement, "employerBik");
        String employerAccountNumber = getElementTextContent(laborContractElement, "employerAccountNumber");
        
        String contractNumber = getElementTextContent(laborContractElement, "contractNumber");
        String contractDateStr = getElementTextContent(laborContractElement, "contractDate");
        String contractDurationType = getElementTextContent(laborContractElement, "contractDurationType");
        String startDateStr = getElementTextContent(laborContractElement, "startDate");
        String endDateStr = getElementTextContent(laborContractElement, "endDate");
        String position = getElementTextContent(laborContractElement, "position");
        String positionCode = getElementTextContent(laborContractElement, "positionCode");
        String workType = getElementTextContent(laborContractElement, "workType");
        String remoteWorkStr = getElementTextContent(laborContractElement, "remoteWork");
        String workPlaceAddress = getElementTextContent(laborContractElement, "workPlaceAddress");
        String workPlaceKato = getElementTextContent(laborContractElement, "workPlaceKato");
        String workPlaceCountry = getElementTextContent(laborContractElement, "workPlaceCountry");
        String workHours = getElementTextContent(laborContractElement, "workHours");
        String tariffRateStr = getElementTextContent(laborContractElement, "tariffRate");
        String workConditions = getElementTextContent(laborContractElement, "workConditions");
        String workConditionCode = getElementTextContent(laborContractElement, "workConditionCode");
        String contractTypeStr = getElementTextContent(laborContractElement, "contractType");
        String generalSkills = getElementTextContent(laborContractElement, "generalSkills");
        String professionalSkills = getElementTextContent(laborContractElement, "professionalSkills");
        String department = getElementTextContent(laborContractElement, "department");
        
        Contract.ContractType contractType = Contract.ContractType.INDEFINITE;
        if (contractTypeStr != null) {
            try {
                int typeId = Integer.parseInt(contractTypeStr);
                contractType = switch (typeId) {
                    case 1 -> Contract.ContractType.INDEFINITE;
                    case 2 -> Contract.ContractType.FIXED_TERM_ONE_YEAR_PLUS;
                    case 3 -> Contract.ContractType.SEASONAL;
                    case 4 -> Contract.ContractType.SPECIFIC_WORK;
                    case 5 -> Contract.ContractType.TEMPORARY_REPLACEMENT;
                    case 6 -> Contract.ContractType.SEASONAL_WORK;
                    case 7 -> Contract.ContractType.FOREIGN_LABOR_PERMIT;
                    case 8 -> Contract.ContractType.CHILD_CARE_LEAVE;
                    default -> {
                        log.warn("Unknown contract type ID: {}", typeId);
                        yield Contract.ContractType.INDEFINITE;
                    }
                };
            } catch (NumberFormatException e) {
                log.warn("Invalid contract type format: {}", contractTypeStr, e);
            }
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            lastName = "Неизвестно";
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            firstName = "Неизвестно";
        }
        if (genderStr == null || genderStr.trim().isEmpty()) {
            genderStr = "MALE";
        }
        if (citizenship == null || citizenship.trim().isEmpty()) {
            citizenship = "KZ";
        }
        
        Employee employee = new Employee();
        if (employeeIin == null || employeeIin.trim().isEmpty()) {
            log.warn("Employee IIN is missing in the SOAP message. Setting a default value.");
            employeeIin = "000000000000";
        }
        employee.setIin(employeeIin);
        
        employee.setLastName(lastName);
        employee.setFirstName(firstName);
        employee.setMiddleName(middleName);
        
        LocalDate birthDate = null;
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                birthDate = LocalDate.parse(birthDateStr);
            } catch (DateTimeParseException e) {
                log.warn("Invalid birth date format: {}. Using default value.", birthDateStr);
                birthDate = LocalDate.of(1900, 1, 1);
            }
        } else {
            birthDate = LocalDate.of(1900, 1, 1);
        }
        employee.setBirthDate(birthDate);
        
        try {
            employee.setGender(Employee.Gender.valueOf(genderStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid gender value: {}. Defaulting to MALE", genderStr);
            employee.setGender(Employee.Gender.MALE);
        }
        
        employee.setCitizenship(citizenship);
        
        employee.setAddressCountry(employeeAddressCountry != null ? employeeAddressCountry : "KZ");
        employee.setAddressKatoCode(employeeAddressKatoCode);
        employee.setAddressCity(employeeAddressCity);
        employee.setAddressStreet(employeeAddressStreet);
        employee.setAddressBuilding(employeeAddressBuilding);
        employee.setAddressApartment(employeeAddressApartment);
        employee.setAddressPostalCode(employeeAddressPostalCode);
        
        employee.setBankName(employeeBankName);
        employee.setBik(employeeBik);
        employee.setAccountNumber(employeeAccountNumber);
        employee.setPhoneNumber(employeePhoneNumber);
        employee.setHrPhoneNumber(employeeHrPhoneNumber);
        
        employee.setCreatedAt(LocalDate.now());
        
        Employer employer = new Employer();
        if (employerBin == null || employerBin.trim().isEmpty()) {
            log.warn("Employer BIN is missing in the SOAP message. Setting a default value.");
            employerBin = "000000000000";
        }
        employer.setBin(employerBin);
        employer.setName(employerName != null ? employerName : "Неизвестная организация");
        employer.setKatoCode(employerKatoCode);
        employer.setEmployerType(employerType);
        
        employer.setAddressCountry(employerAddressCountry != null ? employerAddressCountry : "KZ");
        employer.setAddressKatoCode(employerAddressKatoCode);
        employer.setAddressCity(employerAddressCity);
        employer.setAddressStreet(employerAddressStreet);
        employer.setAddressBuilding(employerAddressBuilding);
        employer.setAddressPostalCode(employerAddressPostalCode);
        
        employer.setPhone(employerPhone);
        employer.setEmail(employerEmail);
        employer.setBankName(employerBankName);
        employer.setBik(employerBik);
        employer.setAccountNumber(employerAccountNumber);
        
        employer.setCreatedAt(LocalDate.now());
        
        Contract contract = new Contract();
        contract.setContractId(UUID.randomUUID().toString());
        if (contractNumber == null || contractNumber.trim().isEmpty()) {
            log.warn("Contract number is missing in the SOAP message. Setting a default value.");
            contractNumber = "TD-" + UUID.randomUUID().toString().substring(0, 8);
        }
        contract.setContractNumber(contractNumber);
        
        LocalDate contractDate = null;
        if (contractDateStr != null && !contractDateStr.trim().isEmpty()) {
            try {
                contractDate = LocalDate.parse(contractDateStr);
            } catch (DateTimeParseException e) {
                log.warn("Invalid contract date format: {}. Using current date.", contractDateStr);
                contractDate = LocalDate.now();
            }
        } else {
            contractDate = LocalDate.now();
        }
        contract.setContractDate(contractDate);
        
        contract.setContractDurationType(contractDurationType);
        
        LocalDate startDate = null;
        if (startDateStr != null && !startDateStr.trim().isEmpty()) {
            try {
                startDate = LocalDate.parse(startDateStr);
            } catch (DateTimeParseException e) {
                log.warn("Invalid start date format: {}. Using current date.", startDateStr);
                startDate = LocalDate.now();
            }
        } else {
            startDate = LocalDate.now();
        }
        contract.setStartDate(startDate);
        
        if (endDateStr != null && !endDateStr.trim().isEmpty()) {
            try {
                LocalDate endDate = LocalDate.parse(endDateStr);
                contract.setEndDate(endDate);
            } catch (DateTimeParseException e) {
                log.warn("Invalid end date format: {}. Not setting end date.", endDateStr);
            }
        }
        
        if (position == null || position.trim().isEmpty()) {
            log.warn("Position is missing in the SOAP message. Setting a default value.");
            position = "Не указана";
        }
        contract.setPosition(position);
        
        if (positionCode == null || positionCode.trim().isEmpty()) {
            log.warn("Position code is missing in the SOAP message. Setting a default value.");
            positionCode = "DEFAULT_CODE";
        }
        contract.setPositionCode(positionCode);
        
        contract.setWorkType(workType);
        
        if (remoteWorkStr != null && !remoteWorkStr.trim().isEmpty()) {
            try {
                boolean remoteWork = Boolean.parseBoolean(remoteWorkStr);
                contract.setRemoteWork(remoteWork);
            } catch (Exception e) {
                log.warn("Invalid remote work flag: {}. Not setting remote work flag.", remoteWorkStr);
            }
        }
        
        contract.setWorkPlaceAddress(workPlaceAddress);
        contract.setWorkPlaceKato(workPlaceKato);
        contract.setWorkPlaceCountry(workPlaceCountry);
        contract.setWorkHours(workHours);
        
        if (tariffRateStr != null && !tariffRateStr.trim().isEmpty()) {
            try {
                double tariffRate = Double.parseDouble(tariffRateStr);
                contract.setTariffRate(tariffRate);
            } catch (NumberFormatException e) {
                log.warn("Invalid tariff rate format: {}. Not setting tariff rate.", tariffRateStr);
            }
        }
        
        contract.setWorkConditions(workConditions);
        contract.setWorkConditionCode(workConditionCode);
        
        if (contractTypeStr == null || contractTypeStr.trim().isEmpty()) {
            log.warn("Contract type is missing in the SOAP message. Setting a default value.");
            contractType = Contract.ContractType.INDEFINITE;
        }
        contract.setContractType(contractType);
        
        contract.setGeneralSkills(generalSkills);
        contract.setProfessionalSkills(professionalSkills);
        contract.setDepartment(department);
        contract.setIsActive(true);
        contract.setCreatedAt(LocalDate.now());
        contract.setEmployee(employee);
        contract.setEmployer(employer);
        
        try {
            contractService.createContract(contract, employee, employer);
            log.info("Contract saved to database with ID: {}", contract.getContractId());
        } catch (Exception e) {
            log.error("Error saving contract data to database", e);
            throw e;
        }
    }

    private Element findElementByLocalName(Element parentElement, String localName) {
        if ((parentElement.getLocalName() != null && parentElement.getLocalName().equals(localName)) || 
            parentElement.getNodeName().equals(localName) || 
            parentElement.getNodeName().endsWith(":" + localName)) {
            return parentElement;
        }
        
        NodeList children = parentElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if ((child.getLocalName() != null && child.getLocalName().equals(localName)) || 
                    child.getNodeName().equals(localName) || 
                    child.getNodeName().endsWith(":" + localName)) {
                    return (Element) child;
                } else {
                    Element foundElement = findElementByLocalName((Element) child, localName);
                    if (foundElement != null) {
                        return foundElement;
                    }
                }
            }
        }
        return null;
    }


    private static class NodeListWrapper implements NodeList {
        private final Node node;
        
        public NodeListWrapper(Node node) {
            this.node = node;
        }
        
        @Override
        public Node item(int index) {
            return index == 0 ? node : null;
        }
        
        @Override
        public int getLength() {
            return 1;
        }
    }

    private void logDocumentStructure(Node node, String indent) {
        log.info("{}Node: {}", indent, node.getNodeName());
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                logDocumentStructure(child, indent + "  ");
            }
        }
    }

    private String getElementTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        
        nodeList = parentElement.getElementsByTagName("esutd:" + tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        
        nodeList = parentElement.getElementsByTagNameNS("*", tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        
        NodeList children = parentElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getLocalName() != null && child.getLocalName().equals(tagName) ||
                    child.getNodeName().endsWith(":" + tagName)) {
                    return child.getTextContent();
                }
            }
        }
        
        return null;
    }
}