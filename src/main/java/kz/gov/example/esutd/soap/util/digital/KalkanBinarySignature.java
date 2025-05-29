package kz.gov.example.esutd.soap.util.digital;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.security.PrivateKey;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;


@Component
public class KalkanBinarySignature {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KalkanBinarySignature.class);

    public Document sign(Document document, X509Certificate certificate, PrivateKey privateKey) {
        try {
            log.info("Signing XML with ECGOST key using simple binary signature");
            log.info("Key algorithm: {}", privateKey.getAlgorithm());
            
            String xmlString = documentToString(document);
            byte[] dataToSign = xmlString.getBytes(StandardCharsets.UTF_8);
            
            MessageDigest md = MessageDigest.getInstance("GOST3411-2015-512", "KALKAN");
            byte[] digest = md.digest(dataToSign);
            log.info("Digest created using GOST3411-2015-512, length: {}", digest.length);
            
            Signature signature = Signature.getInstance("ECGOST3410-2015-512", "KALKAN");
            signature.initSign(privateKey);
            signature.update(dataToSign);
            byte[] signatureBytes = signature.sign();
            
            String base64Signature = Base64.getEncoder().encodeToString(signatureBytes);
            log.info("Binary signature created, length: {}", base64Signature.length());
            
            Element root = document.getDocumentElement();
            
            Element headerElement = null;
            NodeList headerNodes = document.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Header");
            
            if (headerNodes.getLength() > 0) {
                headerElement = (Element) headerNodes.item(0);
            } else {
                headerElement = document.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "soapenv:Header");
                Element bodyElement = (Element) document.getElementsByTagNameNS(
                    "http://schemas.xmlsoap.org/soap/envelope/", "Body").item(0);
                root.insertBefore(headerElement, bodyElement);
            }
            
            Element signatureElement = document.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:Signature");
            signatureElement.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
            
            Element signatureValueElement = document.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:SignatureValue");
            signatureValueElement.setTextContent(base64Signature);
            signatureElement.appendChild(signatureValueElement);
            
            Element keyInfoElement = document.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:KeyInfo");
            Element x509DataElement = document.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:X509Data");
            Element x509CertificateElement = document.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:X509Certificate");
            x509CertificateElement.setTextContent(Base64.getEncoder().encodeToString(certificate.getEncoded()));
            x509DataElement.appendChild(x509CertificateElement);
            keyInfoElement.appendChild(x509DataElement);
            signatureElement.appendChild(keyInfoElement);
            
            headerElement.appendChild(signatureElement);
            
            log.info("XML signed successfully, signature added to SOAP Header");
            return document;
        } catch (Exception e) {
            log.error("Error creating signature", e);
            throw new RuntimeException("Error creating signature", e);
        }
    }
    
    public String documentToString(Document doc) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(doc), new StreamResult(os));
            return os.toString();
        } catch (Exception e) {
            log.error("Error converting document to string", e);
            throw new RuntimeException("Error converting document to string", e);
        }
    }
} 