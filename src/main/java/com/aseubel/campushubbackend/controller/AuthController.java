package com.aseubel.campushubbackend.controller;

import com.aseubel.campushubbackend.common.ApiResponse;
import com.aseubel.campushubbackend.common.annotation.constraint.RequireLogin;
import com.aseubel.campushubbackend.context.UserContext;
import com.aseubel.campushubbackend.pojo.dto.auth.*;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 手机验证码登录
     */
    @PostMapping("/mobile-login")
    public ApiResponse<LoginResponse> mobileLogin(@Valid @RequestBody MobileLoginRequest request) {
        try {
            LoginResponse response = userService.mobileLogin(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            LoginResponse response = userService.register(request);
            return ApiResponse.success("注册成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 刷新token
     */
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                return ApiResponse.badRequest("刷新令牌不能为空");
            }
            LoginResponse response = userService.refreshToken(refreshToken);
            return ApiResponse.success("刷新成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @RequireLogin
    public ApiResponse<Void> logout() {
        try {
            Long userId = UserContext.getCurrentUser().getId();
            userService.logout(userId);
            return ApiResponse.success("登出成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 发送短信验证码 todo 暂时直接返回验证码便于测试，后续需要改为发送短信
     */
    @PostMapping("/send-sms-code")
    public ApiResponse<String> sendSmsCode(@RequestBody SmsRequest request) {
        try {
            String mobile = request.getPhone();

            if (mobile == null || mobile.trim().isEmpty()) {
                return ApiResponse.badRequest("手机号不能为空");
            }

            // 验证手机号格式
            if (!mobile.matches("^1[3-9]\\d{9}$")) {
                return ApiResponse.badRequest("手机号格式不正确");
            }

            String code = userService.sendSmsCode(mobile);
            return ApiResponse.success("验证码发送成功", code);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @RequireLogin
    public ApiResponse<User> getCurrentUser() {
        try {
            User currentUser = UserContext.getCurrentUser();

            return ApiResponse.success("获取成功", currentUser);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

}