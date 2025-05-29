package kz.gov.example.esutd.soap.config;

import org.springframework.ws.soap.SoapMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.xml.transform.TransformerFactory;
import java.io.ByteArrayOutputStream;
import javax.xml.transform.stream.StreamResult;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.MessageFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class SmartBridgeMessageFormatter {

    private final String serviceId;
    
    public SmartBridgeMessageFormatter(String serviceId) {
        this.serviceId = serviceId;
    }
    
    public String wrapRequestForShep(String originalPayload) {
        String messageId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
               "    <SOAP-ENV:Header/>\n" +
               "    <SOAP-ENV:Body>\n" +
               "        <ns3:SendMessage xmlns:ns3=\"http://bip.bee.kz/SyncChannel/v10/Types\" xmlns=\"\">\n" +
               "            <request>\n" +
               "                <requestInfo>\n" +
               "                    <messageId>" + messageId + "</messageId>\n" +
               "                    <correlationId>" + correlationId + "</correlationId>\n" +
               "                    <serviceId>" + serviceId + "</serviceId>\n" +
               "                    <messageDate>" + currentDateTime + "</messageDate>\n" +
               "                    <sender>\n" +
               "                        <senderId>ESUTD</senderId>\n" +
               "                        <password>ESUTD</password>\n" +
               "                    </sender>\n" +
               "                    <sessionId>" + sessionId + "</sessionId>\n" +
               "                </requestInfo>\n" +
               "                <requestData>\n" +
               "                    <data xsi:type=\"ns2:requestData\"\n" +
               "                          xmlns:ns2=\"https://ibd.kz/schema\"\n" +
               "                          xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
               "                        " + originalPayload + "\n" +
               "                    </data>\n" +
               "                </requestData>\n" +
               "            </request>\n" +
               "        </ns3:SendMessage>\n" +
               "    </SOAP-ENV:Body>\n" +
               "</SOAP-ENV:Envelope>";
    }
    
    public void formatMessage(SoapMessage message) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TransformerFactory.newInstance().newTransformer().transform(
                    message.getPayloadSource(), new StreamResult(outputStream));
            String originalPayload = outputStream.toString();
            
            String wrappedPayload = wrapRequestForShep(originalPayload);
            
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage(
                    null, 
                    new ByteArrayInputStream(wrappedPayload.getBytes(StandardCharsets.UTF_8)));
            
            SOAPMessage saajMessage = ((org.springframework.ws.soap.saaj.SaajSoapMessage)message).getSaajMessage();
            
            saajMessage.getSOAPPart().setContent(soapMessage.getSOAPPart().getContent());
            saajMessage.saveChanges();
            
            message.setSoapAction("http://bip.bee.kz/SyncChannel/v10/Types");
        } catch (Exception e) {
            throw new RuntimeException("Error formatting message for ShEP: " + e.getMessage(), e);
        }
    }
    
    /**
     * @param shepResponse Ответ от ШЭП в виде строки
     * @return Извлеченные данные
     */
    public String unwrapResponseFromShep(String shepResponse) {
        try {
            return shepResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error unwrapping response from ShEP: " + e.getMessage(), e);
        }
    }
}
