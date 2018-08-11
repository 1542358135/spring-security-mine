/**
 * 
 */
package com.cli.security.app.properties;

/**
 * @author zhailiang
 *
 */
public interface SecurityConstants {
	/**
	 * 当请求需要身份认证时，默认跳转的url
	 */
	public static final String DEFAULT_UNAUTHENTICATION_URL = "/require";
	/**
	 * 默认的用户名密码登录请求处理url
	 */
	public static final String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/login";
	/**
	 * 默认登录页面
	 */
	public static final String DEFAULT_LOGIN_PAGE_URL = "/imooc-signIn.html";
	/**
	 * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
	 */
	public static final String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
	/**
	 * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
	 */
	public static final String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";

}
