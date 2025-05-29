package kz.gov.example.esutd.soap.util.digital;

import kz.gov.pki.kalkan.xmldsig.DsigConstants;
import lombok.Getter;
import java.security.cert.X509Certificate;
import java.util.Objects;


public class CertificateWrapper {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CertificateWrapper.class);
    @Getter
    private final X509Certificate x509Certificate;
    private final String[] signAlg;
    
    public CertificateWrapper(X509Certificate certificate) {
        Objects.requireNonNull(certificate);
        x509Certificate = certificate;
        signAlg = getSignMethodByOID(x509Certificate.getSigAlgOID());
    }

    public String getSignAlgorithmId() {
        return signAlg[0];
    }

    public String getHashAlgorithmId() {
        return signAlg[1];
    }

    private String[] getSignMethodByOID(String oid) {
        log.info("Processing certificate OID: {}", oid);
        
        if ("1.2.398.3.10.1.1.2.3.2".equals(oid) ||
            "1.2.398.3.10.1.1.1.2".equals(oid) || 
            "1.2.840.113549.1.1.5".equals(oid) || 
            "1.2.112.0.2.0.34.101.31.81".equals(oid)) {
            
            return new String[] {
                DsigConstants.ALGO_ID_SIGNATURE_ECGOST34310_2004_ECGOST34311_95,
                DsigConstants.ALGO_ID_DIGEST_ECGOST34311_95
            };
        } else if ("1.2.398.3.10.1.1.1.1".equals(oid) || 
                  "1.2.840.113549.1.1.11".equals(oid) || 
                  "1.2.112.0.2.0.34.101.31.80".equals(oid)) {
            return new String[] {
                DsigConstants.ALGO_ID_SIGNATURE_RSA_SHA1,
                DsigConstants.ALGO_ID_DIGEST_SHA1
            };
        } else {
            log.warn("Unknown signature algorithm OID: {}. Using GOST as default", oid);
            return new String[] {
                DsigConstants.ALGO_ID_SIGNATURE_ECGOST34310_2004_ECGOST34311_95,
                DsigConstants.ALGO_ID_DIGEST_ECGOST34311_95
            };
        }
    }
} 