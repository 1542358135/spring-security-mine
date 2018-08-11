package com.cli.security.app.valicode;

import com.cli.security.app.valicode.generator.ValiCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图片验证码
 * @author lc
 * @date 2018/6/13
 */
@RestController
public class ValiCodeController {

    @Autowired
    private ValiCodeGenerator imageCodeGenerator;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();     //spring Session工具类
    public static final String session_key = "SESSION_KEY";

    /**
     * 生成图片验证码并返回图片流
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = imageCodeGenerator.generate(new ServletWebRequest(request));
        sessionStrategy.setAttribute(new ServletWebRequest(request), session_key, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

}
