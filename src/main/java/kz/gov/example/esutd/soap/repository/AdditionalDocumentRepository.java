package kz.gov.example.esutd.soap.repository;

import kz.gov.example.esutd.soap.model.entity.AdditionalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с дополнительными документами к трудовому договору
 */
@Repository
public interface AdditionalDocumentRepository extends JpaRepository<AdditionalDocument, Long> {
    
    /**
     * Найти документ по его уникальному идентификатору
     * 
     * @param documentId уникальный идентификатор документа
     * @return документ, если найден
     */
    Optional<AdditionalDocument> findByDocumentId(String documentId);
    
    /**
     * Найти все документы по типу
     * 
     * @param documentType тип документа
     * @return список документов указанного типа
     */
    List<AdditionalDocument> findByDocumentType(String documentType);
    
    /**
     * Найти все документы по номеру документа
     * 
     * @param documentNumber номер документа
     * @return список документов с указанным номером
     */
    List<AdditionalDocument> findByDocumentNumber(String documentNumber);
}
