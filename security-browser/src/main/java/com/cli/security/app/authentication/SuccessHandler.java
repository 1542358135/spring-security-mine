/**
 * 
 */
package com.cli.security.app.authentication;

import com.cli.security.app.properties.LoginType;
import com.cli.security.app.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 设置验证成功后的请求
 */
@Component
public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {		//security提供的成功处理器

	private Logger logger = LoggerFactory.getLogger(SuccessHandler.class);

	@Autowired
	private ObjectMapper objectMapper;		//security提供的Json转化器

	@Autowired
	private SecurityProperties securityProperties;		//自定义的配置抓取器，调用项目可以重写该配置

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		logger.info("登录成功");

		if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {		//如果配置为JSON则返回Json
			response.setStatus(HttpStatus.OK.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(authentication));	//写入responseJson
		} else {																			//否则跳转页面
			super.onAuthenticationSuccess(request, response, authentication);				//父类默认为跳转，所以这样写
		}

	}

}
