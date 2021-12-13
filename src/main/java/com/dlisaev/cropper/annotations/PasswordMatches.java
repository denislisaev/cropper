package com.dlisaev.cropper.annotations;

import com.dlisaev.cropper.validators.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Password don`t match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
