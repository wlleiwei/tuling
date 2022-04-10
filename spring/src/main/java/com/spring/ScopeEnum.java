package com.spring;

/**
 * @author 魏磊
 */

public enum ScopeEnum {
    /**
     * 单例
     */
    singleton("singleton"),
    /**
     * 多例
     */
    prototype("prototype");

    String value;

    ScopeEnum(String scope) {

    }

    public String getValue() {
        return value;
    }
}
