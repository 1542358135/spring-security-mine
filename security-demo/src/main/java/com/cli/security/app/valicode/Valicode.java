package com.cli.security.app.valicode;

import com.cli.security.app.valicode.generator.ValiCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 如果需要Demo重写Core项目中的默认图片生成器，则声明Bean名为imageCodeGenerator，实现ValiCodeGenerator即可
 * @author lc
 * @date 2018/6/13
 */
//@Component("imageCodeGenerator")
public class Valicode implements ValiCodeGenerator {
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("重写默认的验证码机制！");
        return null;
    }
}
