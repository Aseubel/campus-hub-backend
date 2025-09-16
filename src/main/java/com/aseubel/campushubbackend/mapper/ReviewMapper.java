package com.aseubel.campushubbackend.mapper;

import com.aseubel.campushubbackend.pojo.entity.Review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:08
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    @Select("SELECT ROUND(AVG(score), 2) FROM `review` WHERE item_id = #{itemId}")
    Double calculateAverageScore(Long itemId);

    @Select("SELECT EXISTS (SELECT 1 FROM `review` WHERE user_id = #{userId} AND item_id = #{itemId})")
    boolean isUserReviewedItem(Long userId, Long itemId);
}
