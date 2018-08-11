/**
 * 
 */
package com.cli.security.app.authentication;

import com.cli.security.app.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhailiang
 *
 */
@Slf4j
@Component
public class CliFailureHandler extends SimpleUrlAuthenticationFailureHandler {		//security提供的失败处理器
	
	@Autowired
	private ObjectMapper objectMapper;		//security提供的Json转化器

	@Autowired
	private SecurityProperties securityProperties;		//自定义的配置抓取器，调用项目可以重写该配置

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		log.info("登录失败");

		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(exception.getMessage()));
	}
}
