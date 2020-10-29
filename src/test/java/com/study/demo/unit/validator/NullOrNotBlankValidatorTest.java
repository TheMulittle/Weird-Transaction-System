package com.study.demo.unit.validator;

import javax.validation.ConstraintValidatorContext;

import com.study.demo.validator.NullOrNotBlankValidator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NullOrNotBlankValidatorTest {

    @Mock
    ConstraintValidatorContext mockContext;

    @Test
    public void shouldReturnFalse_ifStringIsEmpty() {
        NullOrNotBlankValidator validator = new NullOrNotBlankValidator();
        Assertions.assertThat(validator.isValid("", mockContext)).isFalse();
        Assertions.assertThat(validator.isValid(" ", mockContext)).isFalse();
    }

    @Test
    public void shouldReturnTrue_ifStringIsNull() {
        NullOrNotBlankValidator validator = new NullOrNotBlankValidator();
        Assertions.assertThat(validator.isValid(null, mockContext)).isTrue();
    }

    @Test
    public void shouldReturnTrue_ifStringIsNotEmptyAndNotNull() {
        NullOrNotBlankValidator validator = new NullOrNotBlankValidator();
        Assertions.assertThat(validator.isValid("test", mockContext)).isTrue();
    }

}