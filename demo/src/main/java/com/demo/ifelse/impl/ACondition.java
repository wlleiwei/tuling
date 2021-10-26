package com.demo.ifelse.impl;

import com.demo.ifelse.Condition;
import org.springframework.stereotype.Service;

/**
 * @ClassName ACondition.java
 * @Author weilei
 * @Description TODO
 * @CreateTime 2021年09月01日 10:32
 */

@Service
public class ACondition implements Condition {

    @Override
    public boolean execute(String s) {
        System.out.println();
        return false;
    }
}
