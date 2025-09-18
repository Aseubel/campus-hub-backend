package com.aseubel.campushubbackend.controller;

import com.aseubel.campushubbackend.common.ApiResponse;
import com.aseubel.campushubbackend.pojo.dto.user.UserRequest;
import com.aseubel.campushubbackend.pojo.dto.user.UserResponse;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/9/17 上午12:47
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ApiResponse<List<UserResponse>> getAllUser() {
        try {
            List<User> users = userService.list();
            List<UserResponse> userResponses = UserResponse.ofList(users);
            return ApiResponse.success(userResponses);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.getById(id);
            UserResponse userResponse = UserResponse.of(user);
            return ApiResponse.success(userResponse);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("")
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest request) {
        try {
            User user = request.toEntity();
            if (!userService.save(user)) {
                return ApiResponse.error("创建失败");
            }
            UserResponse userResponse = UserResponse.of(user);
            return ApiResponse.success(userResponse);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest request) {
        try {
            User user = request.toEntity();
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            if (!userService.update(user, updateWrapper)) {
                return ApiResponse.error("更新失败");
            }
            UserResponse userResponse = UserResponse.of(user);
            return ApiResponse.success(userResponse);
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.removeById(id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            log.error("操作失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

}
