package kz.gov.example.esutd.soap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей, которые не требуется сохранять в БД
 * Используется для полей, выделенных желтым в документации
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreField {
    /**
     * Причина игнорирования поля
     */
    String reason() default "Не требуется сохранение в БД";
}
