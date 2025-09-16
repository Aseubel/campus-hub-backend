package com.aseubel.campushubbackend.pojo.dto.auth;

import com.aseubel.campushubbackend.pojo.dto.user.UserInfoResponse;
import com.aseubel.campushubbackend.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {

    private UserInfoResponse user;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 访问令牌过期时间（毫秒）
     */
    private String accessTokenExpiration;

    /**
     * 刷新令牌过期时间（毫秒）
     */
    private String refreshTokenExpiration;

    /**
     * 从用户实体创建登录响应
     */
    public static LoginResponse fromUser(User user, String accessToken, String refreshToken,
                                         String accessTokenExpiration, String refreshTokenExpiration) {
        return LoginResponse.builder()
                .user(UserInfoResponse.of(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(accessTokenExpiration)
                .refreshTokenExpiration(refreshTokenExpiration)
                .build();
    }
}