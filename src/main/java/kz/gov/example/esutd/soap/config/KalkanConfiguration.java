package kz.gov.example.esutd.soap.config;

import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.xmldsig.KncaXS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.Security;

@Configuration
public class KalkanConfiguration {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KalkanConfiguration.class);

    @Bean
    public KalkanProvider kalkanProvider() {
        log.info("Initializing KalkanProvider...");
        
        try {
            org.apache.xml.security.Init.init();
            
            KalkanProvider kalkanProvider = new KalkanProvider();
            Security.insertProviderAt(kalkanProvider, 1);
            
            KncaXS.loadXMLSecurity();
            
            org.apache.xml.security.algorithms.SignatureAlgorithm.registerDefaultAlgorithms();
            
            try {
                org.apache.xml.security.algorithms.JCEMapper.Algorithm signatureAlgorithm = 
                    new org.apache.xml.security.algorithms.JCEMapper.Algorithm(
                        "EC", 
                        "ECGOST3410-2015", 
                        "Signature" 
                    );
                
                org.apache.xml.security.algorithms.JCEMapper.Algorithm digestAlgorithm = 
                    new org.apache.xml.security.algorithms.JCEMapper.Algorithm(
                        "", 
                        "ECGOST3411-2015", 
                        "MessageDigest" 
                    );
                
                org.apache.xml.security.algorithms.JCEMapper.register(
                    "urn:ietf:params:xml:ns:pkigovkz:xmlsec:algorithms:gostr34102015-gostr34112015-512",
                    signatureAlgorithm
                );
                
                org.apache.xml.security.algorithms.JCEMapper.register(
                    "urn:ietf:params:xml:ns:pkigovkz:xmlsec:algorithms:gostr34112015-512",
                    digestAlgorithm
                );
                
                log.info("GOST algorithms registered successfully");
            } catch (Exception e) {
                log.warn("Error registering additional algorithms, but continuing", e);
            }
            
            log.info("KalkanProvider initialized successfully");
            return kalkanProvider;
        } catch (Exception e) {
            log.error("Failed to initialize KalkanProvider", e);
            throw new RuntimeException("Failed to initialize KalkanProvider", e);
        }
    }
}