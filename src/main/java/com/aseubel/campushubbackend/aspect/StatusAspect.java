package com.aseubel.campushubbackend.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.aseubel.campushubbackend.common.annotation.constraint.RequireLogin;
import com.aseubel.campushubbackend.common.annotation.constraint.StatusCheck;
import com.aseubel.campushubbackend.common.exception.BusinessException;
import com.aseubel.campushubbackend.context.UserContext;
import com.aseubel.campushubbackend.pojo.dto.auth.LoginResponse;
import com.aseubel.campushubbackend.pojo.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 状态校验切面
 *
 * @author Aseubel
 * @date 2025/9/18 下午3:42
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class StatusAspect {

    @Around("@annotation(statusCheck) || @within(statusCheck)")
    public Object checkUserStatus(ProceedingJoinPoint joinPoint, StatusCheck statusCheck) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        if (ObjectUtil.isEmpty(statusCheck)) {
//            statusCheck = method.getAnnotation(StatusCheck.class);
//        }
        User currentUser = UserContext.getCurrentUser();
        if (!ObjectUtil.isEmpty(currentUser)) {
            checkStatus(currentUser);
        }
        Object result = joinPoint.proceed();
        if (result instanceof LoginResponse response) {
            response.getUser().checkStatus();
        }
        return joinPoint.proceed();
    }

    private void checkStatus(User user) {
        if (user.getStatus() == User.StatusEnum.DISABLED) {
            throw new BusinessException("该用户已被禁用，请联系管理员");
        }
    }
}
