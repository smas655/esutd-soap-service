package kz.gov.example.esutd.soap.config;

import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;


@Component
public class SoapMessageInterceptor implements EndpointInterceptor {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SoapMessageInterceptor.class);

    private static final ThreadLocal<String> originalXml = new ThreadLocal<>();
    
    public static String getOriginalXml() {
        return originalXml.get();
    }

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
        
        StringWriter writer = new StringWriter();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.transform(soapMessage.getEnvelope().getSource(), new StreamResult(writer));
        String xml = writer.toString();
        
        originalXml.set(xml);
        log.debug("Original SOAP XML saved: {}", xml);
        
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        originalXml.remove();
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        originalXml.remove();
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        originalXml.remove();
    }
}