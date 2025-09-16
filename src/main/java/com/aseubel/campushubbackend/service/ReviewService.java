package com.aseubel.campushubbackend.service;

import com.aseubel.campushubbackend.pojo.entity.Review;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:09
 */
public interface ReviewService extends IService<Review> {
    Double calculateAverageScore(Long categoryId);

    boolean isUserReviewedItem(Long userId, Long itemId);
}
