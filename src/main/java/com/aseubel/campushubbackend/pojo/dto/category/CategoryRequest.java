package com.aseubel.campushubbackend.pojo.dto.category;

import com.aseubel.campushubbackend.pojo.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:00
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest implements Serializable {

    private String name;
    private String description;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .description(description)
                .build();
    }
}
