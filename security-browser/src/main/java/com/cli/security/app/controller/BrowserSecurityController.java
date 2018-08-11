package com.cli.security.app.controller;

import com.cli.security.app.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 非法登录跳转到该类
 * @author lc
 * @date 2018/6/13
 */
@RestController
public class BrowserSecurityController {

    @Autowired
    private SecurityProperties securityProperties;

    private static final Logger logger = LoggerFactory.getLogger(BrowserSecurityController.class);
    private RequestCache cache = new HttpSessionRequestCache();                                         //请求时request会存到RequestCache中
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();                          //提供跳转功能

    @RequestMapping("/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)      //返回401
    public String requireAnto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = cache.getRequest(request, response);
        if(savedRequest != null){
            String target = savedRequest.getRedirectUrl();
            logger.info("target:" + target);
            if(StringUtils.endsWithIgnoreCase(target, ".html")){
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        return "需要身份验证！";
    }
}
