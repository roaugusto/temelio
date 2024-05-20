package com.temelio.nonprofit.email.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

}
