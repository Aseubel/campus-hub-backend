package com.aseubel.campushubbackend.mapper;

import com.aseubel.campushubbackend.pojo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Aseubel
 * @date 2025/9/16 上午10:53
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
