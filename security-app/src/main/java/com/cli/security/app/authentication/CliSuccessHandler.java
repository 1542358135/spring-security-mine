package com.cli.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * 设置验证成功后的请求
 * @author lc
 */
@Component
@Slf4j
public class CliSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {		//security提供的成功处理器

	@Autowired
	private ObjectMapper objectMapper;		//security提供的Json转化器

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;

	/**
	 * 生成token并返回
	 * @param request request
	 * @param response response
	 * @param authentication 身份信息
	 * @throws IOException IOException
	 * @throws ServletException ServletException
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		/* 解析请求头 */
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Basic ")) {
			throw new UnapprovedClientAuthenticationException("请求头无client信息！");
		}

		String[] tokens = extractAndDecodeHeader(header);
		assert tokens.length == 2;

		/* 解析后的clientId 和 clientSecret */
		String clientId = tokens[0];
		String clientSecret = tokens[1];

		/* 获取ClientDetails */
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

		/* 获取client信息校验 */
		if(clientDetails == null){
			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在！");
		}else if(!clientSecret.equals(clientDetails.getClientSecret())){
			throw new UnapprovedClientAuthenticationException("clientSecret不匹配！" + clientSecret);
		}

		/* 获取TokenRequest */
		TokenRequest tokenRequest = new TokenRequest(new HashMap<>(0), clientId, clientDetails.getScope(), "custom");

		/* 获取Token */
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
		OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(token));
	}

	/**
	 * 请求头解码clientId、clientSecret的方法
	 * @param header header
	 * @return 解析后的数据
	 * @throws IOException IOException
	 */
	private String[] extractAndDecodeHeader(String header)
			throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		}
		catch (IllegalArgumentException e) {
			throw new BadCredentialsException(
					"Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}
}
