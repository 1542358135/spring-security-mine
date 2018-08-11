/**
 * 
 */
package com.cli.security.app.properties.oauth2;

/**
 * @author zhailiang
 *
 */
public class OAuth2Properties {

	private String jwtSigningKey = "dociee_cli";

	private OAuth2ClientProperties[] clients = {};

	public OAuth2ClientProperties[] getClients() {
		return clients;
	}

	public void setClients(OAuth2ClientProperties[] clients) {
		this.clients = clients;
	}

	public String getJwtSigningKey() {
		return jwtSigningKey;
	}

	public void setJwtSigningKey(String jwtSigningKey) {
		this.jwtSigningKey = jwtSigningKey;
	}
}
