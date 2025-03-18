package com.zup.desafio_imposto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum  {

    Class<? extends Enum<?>> enumClass();
    String message() default "Ops! Esse valor não é válido. Tente um dos seguintes:\n {enumValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}