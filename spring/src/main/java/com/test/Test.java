package com.test;

import com.spring.ApplicationContext;
import com.test.config.AppConfig;

/**
 * <h1>测试手工模拟的spring</h1>
 * @author weilei
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext(AppConfig.class);
        Object userService = context.getBean("userService");
    }
}
