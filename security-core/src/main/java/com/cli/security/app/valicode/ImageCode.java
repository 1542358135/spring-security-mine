package com.cli.security.app.valicode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author lc
 * @date 2018/6/13
 */
public class ImageCode {
    private BufferedImage image;
    private String code;
    private LocalDateTime dateTime;

    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.dateTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
