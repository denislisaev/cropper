package com.dlisaev.cropper.annotations;

import com.dlisaev.cropper.validators.OnlyPositiveValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= OnlyPositiveValidator.class)
public @interface OnlyPositiveConstraint {
    String message() default "Value is negative!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
