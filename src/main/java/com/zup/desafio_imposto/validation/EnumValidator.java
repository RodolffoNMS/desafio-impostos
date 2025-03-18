package com.zup.desafio_imposto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> enumValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (!enumValues.contains(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Ops! Esse valor não é válido. Tente um dos seguintes: "
                            + String.join(", ", enumValues)
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}