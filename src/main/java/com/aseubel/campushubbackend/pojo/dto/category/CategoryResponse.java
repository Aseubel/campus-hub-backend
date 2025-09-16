package com.aseubel.campushubbackend.pojo.dto.category;

import com.aseubel.campushubbackend.pojo.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse implements Serializable {

    private String id;
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
               .id(String.valueOf(category.getId()))
               .name(category.getName())
               .description(category.getDescription())
               .createdAt(category.getCreatedAt())
               .updatedAt(category.getUpdatedAt())
               .build();
    }
}
