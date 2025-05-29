package kz.gov.example.esutd.soap.util;

import kz.gov.example.esutd.soap.config.SoapMessageInterceptor;
import kz.gov.example.esutd.soap.model.ContractSyncRequest;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.xmldsig.KncaXS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Enumeration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.InputStream;
import org.xml.sax.InputSource;
import kz.gov.example.esutd.soap.util.digital.KalkanBinarySignature;


@Component
public class SignatureVerifier {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SignatureVerifier.class);

    @Value("${signature.certificate.path}")
    private String certificatePath;
    
    @Value("${signature.certificate.password}")
    private String certificatePassword;
    
    @Value("${signature.certificate.alias}")
    private String certificateAlias;

    static {
        try {
            org.apache.xml.security.Init.init();
            
            KalkanProvider kalkanProvider = new KalkanProvider();
            Security.addProvider(kalkanProvider);
            KncaXS.loadXMLSecurity();
            
            log.info("Kalkan provider initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Kalkan provider", e);
        }
    }


    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private KalkanBinarySignature kalkanBinarySignature;

    public void getStatusFromSignature(Signature signature) {

    }
    
    public boolean verify(String signedXml) {
        try {
            log.debug("Parsing XML string (length: {})", signedXml.length());
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(signedXml)));
            
            log.debug("XML parsed successfully");
            
            NodeList signatures = doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
            
            if (signatures.getLength() == 0) {
                signatures = doc.getElementsByTagName("Signature");
            }
            
            if (signatures.getLength() == 0) {
                log.error("No signature found in document");
                return false;
            }
            
            Element signatureElement = (Element) signatures.item(0);
            log.info("Signature element found: {}", signatureElement.getNodeName());
            
            NodeList signatureValueElements = signatureElement.getElementsByTagNameNS(
                "http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
            
            if (signatureValueElements.getLength() == 0) {
                signatureValueElements = signatureElement.getElementsByTagName("SignatureValue");
            }
            
            if (signatureValueElements.getLength() > 0) {
                log.info("SignatureValue found, length: {}", 
                        signatureValueElements.item(0).getTextContent().length());
            } else {
                log.error("SignatureValue not found");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }
    

    private Document parseXmlDocument(String xmlString) {
        try {
            if (xmlString == null || xmlString.trim().isEmpty()) {
                throw new IllegalArgumentException("XML string cannot be null or empty");
            }
            
            log.debug("Parsing XML string (length: {})", xmlString.length());
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setNamespaceAware(true);
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            Document doc = db.parse(is);
            
            log.debug("XML parsed successfully");
            return doc;
        } catch (Exception e) {
            log.error("Error parsing XML", e);
            throw new RuntimeException("Error parsing XML", e);
        }
    }

    public String sign(String soapXml) {
        try {
            if (soapXml == null || soapXml.trim().isEmpty()) {
                throw new IllegalArgumentException("XML string cannot be null or empty");
            }
            
            log.info("Signing SOAP XML (length: {})", soapXml.length());
            
            Document document = parseXmlDocument(soapXml);
            
            if (document == null) {
                throw new IllegalStateException("Failed to parse XML document");
            }
            
            X509Certificate certificate = loadCertificate();
            PrivateKey privateKey = loadPrivateKey();
            
            Document signedDoc = kalkanBinarySignature.sign(document, certificate, privateKey);
            
            String result = kalkanBinarySignature.documentToString(signedDoc);
            log.info("XML signed successfully");
            
            return result;
        } catch (Exception e) {
            log.error("Error signing SOAP request", e);
            throw new RuntimeException("Failed to sign SOAP request", e);
        }
    }

    private X509Certificate loadCertificate() {
        try {
            log.info("Loading certificate from: {}", certificatePath);
            
            Resource resource = resourceLoader.getResource(certificatePath);
            
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "KALKAN");
            
            try (InputStream is = resource.getInputStream()) {
                keyStore.load(is, certificatePassword.toCharArray());
            }
            
            String alias = certificateAlias;
            if (alias == null || alias.isEmpty()) {
                Enumeration<String> aliases = keyStore.aliases();
                if (aliases.hasMoreElements()) {
                    alias = aliases.nextElement();
                    log.info("Using first alias from keystore: {}", alias);
                } else {
                    throw new IllegalStateException("No aliases found in keystore");
                }
            }
            
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
            
            if (certificate == null) {
                throw new IllegalStateException("Certificate not found in keystore for alias: " + alias);
            }
            
            log.info("Certificate loaded successfully: Subject={}", 
                    certificate.getSubjectX500Principal().getName());
            
            return certificate;
        } catch (Exception e) {
            log.error("Error loading certificate from {}", certificatePath, e);
            throw new RuntimeException("Error loading certificate", e);
        }
    }

    private PrivateKey loadPrivateKey() {
        try {
            Resource resource = resourceLoader.getResource(certificatePath);
            
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "KALKAN");
            
            try (InputStream is = resource.getInputStream()) {
                keyStore.load(is, certificatePassword.toCharArray());
            }
            
            String alias = certificateAlias;
            if (alias == null || alias.isEmpty()) {
                Enumeration<String> aliases = keyStore.aliases();
                if (aliases.hasMoreElements()) {
                    alias = aliases.nextElement();
                } else {
                    throw new IllegalStateException("No aliases found in keystore");
                }
            }
            
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, certificatePassword.toCharArray());
            
            if (privateKey == null) {
                throw new IllegalStateException("Private key not found in keystore for alias: " + alias);
            }
            
            log.info("Private key loaded successfully: Algorithm={}", privateKey.getAlgorithm());
            
            return privateKey;
        } catch (Exception e) {
            log.error("Error loading private key from {}", certificatePath, e);
            throw new RuntimeException("Error loading private key", e);
        }
    }


    public boolean verify(ContractSyncRequest request) {
        try {
            log.info("Verifying signature for request ID: {}", request.getRequestInfo().getRequestId());
            
            String xmlString = SoapMessageInterceptor.getOriginalXml();
            
            if (xmlString == null || xmlString.isEmpty()) {
                log.error("Original XML not found in interceptor");
                return false;
            }
            
            return verify(xmlString);
        } catch (Exception e) {
            log.error("Error verifying signature for JAXB object", e);
            return false;
        }
    }

    private String jaxbObjectToXml(Object jaxbObject) throws Exception {
        JAXBContext context = JAXBContext.newInstance(jaxbObject.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(jaxbObject, sw);
        return sw.toString();
    }

    private <T> T xmlToJaxbObject(String xml, Class<T> clazz) throws Exception {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        
        StringReader reader = new StringReader(xml);
        return clazz.cast(unmarshaller.unmarshal(reader));
    }
} 