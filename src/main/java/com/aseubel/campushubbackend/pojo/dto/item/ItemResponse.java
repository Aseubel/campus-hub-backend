package com.aseubel.campushubbackend.pojo.dto.item;

import com.aseubel.campushubbackend.pojo.dto.category.CategoryResponse;
import com.aseubel.campushubbackend.pojo.entity.Category;
import com.aseubel.campushubbackend.pojo.entity.Item;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date 2025/9/16 下午8:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse implements Serializable {
    @TableId(type = IdType.AUTO)
    private String id;
    private String name;
    private String categoryId;
    private String description;
    private String imageUrl;
    // 其他元数据 例如: {"teacher": "李教授", "credits": 3}
    private String metadata;
    private Double score;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    public static ItemResponse fromEntity(Item item) {
        return ItemResponse.builder()
                .id(String.valueOf(item.getId()))
                .name(item.getName())
                .categoryId(String.valueOf(item.getCategoryId()))
                .description(item.getDescription())
                .imageUrl(item.getImageUrl())
                .metadata(item.getMetadata())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .score(item.getScore())
                .build();
    }
}
