package com.test.service;

import com.spring.Autowired;
import com.spring.Component;

/**
 * @author 魏磊
 */
@Component
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println("test custom spring");
    }
}
