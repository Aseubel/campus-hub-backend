package com.aseubel.campushubbackend.service.impl;

import com.aseubel.campushubbackend.mapper.ItemMapper;
import com.aseubel.campushubbackend.pojo.entity.Item;
import com.aseubel.campushubbackend.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Aseubel
 * @date 2025/9/16 下午5:09
 */
@Slf4j
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
}
