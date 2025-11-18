package com.deliverytech.delivery_api.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CategoriaValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoria {
    String message() default "Categoria deve ser uma das opções válidas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
