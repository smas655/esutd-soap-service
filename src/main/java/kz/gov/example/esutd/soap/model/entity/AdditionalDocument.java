package kz.gov.example.esutd.soap.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Сущность для хранения информации о дополнительных документах к трудовому договору
 */
@Entity
@Table(name = "additional_documents")
@NoArgsConstructor
@Data
public class AdditionalDocument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "document_id", nullable = false)
    private String documentId;
    
    @Column(name = "document_type", nullable = false, length = 100)
    private String documentType;
    
    @Column(name = "document_number", length = 60)
    private String documentNumber;
    
    @Column(name = "document_date")
    private LocalDate documentDate;
    
    @Column(name = "document_name", length = 255)
    private String documentName;
    
    @Column(name = "document_description", length = 1000)
    private String documentDescription;
    
    @Column(name = "document_data", columnDefinition = "TEXT")
    private String documentData;
    
    @Column(name = "file_name", length = 255)
    private String fileName;
    
    @Column(name = "file_mime_type", length = 100)
    private String fileMimeType;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
}
