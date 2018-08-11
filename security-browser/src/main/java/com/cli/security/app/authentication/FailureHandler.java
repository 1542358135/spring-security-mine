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
@Component
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {		//security提供的失败处理器

	private Logger logger = LoggerFactory.getLogger(FailureHandler.class);
	
	@Autowired
	private ObjectMapper objectMapper;		//security提供的Json转化器

	@Autowired
	private SecurityProperties securityProperties;		//自定义的配置抓取器，调用项目可以重写该配置

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		logger.info("登录失败");

		if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {				//如果配置为JSON则返回Json
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(exception.getMessage()));	//写入responseJson
		}else{																						//否则跳转页面
			super.onAuthenticationFailure(request, response, exception);							//父类默认为跳转，所以这样写
		}
	}
}
