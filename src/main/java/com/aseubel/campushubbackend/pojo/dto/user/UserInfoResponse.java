package com.aseubel.campushubbackend.pojo.dto.user;

import com.aseubel.campushubbackend.common.annotation.Desensitization;
import com.aseubel.campushubbackend.common.desensitize.DesensitizationTypeEnum;
import com.aseubel.campushubbackend.pojo.entity.Image;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.pojo.entity.User.RoleEnum;
import com.aseubel.campushubbackend.pojo.entity.User.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * 用户信息响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
public class UserInfoResponse {

    private String id; // 用户ID
    private String username; // 用户名
    @Desensitization(type = DesensitizationTypeEnum.MOBILE)
    private String mobile; // 手机号（脱敏）
    private String avatarUrl;
    private String role;
    private String status;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
               .id(user.getId().toString())
               .username(user.getName())
               .mobile(user.getPhone())
               .avatarUrl(user.getAvatarUrl())
               .role(Optional.ofNullable(user.getRole()).map(RoleEnum::getName).orElse(RoleEnum.USER.getName()))
               .status(Optional.ofNullable(user.getStatus()).map(StatusEnum::getName).orElse(StatusEnum.ACTIVE.getName()))
               .build();
    }

}