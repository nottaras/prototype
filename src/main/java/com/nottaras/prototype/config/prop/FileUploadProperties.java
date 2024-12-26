package com.nottaras.prototype.config.prop;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app.file.upload")
public record FileUploadProperties(
        @NotNull Long maxSize,
        @NotEmpty List<String> allowedTypes
) {
}