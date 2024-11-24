package com.koyeb.rayshan.models;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private Long statusCode;
    private String statusMsg;
    private String error;
    private T data;
}
