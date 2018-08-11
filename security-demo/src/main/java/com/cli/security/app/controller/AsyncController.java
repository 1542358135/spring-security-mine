package com.cli.security.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * @author lc
 * @date 2018/6/13
 */
@RestController
public class AsyncController {
    private Logger logger = LoggerFactory.getLogger(AsyncController.class);

    @PostMapping("/order")
    public Callable<String> order() throws Exception{
        logger.info("start");
        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "success";
        };
        logger.info("end");
        return callable;
    }
}
