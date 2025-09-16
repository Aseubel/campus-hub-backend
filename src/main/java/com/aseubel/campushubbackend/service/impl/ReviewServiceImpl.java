package com.aseubel.campushubbackend.service.impl;

import com.aseubel.campushubbackend.mapper.ReviewMapper;
import com.aseubel.campushubbackend.pojo.entity.Review;
import com.aseubel.campushubbackend.service.ReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:10
 */
@Slf4j
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    @Override
    public Double calculateAverageScore(Long itemId) {
        return reviewMapper.calculateAverageScore(itemId);
    }

    @Override
    public boolean isUserReviewedItem(Long userId, Long itemId) {
        return reviewMapper.isUserReviewedItem(userId, itemId);
    }
}
