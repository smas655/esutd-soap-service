package kz.gov.example.esutd.soap.model;

import javax.xml.bind.annotation.XmlTransient;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Base class for all JAXB-generated types to prevent duplicate method issues.
 * This class provides common functionality and can be extended by generated classes.
 */
@XmlTransient
@EqualsAndHashCode
@ToString
public abstract class BaseJaxbType {}
