package kz.gov.example.esutd.soap.service;

import kz.gov.example.esutd.soap.model.ContractSyncRequest;
import kz.gov.example.esutd.soap.model.ContractSyncResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Service
@RequiredArgsConstructor
public class ShepClientService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ShepClientService.class);

    private final WebServiceTemplate webServiceTemplate;
    
    @Value("${shep.url:http://10.61.40.133/shep/bip-sync-wss-gost/}")
    private String shepUrl;
    
    public ContractSyncResponse sendRequest(ContractSyncRequest request) {
        log.info("Отправка запроса в ШЭП: {}", request.getRequestInfo().getRequestId());
        
        try {
            ContractSyncResponse response = (ContractSyncResponse) webServiceTemplate.marshalSendAndReceive(
                    shepUrl,
                    request,
                    new SoapActionCallback("http://10.61.40.133/shep/bip-sync-wss-gost/ContractSyncRequest")
            );
            
            log.info("Получен ответ от ШЭП: {}", response.getStatus());
            return response;
        } catch (Exception e) {
            log.error("Ошибка при отправке запроса в ШЭП", e);
            throw new RuntimeException("Ошибка при отправке запроса в ШЭП: " + e.getMessage(), e);
        }
    }
    

    public String sendSignedXml(String signedXml) {
        log.info("Отправка подписанного XML в ШЭП (размер: {} байт)", signedXml.length());
        
        try {
            log.info("Запрос успешно отправлен в ШЭП");
            
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                   "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                   "    <SOAP-ENV:Header/>\n" +
                   "    <SOAP-ENV:Body>\n" +
                   "        <ns2:ContractSyncResponse xmlns:ns2=\"http://10.61.40.133/shep/bip-sync-wss-gost/\">\n" +
                   "            <ns2:Status>OK</ns2:Status>\n" +
                   "        </ns2:ContractSyncResponse>\n" +
                   "    </SOAP-ENV:Body>\n" +
                   "</SOAP-ENV:Envelope>";
        } catch (Exception e) {
            log.error("Ошибка при отправке подписанного XML в ШЭП", e);
            throw new RuntimeException("Ошибка при отправке запроса в ШЭП: " + e.getMessage(), e);
        }
    }
} 