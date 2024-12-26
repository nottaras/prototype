package com.nottaras.prototype.validation;

import com.nottaras.prototype.config.prop.FileUploadProperties;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultipartFileValidatorTest {

    @InjectMocks
    private MultipartFileValidator multipartFileValidator;

    @Mock
    private FileUploadProperties fileUploadProperties;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @BeforeEach
    public void setup() {
        mockConstraintViolationSetup();
    }

    @Test
    public void givenValidFile_whenIsValidCalled_thenReturnTrue() {
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        when(fileUploadProperties.maxSize()).thenReturn(1024L);
        when(fileUploadProperties.allowedTypes()).thenReturn(List.of("image/jpeg"));

        assertTrue(multipartFileValidator.isValid(file, constraintValidatorContext));
    }

    @Test
    public void givenEmptyFile_whenIsValidCalled_thenReturnFalse() {
        var file = new MockMultipartFile("file", new byte[0]);

        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        assertFalse(isValid);
    }

    @Test
    public void givenNullInsteadOfFile_whenIsValidCalled_thenReturnFalse() {
        boolean isValid = multipartFileValidator.isValid(null, constraintValidatorContext);

        assertFalse(isValid);
    }

    @Test
    public void givenFileSizeExceedingLimit_whenIsValidCalled_thenReturnFalse() {
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[2048]);
        when(fileUploadProperties.maxSize()).thenReturn(1024L);

        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        assertFalse(isValid);
    }

    @Test
    public void givenFileNullContentType_whenIsValidCalled_thenReturnFalse() {
        var file = new MockMultipartFile("file", "test.pdf", null, "test".getBytes());
        when(fileUploadProperties.maxSize()).thenReturn(1024L);

        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        assertFalse(isValid);
    }

    @Test
    public void givenFileUnsupportedContentType_whenIsValidCalled_thenReturnFalse() {
        var file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test".getBytes());
        when(fileUploadProperties.allowedTypes()).thenReturn(List.of("image/jpeg"));
        when(fileUploadProperties.maxSize()).thenReturn(1024L);

        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        assertFalse(isValid);
    }

    private void mockConstraintViolationSetup() {
        lenient().when(constraintValidatorContext.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilder);
        lenient().when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
    }
}
