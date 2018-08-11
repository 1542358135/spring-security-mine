package com.cli.security.app;

import com.cli.security.app.authentication.CliFailureHandler;
import com.cli.security.app.authentication.CliSuccessHandler;
import com.cli.security.app.properties.SecurityConstants;
import com.cli.security.app.properties.SecurityProperties;
import com.cli.security.app.valicode.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源服务器的安全配置，相当于服务器端的核心入口类BrowserSecurityConfig
 * @author lc
 * @date 2018/6/13
 */
@Configuration
@EnableResourceServer
public class CliResourcesServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CliSuccessHandler cliSuccessHandler;
    @Autowired
    private CliFailureHandler cliFailureHandler;
    @Autowired
    private SecurityProperties securityProperties;      //把配置文件的属性，变成实体，也是实现可扩展功能的核心
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(cliSuccessHandler)
                .failureHandler(cliFailureHandler);
        http.authorizeRequests()
                .antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL).permitAll()                        //放行非法权限跳转的controller
                .antMatchers("/code/image").permitAll()                                                         //放行的图片验证码
                .anyRequest().authenticated()                                                                   //所有请求都要身份验证
                .and()
            .csrf()
                .disable();                                                                                     //跨站伪造防护，关闭
    }
}
