package com.cli.security.app.properties;

/**
 * 浏览器端配置抓取（所有属性可在配置文件覆盖默认值）
 * @author lc
 * @date 2018/6/13
 */
public class BrowserProperties {
    /**
     * 指定默认登录成功返回Json还是页面
     */
    private LoginType loginType = LoginType.JSON;
    /**
     * 指定默认rememberMe Token的过期时间
     */
    private int rememberMeSeconds = 3600;

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
