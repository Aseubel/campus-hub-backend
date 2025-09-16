package com.aseubel.campushubbackend.controller;

import com.aseubel.campushubbackend.common.ApiResponse;
import com.aseubel.campushubbackend.pojo.dto.item.ItemRequest;
import com.aseubel.campushubbackend.pojo.dto.item.ItemResponse;
import com.aseubel.campushubbackend.pojo.entity.Item;
import com.aseubel.campushubbackend.service.ItemService;
import com.aseubel.campushubbackend.service.ReviewService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/9/16 下午8:27
 */
@Slf4j
@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ReviewService reviewService;

    @GetMapping("")
    public ApiResponse<List<ItemResponse>> getByCategory(@RequestParam("categoryId") Long categoryId) {
        try {
            QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_id", categoryId);
            List<Item> items = itemService.list(queryWrapper);

            for (Item item : items) {
                item.setScore(calculateAverageScore(item.getId()));
            }

            List<ItemResponse> itemResponses =
                    items.stream()
                            .map(ItemResponse::fromEntity)
                            .toList();

            return ApiResponse.success(itemResponses);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    private Double calculateAverageScore(Long itemId) {
        return reviewService.calculateAverageScore(itemId);
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemResponse> queryById(@PathVariable("id") Long id) {
        try {
            Item item = itemService.getById(id);
            if (item == null) {
                return ApiResponse.notFound("找不到该条目");
            }
            ItemResponse itemResponse = ItemResponse.fromEntity(item);
            itemResponse.setScore(calculateAverageScore(item.getId()));
            return ApiResponse.success(itemResponse);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ApiResponse<List<ItemResponse>> getAll() {
        try {
            List<Item> items = itemService.list();
            List<ItemResponse> itemResponses =
                    items.stream()
                            .map(ItemResponse::fromEntity)
                            .toList();
            return ApiResponse.success(itemResponses);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("")
    public ApiResponse<ItemResponse> create(@RequestBody ItemRequest request) {
        try {
            if (!itemService.save(request.toEntity())) {
                return ApiResponse.error("创建新类别失败！");
            }
            return ApiResponse.success(ItemResponse.fromEntity(request.toEntity()));
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<ItemResponse> update(@PathVariable("id") Long id, @RequestBody ItemRequest request) {
        try {
            Item item = request.toEntity();
            item.setId(id);
            if (!itemService.updateById(item)) {
                return ApiResponse.error("更新失败！");
            }
            return ApiResponse.success(ItemResponse.fromEntity(item));
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        try {
            if (!itemService.removeById(id)) {
                return ApiResponse.error("删除失败！");
            }
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }
}
