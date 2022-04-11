package com.test.service;

import com.spring.Autowired;
import com.spring.Component;
import com.spring.Scope;
import com.spring.ScopeEnum;

/**
 * @author 魏磊
 */
@Component
@Scope("prototype")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println("test custom spring");
        orderService.test();
    }
}
