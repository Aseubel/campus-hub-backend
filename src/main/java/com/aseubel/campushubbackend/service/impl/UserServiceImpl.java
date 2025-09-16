package com.aseubel.campushubbackend.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.aliyuncs.ram.model.v20150501.ChangePasswordRequest;
import com.aseubel.campushubbackend.common.exception.BusinessException;
import com.aseubel.campushubbackend.common.util.JwtUtil;
import com.aseubel.campushubbackend.mapper.UserMapper;
import com.aseubel.campushubbackend.pojo.dto.auth.LoginRequest;
import com.aseubel.campushubbackend.pojo.dto.auth.LoginResponse;
import com.aseubel.campushubbackend.pojo.dto.auth.MobileLoginRequest;
import com.aseubel.campushubbackend.pojo.dto.auth.RegisterRequest;
import com.aseubel.campushubbackend.pojo.dto.user.UserUpdateRequest;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.redis.IRedisService;
import com.aseubel.campushubbackend.redis.KeyBuilder;
import com.aseubel.campushubbackend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.aseubel.campushubbackend.common.Constant.SMS_CODE_EXPIRE_MINUTES;

/**
 * @author Aseubel
 * @date 2025/9/16 上午10:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final IRedisService redisService;
    private final ThreadPoolTaskExecutor threadPoolExecutor;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Value("${app.password}")
    private String defaultPassword;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPhone, request.getLoginId())
                .or()
                .eq(User::getName, request.getLoginId());
        // 查找用户
        User user = Optional.ofNullable(userMapper.selectOne(lambdaQueryWrapper))
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        // 验证密码
        if (!BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("用户名或密码错误");
        }

        return generateTokenResponse(user);
    }

    @Override
    @Transactional
    public LoginResponse mobileLogin(MobileLoginRequest request) {
        // 验证短信验证码
        if (!verifySmsCode(request.getPhone(), request.getSmsCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 查找或创建用户
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPhone, request.getPhone());
        User user = Optional.ofNullable(userMapper.selectOne(lambdaQueryWrapper))
                .orElseThrow(() -> new BusinessException("用户不存在，请先注册"));

        return generateTokenResponse(user);
    }
    @Override
    public List<User> getUserList(int page, int size, String keyword) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, keyword);
        lambdaQueryWrapper.orderByDesc(User::getCreatedAt);
        IPage<User> userIPage = userMapper.selectPage(pageParam, lambdaQueryWrapper);
        return userIPage.getRecords();
    }

    @Override
    public void toggleUserStatus(Long userId) {

    }

    /**
     * 生成token响应
     */
    private LoginResponse generateTokenResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getName());

        // 将访问令牌存储到Redis
        String redisKey = KeyBuilder.userTokenKey(user.getId());
        redisService.setValue(redisKey, accessToken, accessTokenExpiration);

        return LoginResponse.fromUser(user, accessToken, refreshToken,
                String.valueOf(System.currentTimeMillis() + accessTokenExpiration),
                String.valueOf(System.currentTimeMillis() + refreshTokenExpiration));
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // 验证密码确认
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 检查用户名是否存在
        if (existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否存在（如果提供了手机号）
        if (StringUtils.hasText(request.getPhone())) {
            if (existsByMobile(request.getPhone())) {
                throw new RuntimeException("手机号已被注册");
            }
            // 验证短信验证码
            if (!verifySmsCode(request.getPhone(), request.getSmsCode())) {
                throw new RuntimeException("验证码错误或已过期");
            }
        }

        // 创建用户
        User user = User.builder()
                .name(request.getUsername())
                .passwordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .phone(request.getPhone())
                .build();

        userMapper.insert(user);
        return generateTokenResponse(user);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // 验证刷新令牌
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new RuntimeException("刷新令牌无效或已过期");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return generateTokenResponse(user);
    }

    @Override
    public void logout(Long userId) {
        // 从Redis中删除token
        String redisKey = KeyBuilder.userTokenKey(userId);
        redisService.remove(redisKey);
    }

    @Override
    public String sendSmsCode(String mobile) {
        // 生成6位数字验证码
        String code = RandomUtil.randomNumbers(6);

        // 存储到Redis，5分钟过期
        String redisKey = KeyBuilder.smsCodeKey(mobile);
        redisService.setValue(redisKey, code, Duration.ofMinutes(SMS_CODE_EXPIRE_MINUTES).toMillis());

        // 这里应该调用短信服务发送验证码，我们只是记录日志
        log.info("发送短信验证码到 {}: {}", mobile, code);
        return code;

        // TODO 实际项目中应该调用阿里云、腾讯云等短信服务
        // sendSmsViaSmsProvider(mobile, code, type);
    }

    @Override
    public boolean verifySmsCode(String mobile, String code) {
        String redisKey = KeyBuilder.smsCodeKey(mobile);
        String storedCode = redisService.getValue(redisKey);

        if (storedCode != null && storedCode.equals(code)) {
            // 验证成功后删除验证码
            redisService.remove(redisKey);
            return true;
        }
        return false;
    }

    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, username);
        return userMapper.selectCount(lambdaQueryWrapper) > 0;
    }

    public boolean existsByMobile(String mobile) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getPhone, mobile);
        return userMapper.selectCount(lambdaQueryWrapper) > 0;
    }

    @Override
    public void updateUserInfo(User user, UserUpdateRequest request) {

    }

    @Override
    public void changePassword(User user, ChangePasswordRequest request) {

    }
}
