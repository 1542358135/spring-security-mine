package com.cli.security.app;

import com.cli.security.app.properties.SecurityProperties;
import com.cli.security.app.properties.oauth2.OAuth2ClientProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器实现
 * @author lc
 * @date 2018/6/13
 */
@Configuration
/* 实现认证服务器 */
@EnableAuthorizationServer
public class CliAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;      //用户名密码验证bean

    @Autowired
    private SecurityProperties securityProperties;      //配置抓取bean

    @Autowired
    private TokenStore tokenStore;      //生成的token会生成在redis中

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;    //负责jwt的生成

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;            //负责jwt生成前属性添加

    @Autowired
    public CliAuthorizationServerConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .userDetailsService(userDetailsService);

        //如果jwt形式的bean不为空则使用jwt的生成器(jwtAccessTokenConverter)和jwt属性添加(tokenEnhancer)的bean
        if(null != jwtAccessTokenConverter && null != jwtTokenEnhancer){
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(enhancers);

            endpoints.tokenEnhancer(tokenEnhancerChain)             //用新定义的EnhancerChain
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    /* 设置token的基本属性clientId、secret、允许的授权方式、accessToken的超时和refreshToken的超时、scopes*/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
                builder.withClient(client.getClientId())
                        .secret(client.getClientSecret())
                        .authorizedGrantTypes("refresh_token")
                        .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
                        .refreshTokenValiditySeconds(2592000)
                        .scopes("all");
            }
        }
    }
}
