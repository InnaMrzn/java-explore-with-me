package ru.practicum.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

class RestrictedStringValuesValidator implements ConstraintValidator<RestrictedStringValues, String> {
    private String propName;
    private String message;
    private List<String> allowable;

    @Override
    public void initialize(RestrictedStringValues requiredIfChecked) {
        this.propName = requiredIfChecked.propName();
        this.message = requiredIfChecked.message();
        this.allowable = Arrays.asList(requiredIfChecked.values());
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid;
        valid = (value == null) || (this.allowable.stream().anyMatch(s -> s.equalsIgnoreCase(value)));

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message.concat(this.allowable.toString()))
                    .addPropertyNode(this.propName).addConstraintViolation();
        }
        return valid;
    }
}