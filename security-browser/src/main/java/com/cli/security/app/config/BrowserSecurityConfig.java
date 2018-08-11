package com.cli.security.app.config;

import com.cli.security.app.AbstractChannelSecurityConfig;
import com.cli.security.app.properties.SecurityProperties;
import com.cli.security.app.valicode.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * security核心配置类
 * @author lc
 * @date 2018/6/13
 */
@Configuration
@EnableWebSecurity
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;      //把配置文件的属性，变成实体，也是实现可扩展功能的核心
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        /*tokenRepository.setCreateTableOnStartup(true);*/
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.applyPasswordAuthenticationConfig(http);

        http.apply(validateCodeSecurityConfig)
                .and()
            .rememberMe()
                .tokenRepository(persistentTokenRepository())                                   //存储token
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())   //设置过期时间
                .userDetailsService(userDetailsService)                                         //使用userDetailsService实现登陆
                .and()
            .authorizeRequests()
                .antMatchers("/require").permitAll()                        //放行非法权限跳转的controller
                .antMatchers("/code/image").permitAll()                        //放行的图片验证码
                .anyRequest().authenticated()                               //所有请求都要身份验证
                .and()
            .csrf().disable();                                              //跨站伪造防护，关闭
    }

}
