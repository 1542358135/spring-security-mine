package com.cli.security.app;

import com.cli.security.app.jwt.CliJwtTokenEnhancer;
import com.cli.security.app.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 根据配置cli.security.oauth2.tokenStore选择token的存储方式
 * redis，生成uuid存在本地redis，供后续验证
 * jwt，生成jwt token并返回给客户端，本地不做存储
 * @author lc
 */
@Configuration
public class TokenStoreConfig {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	/* 在配置中如果有cli.security.oauth2=redis则使用redis存uuid token */
	@ConditionalOnProperty(prefix = "cli.security.oauth2", name = "tokenStore", havingValue = "redis")
	public TokenStore redisTokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	@Configuration
	/* 在配置中如果有cli.security.oauth2=jwt则使用jwt token */
	@ConditionalOnProperty(prefix = "cli.security.oauth2", name = "tokenStore", havingValue = "jwt", matchIfMissing = true)
	public static class JwtConfig {

		private final SecurityProperties securityProperties;

		@Autowired
		public JwtConfig(SecurityProperties securityProperties) {
			this.securityProperties = securityProperties;
		}

		@Bean
		public TokenStore jwtTokenStore() {
			return new JwtTokenStore(jwtAccessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter(){
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
			return converter;
		}

		@Bean
		@ConditionalOnMissingBean(name = "jwtTokenEnhancer")
		public TokenEnhancer jwtTokenEnhancer(){
			return new CliJwtTokenEnhancer();
		}
	}

}
