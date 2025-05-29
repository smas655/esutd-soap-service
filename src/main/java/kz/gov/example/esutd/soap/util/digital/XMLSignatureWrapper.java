package kz.gov.example.esutd.soap.util.digital;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class XMLSignatureWrapper {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(XMLSignatureWrapper.class);
    private final XMLSignature xmlSignature;
    private final X509Certificate certificate;
    
    public XMLSignatureWrapper(Document document, CertificateWrapper certificateWrapper) {
        try {
            this.certificate = certificateWrapper.getX509Certificate();
            
            String signatureMethodURI = certificateWrapper.getSignAlgorithmId();
            String digestMethodURI = certificateWrapper.getHashAlgorithmId();
            
            log.debug("Creating XMLSignature with signatureMethod: {} and digestMethod: {}", 
                    signatureMethodURI, digestMethodURI);
            
            xmlSignature = new XMLSignature(document, "", signatureMethodURI);
            
            Element root = document.getDocumentElement();
            root.appendChild(xmlSignature.getElement());
            
            Transforms transforms = new Transforms(document);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            
            xmlSignature.addDocument("", transforms, digestMethodURI);
            
            xmlSignature.addKeyInfo(certificate);
            
        } catch (Exception e) {
            log.error("XML Signature creation error", e);
            throw new RuntimeException("XML Signature creation error", e);
        }
    }
    
    public XMLSignatureWrapper(Element signatureElement) {
        try {
            xmlSignature = new XMLSignature(signatureElement, "");
            certificate = null;
        } catch (XMLSecurityException e) {
            log.error("XML Signature creation error from element", e);
            throw new RuntimeException("XML Signature creation error from element", e);
        }
    }
    
    public void sign(PrivateKey privateKey) {
        try {
            String keyAlg = privateKey.getAlgorithm();
            log.debug("Signing with key algorithm: {}", keyAlg);
            
            xmlSignature.sign(privateKey);
            
        } catch (XMLSecurityException e) {
            log.error("XML Signing error", e);
            throw new RuntimeException("XML Signing error", e);
        }
    }
    
    public String getSignedDocument() {
        try {
            Document doc = xmlSignature.getDocument();
            return documentToString(doc);
        } catch (Exception e) {
            log.error("Error converting signed document to string", e);
            throw new RuntimeException("Error converting signed document to string", e);
        }
    }
    
    public boolean check() {
        try {
            return xmlSignature.checkSignatureValue(getCertificate());
        } catch (XMLSecurityException e) {
            log.error("Error checking signature", e);
            return false;
        }
    }
    
    public X509Certificate getCertificate() {
        if (certificate != null) {
            return certificate;
        }
        
        try {
            return xmlSignature.getKeyInfo().getX509Certificate();
        } catch (XMLSecurityException e) {
            log.error("Error getting certificate from signature", e);
            throw new RuntimeException("Error getting certificate from signature", e);
        }
    }
    
    private String documentToString(Document doc) {
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