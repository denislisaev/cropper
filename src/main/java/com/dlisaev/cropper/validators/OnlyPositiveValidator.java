package com.dlisaev.cropper.validators;

import com.dlisaev.cropper.annotations.OnlyPositiveConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyPositiveValidator implements ConstraintValidator<OnlyPositiveConstraint, Integer> {
    @Override
    public void initialize(OnlyPositiveConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer > 0;
    }
}
