package com.cli.security.app.valicode.generator;

import com.cli.security.app.valicode.ImageCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author lc
 * @date 2018/6/13
 */
public interface ValiCodeGenerator {
    ImageCode generate(ServletWebRequest request);
}
