package com.temelio.nonprofit.email.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ErrorResponse {

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("message")
    private String message;
}

