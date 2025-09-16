package com.aseubel.campushubbackend.pojo.dto.review;

import com.aseubel.campushubbackend.pojo.entity.Category;
import com.aseubel.campushubbackend.pojo.entity.Review;
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
 * @date 2025/9/16 下午10:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest implements Serializable {

    private Long userId;
    private Long itemId;
    private Integer score;
    private String content;

    public Review toEntity() {
        return Review.builder()
                .userId(userId)
                .itemId(itemId)
                .score(score)
                .content(content)
                .build();
    }
}
