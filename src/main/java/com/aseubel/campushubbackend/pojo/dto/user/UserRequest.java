package com.aseubel.campushubbackend.pojo.dto.user;

import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.pojo.entity.User.RoleEnum;
import com.aseubel.campushubbackend.pojo.entity.User.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {

    private String username;
    private String phone;
    private String avatarUrl;
    private String role;
    private String status;

    public User toEntity() {
        return User.builder()
               .name(username)
               .phone(phone)
               .avatarUrl(avatarUrl)
               .role(RoleEnum.of(Integer.parseInt(role)).orElse(null))
               .status(StatusEnum.of(Integer.parseInt(status)).orElse(null))
               .build();
    }
}