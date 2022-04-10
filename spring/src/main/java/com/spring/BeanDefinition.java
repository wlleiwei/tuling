package com.spring;

/**
 * @author 魏磊
 */
public class BeanDefinition {
    /**
     * 对象的class对象
     */
    private Class classType;
    /**
     * bean对象时单例还是多例
     */
    private ScopeEnum scope;

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public ScopeEnum getScope() {
        return scope;
    }

    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }

    public BeanDefinition(Class classType, ScopeEnum scope) {
        this.classType = classType;
        this.scope = scope;
    }

    public BeanDefinition() {
    }
}
