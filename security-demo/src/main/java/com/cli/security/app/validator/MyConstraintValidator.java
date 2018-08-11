package com.cli.security.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *  可以注入服务，验证或做其他事，最后返回校验结果
 * @author lc
 * @date 2018/6/13
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object>{    //指定自定义注解类和被验证的字段类型

    @Override
    public void initialize(MyConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        System.out.println(value);
        return false;
    }
}
