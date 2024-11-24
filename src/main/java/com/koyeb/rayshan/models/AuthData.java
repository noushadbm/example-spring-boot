package com.koyeb.rayshan.models;

import lombok.Data;

@Data
public class AuthData {
    private String status;
    private String token;
    private String userName;
    private long userId;
    private long expiry;
}
