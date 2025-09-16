package com.aseubel.campushubbackend.controller;

import com.aseubel.campushubbackend.common.ApiResponse;
import com.aseubel.campushubbackend.pojo.dto.category.CategoryRequest;
import com.aseubel.campushubbackend.pojo.dto.category.CategoryResponse;
import com.aseubel.campushubbackend.pojo.entity.Category;
import com.aseubel.campushubbackend.service.CategoryService;
import com.aseubel.campushubbackend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:00
 */
@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        try {
            List<Category> categories = categoryService.list();
            List<CategoryResponse> categoryResponses =
                    categories.stream()
                            .map(CategoryResponse::fromEntity)
                            .toList();
            return ApiResponse.success(categoryResponses);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        try {
            if (!categoryService.save(request.toEntity())) {
                return ApiResponse.error("创建新类别失败！");
            }
            return ApiResponse.success(CategoryResponse.fromEntity(request.toEntity()));
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryRequest request) {
        try {
            Category category = request.toEntity();
            category.setId(id);
            if (!categoryService.updateById(category)) {
                return ApiResponse.error("更新类别失败！");
            }
            return ApiResponse.success(CategoryResponse.fromEntity(category));
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable("id") Long id) {
        try {
            if (!categoryService.removeById(id)) {
                return ApiResponse.error("删除类别失败！");
            }
            return ApiResponse.success("删除成功！");
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }
}
