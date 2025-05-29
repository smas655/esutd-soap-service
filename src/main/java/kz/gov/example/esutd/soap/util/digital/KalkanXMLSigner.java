package kz.gov.example.esutd.soap.util.digital;

import kz.gov.pki.kalkan.asn1.knca.KNCAObjectIdentifiers;
import kz.gov.pki.kalkan.asn1.pkcs.PKCSObjectIdentifiers;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.encryption.XMLCipherParameters;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@Slf4j
@Component
public class KalkanXMLSigner {


    public Document sign(Document document, X509Certificate certificate, PrivateKey privateKey) {
        try {
            if (document == null) {
                throw new IllegalArgumentException("Document cannot be null");
            }
            
            if (certificate == null) {
                throw new IllegalArgumentException("Certificate cannot be null");
            }
            
            if (privateKey == null) {
                throw new IllegalArgumentException("PrivateKey cannot be null");
            }
            
            log.info("Signing document using certificate: {}", 
                    certificate.getSubjectX500Principal().getName());
            
            String signAlgOid = certificate.getSigAlgOID();
            log.info("Certificate signature algorithm OID: {}", signAlgOid);
            
            String signatureMethod = getSignatureMethod(signAlgOid);
            String digestMethod = getDigestMethod(signAlgOid);
            String c14nAlgorithmId = Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS;
            
            log.debug("Using signature method: {} and digest method: {}", signatureMethod, digestMethod);
            
            Document doc = document;
            
            Element signatureMethodElement = XMLUtils.createElementInSignatureSpace(doc, "SignatureMethod");
            signatureMethodElement.setAttributeNS(null, "Algorithm", signatureMethod);
            
            Element c14nMethodElement = XMLUtils.createElementInSignatureSpace(doc, "CanonicalizationMethod");
            c14nMethodElement.setAttributeNS(null, "Algorithm", c14nAlgorithmId);
            
            XMLSignature signature = new XMLSignature(doc, "", signatureMethodElement, c14nMethodElement);
            
            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
            
            signature.addDocument("", transforms, digestMethod);
            
            Element root = doc.getDocumentElement();
            
            root.appendChild(signature.getElement());
            
            signature.addKeyInfo(certificate);
            
            signature.sign(privateKey);
            
            log.info("Document signed successfully");
            return document;
        } catch (Exception e) {
            log.error("Error signing document with Kalkan", e);
            throw new RuntimeException("Error signing document with Kalkan", e);
        }
    }
    
    private String getSignatureMethod(String sigAlgOid) {
        if (sigAlgOid.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId())) {
            return Constants.MoreAlgorithmsSpecNS + "rsa-sha1";
        } else if (sigAlgOid.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId())) {
            return Constants.MoreAlgorithmsSpecNS + "rsa-sha256";
        } else if (sigAlgOid.equals(KNCAObjectIdentifiers.gost3411_2015_with_gost3410_2015_512.getId())) {
            return "urn:ietf:params:xml:ns:pkigovkz:xmlsec:algorithms:gostr34102015-gostr34112015-512";
        } else {
            return Constants.MoreAlgorithmsSpecNS + "gost34310-gost34311";
        }
    }
    
    private String getDigestMethod(String sigAlgOid) {
        if (sigAlgOid.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId())) {
            return Constants.MoreAlgorithmsSpecNS + "sha1";
        } else if (sigAlgOid.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId())) {
            return XMLCipherParameters.SHA256;
        } else if (sigAlgOid.equals(KNCAObjectIdentifiers.gost3411_2015_with_gost3410_2015_512.getId())) {
            return "urn:ietf:params:xml:ns:pkigovkz:xmlsec:algorithms:gostr34112015-512";
        } else {
            return Constants.MoreAlgorithmsSpecNS + "gost34311";
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