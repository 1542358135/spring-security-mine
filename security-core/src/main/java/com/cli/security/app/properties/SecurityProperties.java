package com.cli.security.app.properties;

import com.cli.security.app.properties.oauth2.OAuth2Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置抓取父类：抓取所有配置文件中cli.security开头的配置
 * @author lc
 * @date 2018/6/13
 */
@Component
@ConfigurationProperties(prefix = "cli.security")
public class SecurityProperties {
    /**
     * 浏览器端相关属性
     */
    private BrowserProperties browser = new BrowserProperties() ;
    /**
     * 验证码相关属性
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();
    /**
     * 抓取OAuth2用户、密码、过期时间
     */
    private OAuth2Properties oauth2 = new OAuth2Properties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }

    public OAuth2Properties getOauth2() {
        return oauth2;
    }

    public void setOauth2(OAuth2Properties oauth2) {
        this.oauth2 = oauth2;
    }
}
