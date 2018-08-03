package com.cli.security.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author lc
 * @date 2018/6/13
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //.formLogin().and()     //使用自带的表单页面登录
            //.httpBasic().and()      //使用自带的浮动框登录
            .authorizeRequests()
                .anyRequest().authenticated()  //所有请求都要身份验证
                .and()
            .csrf().disable();
    }
}
