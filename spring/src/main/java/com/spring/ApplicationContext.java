package com.spring;

/**
 * <h1>spring context</h1>
 *
 * @author weilei
 */
public class ApplicationContext {
    private Class configClass;

    public ApplicationContext() {
    }

    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
    }

    /**
     * <h2>从spring容器中获取bean对象</h2>
     *
     * @param beanName
     * @return 创建的bean对象
     */
    public Object getBean(String beanName) {
        return null;
    }

    public Object getBean(String beanName, Class classType) throws Exception {
        Object bean = getBean(beanName);
        if (classType.isInstance(bean)) {
            return classType.cast(bean);
        }else {
            throw new Exception("cannot cast "+bean+" to "+classType);
        }
    }
}
