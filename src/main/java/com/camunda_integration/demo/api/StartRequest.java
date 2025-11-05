package com.camunda_integration.demo.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StartRequest(
        @NotBlank String businessKey,
        @Email String reviewEmail,
        @NotBlank String kcTokenUrl,
        @NotBlank String kcClientId,
        @NotBlank String kcClientSecret,
        @NotBlank String orionBaseUrl,
        @NotBlank String applicantEmail
) {}
