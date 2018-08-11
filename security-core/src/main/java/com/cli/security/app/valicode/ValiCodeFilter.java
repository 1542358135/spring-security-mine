package com.cli.security.app.valicode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lc
 * @date 2018/6/13
 */
@Component("valiCodeFilter")
public class ValiCodeFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();     //spring Session工具类

    private static final Logger logger = LoggerFactory.getLogger(ValiCodeFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/login".equals(request.getRequestURI())){
            try {
                validate(new ServletWebRequest(request));           //校验验证码是否匹配
                logger.info("验证码验证成功！");
            }catch (ValiCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(ServletWebRequest request) {
        ImageCode code = (ImageCode) sessionStrategy.getAttribute(request, ValiCodeController.session_key);
        String enterCode;
        try {
            enterCode = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
        } catch (ServletRequestBindingException e) {
            throw new ValiCodeException("获取验证码的值失败");
        }
        //todo 还有其他验证，省略

        if(!code.getCode().equals(enterCode)){
            throw new ValiCodeException("验证码有误！");
        }

        sessionStrategy.removeAttribute(request, ValiCodeController.session_key);
    }

    private class ValiCodeException extends AuthenticationException {
        ValiCodeException(String msg){
            super(msg);
        }
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
