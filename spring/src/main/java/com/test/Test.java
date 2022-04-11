package com.test;

import com.spring.ApplicationContext;
import com.test.config.AppConfig;
import com.test.service.UserService;

/**
 * <h1>测试手工模拟的spring</h1>
 *
 * @author weilei
 */
public class Test {
    public static void main(String[] args) {
        //1.初始化ApplicationContext
        ApplicationContext context = new ApplicationContext(AppConfig.class);
        //2.从spring容器中获取对应的bean对象
        UserService userService = (UserService) context.getBean("userService");
        UserService userService1 = (UserService) context.getBean("userService");
        UserService userService2 = (UserService) context.getBean("userService");
        System.out.println(userService);
        System.out.println(userService1);
        System.out.println(userService2);
        userService.test();
    }
}
