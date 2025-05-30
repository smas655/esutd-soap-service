package kz.gov.example.esutd.soap.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.SoapVersion;

@Configuration
public class ShepSoapClientConfig {

    @Value("${shep.url:http://10.61.40.133/shep/bip-sync-wss-gost/}")
    private String shepUrl;
    
    @Value("${shep.service.id:ESUTD_SERVICE}")
    private String serviceId;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("kz.gov.example.esutd.soap.model");
        
        // Настройка свойств маршаллера
        Map<String, Object> marshallerProperties = new HashMap<>();
        marshallerProperties.put("jaxb.formatted.output", true);
        marshaller.setMarshallerProperties(marshallerProperties);
        
        return marshaller;
    }
    
    @Bean
    public SaajSoapMessageFactory messageFactory() {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setSoapVersion(SoapVersion.SOAP_11);
        return messageFactory;
    }
    
    @Bean
    public HttpComponentsMessageSender httpComponentsMessageSender() {
        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        messageSender.setConnectionTimeout(30000);
        messageSender.setReadTimeout(60000);
        return messageSender;
    }
    
    @Bean
    public WebServiceTemplate webServiceTemplate() {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        webServiceTemplate.setMessageFactory(messageFactory());
        webServiceTemplate.setMessageSender(httpComponentsMessageSender());
        webServiceTemplate.setDefaultUri(shepUrl);
        
        return webServiceTemplate;
    }
    
    @Bean
    public SmartBridgeMessageFormatter smartBridgeMessageFormatter() {
        return new SmartBridgeMessageFormatter(serviceId);
    }
}