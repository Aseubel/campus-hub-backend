package com.aseubel.campushubbackend.pojo.dto.item;

import com.aseubel.campushubbackend.pojo.entity.Category;
import com.aseubel.campushubbackend.pojo.entity.Item;
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
public class ItemRequest implements Serializable {

    private String name;
    private Long categoryId;
    private String description;
    private String imageUrl;
    // 其他元数据 例如: {"teacher": "李教授", "credits": 3}
    private String metadata;

    public Item toEntity() {
        return Item.builder()
                .name(name)
                .categoryId(categoryId)
                .description(description)
                .imageUrl(imageUrl)
                .metadata(metadata)
                .build();
    }
}
