package com.aseubel.campushubbackend.controller;

import com.aseubel.campushubbackend.common.ApiResponse;
import com.aseubel.campushubbackend.pojo.dto.item.ItemResponse;
import com.aseubel.campushubbackend.pojo.dto.review.ReviewRequest;
import com.aseubel.campushubbackend.pojo.dto.review.ReviewResponse;
import com.aseubel.campushubbackend.pojo.dto.user.UserInfoResponse;
import com.aseubel.campushubbackend.pojo.entity.Item;
import com.aseubel.campushubbackend.pojo.entity.Review;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.service.ItemService;
import com.aseubel.campushubbackend.service.ReviewService;
import com.aseubel.campushubbackend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/9/16 下午10:07
 */
@Slf4j
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ItemService itemService;

    @GetMapping("")
    public ApiResponse<List<ReviewResponse>> getByItem(@RequestParam("itemId") Long itemId) {
        try {
            QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_id", itemId);
            List<Review> items = reviewService.list(queryWrapper);

            List<ReviewResponse> itemResponses =
                    items.stream()
                            .map(ReviewResponse::fromEntity)
                            .map(this::getExtraInfo)
                            .toList();

            return ApiResponse.success(itemResponses);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<ReviewResponse> queryById(@PathVariable("id") Long id) {
        try {
            Review review = reviewService.getById(id);
            if (review == null) {
                return ApiResponse.notFound("找不到该条目");
            }
            ReviewResponse itemResponse = ReviewResponse.fromEntity(review);

            return ApiResponse.success(getExtraInfo(itemResponse));
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ApiResponse<List<ReviewResponse>> getAll() {
        try {
            List<Review> items = reviewService.list();
            List<ReviewResponse> itemResponses =
                    items.stream()
                            .map(ReviewResponse::fromEntity)
                            .map(this::getExtraInfo)
                            .toList();
            return ApiResponse.success(itemResponses);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("")
    public ApiResponse<ReviewResponse> create(@RequestBody ReviewRequest request) {
        try {
            // 评价过该条目则更新，否则创建
            if (reviewService.isUserReviewedItem(request.getUserId(), request.getItemId())) {
                Review review = request.toEntity();
                UpdateWrapper<Review> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("user_id", request.getUserId())
                       .eq("item_id", request.getItemId());
                if (!reviewService.update(review, updateWrapper)) {
                    return ApiResponse.error("更新失败！");
                }
                return ApiResponse.success("已覆盖原评价！", ReviewResponse.fromEntity(review));
            }
            if (!reviewService.save(request.toEntity())) {
                return ApiResponse.error("创建新评论失败！");
            }
            return ApiResponse.success(ReviewResponse.fromEntity(request.toEntity()));
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<ReviewResponse> update(@PathVariable("id") Long id, @RequestBody ReviewRequest request) {
        try {
            Review review = request.toEntity();
            review.setId(id);
            if (!reviewService.updateById(review)) {
                return ApiResponse.error("更新失败！");
            }
            return ApiResponse.success(ReviewResponse.fromEntity(review));
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        try {
            if (!reviewService.removeById(id)) {
                return ApiResponse.error("删除失败！");
            }
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/admin/{id}")
    public ApiResponse<String> deleteAdmin(@PathVariable("id") Long id) {
        try {
            if (!reviewService.removeById(id)) {
                return ApiResponse.error("删除失败！");
            }
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    private ReviewResponse getExtraInfo(ReviewResponse reviewResponse) {
        User user = userService.getById(reviewResponse.getUserId());
        reviewResponse.setUser(UserInfoResponse.of(user));
        Item item = itemService.getById(reviewResponse.getItemId());
        reviewResponse.setItem(ItemResponse.fromEntity(item));
        return reviewResponse;
    }
}
