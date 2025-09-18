package com.aseubel.campushubbackend.pojo.dto.user;

import com.aseubel.campushubbackend.common.annotation.Desensitization;
import com.aseubel.campushubbackend.common.desensitize.DesensitizationTypeEnum;
import com.aseubel.campushubbackend.common.exception.BusinessException;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.pojo.entity.User.RoleEnum;
import com.aseubel.campushubbackend.pojo.entity.User.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * 用户信息响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
public class UserResponse {

    private String id; // 用户ID
    private String username; // 用户名
    @Desensitization(type = DesensitizationTypeEnum.MOBILE)
    private String phone; // 手机号（脱敏）
    private String avatarUrl;
    private String role;
    private String status;

    public static UserResponse of(User user) {
        return UserResponse.builder()
               .id(user.getId().toString())
               .username(user.getName())
               .phone(user.getPhone())
               .avatarUrl(user.getAvatarUrl())
               .role(Optional.ofNullable(user.getRole()).map(RoleEnum::getName).orElse(RoleEnum.USER.getName()))
               .status(Optional.ofNullable(user.getStatus()).map(StatusEnum::getName).orElse(StatusEnum.ACTIVE.getName()))
               .build();
    }

    public static List<UserResponse> ofList(List<User> users) {
        return users.stream().map(UserResponse::of).toList();
    }

    public void checkStatus() {
        if (status.equals(StatusEnum.DISABLED.getName())) {
            throw new BusinessException("该用户已被禁用，请联系管理员");
        }
    }

}