package com.aseubel.campushubbackend.service.impl;

import com.aseubel.campushubbackend.mapper.CategoryMapper;
import com.aseubel.campushubbackend.pojo.entity.Category;
import com.aseubel.campushubbackend.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:08
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
