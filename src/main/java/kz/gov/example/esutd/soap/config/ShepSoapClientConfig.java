package kz.gov.example.esutd.soap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

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
        return marshaller;
    }
    
    @Bean
    public SaajSoapMessageFactory messageFactory() {
        return new SaajSoapMessageFactory();
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