package com.aseubel.campushubbackend.pojo.entity;

import com.aseubel.campushubbackend.common.constants.BaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/9/16 上午10:36
 */
@TableName("user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String passwordHash;
    private String avatarUrl;
    private String phone;
    private RoleEnum role;
    private StatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    public enum StatusEnum implements BaseEnum<StatusEnum> {
        DISABLED(0, "disabled"),
        ACTIVE(1, "active");

        private final Integer code;
        @EnumValue
        private final String name;

        public static Optional<StatusEnum> of(Integer code) {
            return Optional.ofNullable(BaseEnum.parseByCode(StatusEnum.class, code));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum RoleEnum implements BaseEnum<RoleEnum> {
        USER(0, "user"),
        ADMIN(1, "admin");

        private final Integer code;
        @EnumValue
        private final String name;

        public static Optional<RoleEnum> of(Integer code) {
            return Optional.ofNullable(BaseEnum.parseByCode(RoleEnum.class, code));
        }
    }
}
