package com.cli.security.app.valicode.impl;

import com.cli.security.app.valicode.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;


/**
 * 手机验证码的创建、发送、接收、校验
 * @author lc
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

	/**
	 * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
	 */
	@Autowired
	private Map<String, ValidateCodeGenerator> validateCodeGenerators;

	@Autowired
	private ValidateCodeRepository validateCodeRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imooc.security.core.validate.code.ValidateCodeProcessor#create(org.
	 * springframework.web.context.request.ServletWebRequest)
	 */
	@Override
	public void create(ServletWebRequest request) throws Exception {
		C validateCode = generate(request);
		save(request, validateCode);
		send(request, validateCode);
	}

	/**
	 * 生成校验码
	 * 
	 * @param request
	 * @return
	 */
	private C generate(ServletWebRequest request) {
		String type = getValidateCodeType(request).toString().toLowerCase();
		String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
		ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
		if (validateCodeGenerator == null) {
			throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
		}
		return (C) validateCodeGenerator.generate(request);
	}

	/**
	 * 保存校验码
	 * 
	 * @param request
	 * @param validateCode
	 */
	private void save(ServletWebRequest request, C validateCode) {
		ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
		validateCodeRepository.save(request, code, getValidateCodeType(request));
	}

	/**
	 * 构建验证码放入session时的key
	 * 
	 * @param request
	 * @return
	 */
	private String getSessionKey(ServletWebRequest request) {
		return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
	}

	/**
	 * 发送校验码，由子类实现
	 * 
	 * @param request
	 * @param validateCode
	 * @throws Exception
	 */
	protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

	/**
	 * 根据请求的url获取校验码的类型
	 * 
	 * @param request
	 * @return
	 */
	private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
		String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
		return ValidateCodeType.valueOf(type.toUpperCase());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(ServletWebRequest request) {

		ValidateCodeType processorType = getValidateCodeType(request);

		C codeInSession = (C) validateCodeRepository.get(request, processorType);

		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
					processorType.getParamNameOnValidate());
		} catch (ServletRequestBindingException e) {
			throw new ValidateCodeException("获取验证码的值失败");
		}

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException(processorType + "验证码的值不能为空");
		}

		if (codeInSession == null) {
			throw new ValidateCodeException(processorType + "验证码不存在");
		}

		if (codeInSession.isExpried()) {
			validateCodeRepository.remove(request, processorType);
			throw new ValidateCodeException(processorType + "验证码已过期");
		}

		if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException(processorType + "验证码不匹配");
		}

		validateCodeRepository.remove(request, processorType);
	}

}
