package com.cli.security.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 必须有三个属性，message失败返回信息
 * @author lc
 * @date 2018/6/13
 */
@Target({ElementType.METHOD,ElementType.FIELD})     //指定可以标注在方法和字段上
@Retention(RetentionPolicy.RUNTIME)     //指定为运行时
@Constraint(validatedBy = MyConstraintValidator.class)      //指定约束为自定义逻辑类
public @interface MyConstraint {
    String message();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
