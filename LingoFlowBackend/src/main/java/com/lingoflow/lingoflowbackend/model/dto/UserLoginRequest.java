package com.lingoflow.lingoflowbackend.model.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
