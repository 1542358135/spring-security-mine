/**
 * 
 */
package com.cli.security.app.properties.oauth2;

/**
 * @author zhailiang
 *
 */
public class OAuth2ClientProperties {
	
	private String clientId;
	
	private String clientSecret;
	
	private int accessTokenValidateSeconds = 7200;		//默认7200秒

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public int getAccessTokenValidateSeconds() {
		return accessTokenValidateSeconds;
	}

	public void setAccessTokenValidateSeconds(int accessTokenValidateSeconds) {
		this.accessTokenValidateSeconds = accessTokenValidateSeconds;
	}
	
}
