package com.aseubel.campushubbackend.common.annotation.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查状态注解
 *
 * @author Aseubel
 * @date 2025/9/18 下午3:47
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusCheck {
    String value() default "";
}
