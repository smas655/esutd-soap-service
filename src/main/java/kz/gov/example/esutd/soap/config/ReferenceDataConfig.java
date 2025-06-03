package kz.gov.example.esutd.soap.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.reference-data")
public class ReferenceDataConfig {
    
    private String source = "excel";
    private ExcelConfig excel = new ExcelConfig();
    
    @Data
    public static class ExcelConfig {
        private boolean enabled = true;
        private String positionFile;
        private String terminationReasonFile;
        private String workingHoursFile;
        private String workConditionsFile;
        
        private ColumnConfig position = new ColumnConfig();
        private ColumnConfig terminationReason = new ColumnConfig();
        private ColumnConfig workingHours = new ColumnConfig();
        private ColumnConfig workConditions = new ColumnConfig();
    }
    
    @Data
    public static class ColumnConfig {
        private String codeColumn = "CODE";
        private String nameColumn = "NAME_RU";
    }
    
    public boolean isExcelSource() {
        return "excel".equalsIgnoreCase(source);
    }
    
    public boolean isDatabaseSource() {
        return "database".equalsIgnoreCase(source);
    }
} 