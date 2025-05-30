package kz.gov.example.esutd.soap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Конфигурация JAXB для работы с XML
 */
@Configuration
public class JaxbConfig {
    
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("kz.gov.example.esutd.soap.model");
        
        marshaller.setMarshallerProperties(java.util.Map.of(
            "jaxb.formatted.output", true,
            "com.sun.xml.bind.xmlDeclaration", false
        ));
        
        return marshaller;
    }
}
