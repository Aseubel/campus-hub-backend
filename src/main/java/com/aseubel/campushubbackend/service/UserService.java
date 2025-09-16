package com.aseubel.campushubbackend.service;

import com.aliyuncs.ram.model.v20150501.ChangePasswordRequest;
import com.aseubel.campushubbackend.pojo.dto.auth.LoginRequest;
import com.aseubel.campushubbackend.pojo.dto.auth.LoginResponse;
import com.aseubel.campushubbackend.pojo.dto.auth.MobileLoginRequest;
import com.aseubel.campushubbackend.pojo.dto.auth.RegisterRequest;
import com.aseubel.campushubbackend.pojo.dto.common.PageResponse;
import com.aseubel.campushubbackend.pojo.dto.user.UserUpdateRequest;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/9/16 上午10:54
 */
public interface UserService extends IService<User> {

    /**
     * 用户名密码登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户注册
     */
    LoginResponse register(RegisterRequest request);

    /**
     * 刷新token
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * 用户登出
     */
    void logout(Long userId);

    /**
     * 发送短信验证码
     */
    String sendSmsCode(String mobile);

    /**
     * 验证短信验证码
     */
    boolean verifySmsCode(String mobile, String code);

    /**
     * 更新用户信息
     */
    void updateUserInfo(User user, UserUpdateRequest request);

    /**
     * 修改密码
     */
    void changePassword(User user, ChangePasswordRequest request);

    @Transactional
    LoginResponse mobileLogin(MobileLoginRequest request);

    /**
     * 获取用户列表（分页）
     */
    List<User> getUserList(int page, int size, String keyword);

    /**
     * 切换用户状态（启用/禁用）
     */
    void toggleUserStatus(Long userId);
}
