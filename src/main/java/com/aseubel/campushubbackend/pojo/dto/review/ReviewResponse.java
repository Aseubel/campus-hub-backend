package com.aseubel.campushubbackend.pojo.dto.review;

import com.aseubel.campushubbackend.pojo.dto.item.ItemResponse;
import com.aseubel.campushubbackend.pojo.dto.user.UserResponse;
import com.aseubel.campushubbackend.pojo.entity.Review;
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
public class ReviewResponse implements Serializable {
    private String id;
    private String userId;
    private String itemId;
    private UserResponse user;
    private ItemResponse item;
    private Integer score;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    public static ReviewResponse fromEntity(Review review) {
        return ReviewResponse.builder()
                .id(String.valueOf(review.getId()))
                .userId(String.valueOf(review.getUserId()))
                .itemId(String.valueOf(review.getItemId()))
                .score(review.getScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
