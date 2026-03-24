package com.lingoflow.lingoflowbackend.controller;

import com.lingoflow.lingoflowbackend.common.Result;
import com.lingoflow.lingoflowbackend.model.dto.UserLoginRequest;
import com.lingoflow.lingoflowbackend.model.dto.UserRegisterRequest;
import com.lingoflow.lingoflowbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterRequest request) {
        Long userId = userService.register(request);
        return Result.success(userId);
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request);
        return Result.success(token);
    }
}