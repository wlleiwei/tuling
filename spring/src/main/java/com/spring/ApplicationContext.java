package com.spring;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>spring context</h1>
 *
 * @author weilei
 */
public class ApplicationContext {
    /**
     * 缓存所有的BeanDefinition
     * key:beanName
     * value:BeanDefinition
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    /**
     * 缓存所有的单例对象，在获取该bean时可以直接从缓存中获取，不用重新创建。
     * key:beanName
     * value:singletonObject
     */
    private Map<String, Object> singletonMap = new HashMap<>();

    public ApplicationContext() {
    }

    public ApplicationContext(Class configClass) {


        //1.获取ComponentScan定义的扫描路径，获取所有的class
        scan(configClass);

        //2.初始化所有单例类
        initializeSingletonClass();
    }

    /**
     * <h2>
     * 根据配置类去扫描对应路径下的所有类
     * </h2>
     * <p>
     * 将Component标记的class缓存到beanDefinitionMap中，后续创建对象时可用到
     * </p>
     *
     * @param configClass
     */
    private void scan(Class configClass) {
        ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String packagePath = componentScan.path();
        System.out.println("scan package path is " + packagePath);
        //保存扫描路径下所有的class类，直接把类加载到jvm中才可以判断类是否存在Component注解
        List<Class> classList = new ArrayList<>();
        ClassLoader loader = ApplicationContext.class.getClassLoader();
        //需要将com.test转化成com/test
        packagePath = packagePath.replace(".", File.separator);
        //通过类加载器来获取packagePath的绝对路径
        URL resource = loader.getResource(packagePath);
        String targetPath = resource.getPath();
        getBeanClasses(targetPath, classList, loader);
        //处理Component标记的class
        for (Class aClass : classList) {
            //将扫描路径下Component标注的类，缓存到beanDefinitionMap中，可根据beanDefinitionMap来创建对象的bean对象
            if (aClass.isAnnotationPresent(Component.class)) {
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setClassType(aClass);
                Component componentAnnotation = (Component) aClass.getAnnotation(Component.class);
                String beanName = componentAnnotation.value();
                //如果没有标注bean name，则生成默认的bean name
                if (beanName == null || "".equals(beanName)) {
                    beanName = getDefaultBeanName(aClass);
                }
                if (aClass.isAnnotationPresent(Scope.class)) {
                    Scope scopeAnnotation = (Scope) aClass.getAnnotation(Scope.class);
                    String scope = scopeAnnotation.value();
                    if (scope.equals(ScopeEnum.singleton.getValue())) {
                        beanDefinition.setScope(ScopeEnum.singleton);
                    } else {
                        beanDefinition.setScope(ScopeEnum.prototype);
                    }
                } else {
                    beanDefinition.setScope(ScopeEnum.singleton);
                }
                beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }

    /**
     * <h2>初始化所有单例对象</h2>
     */
    private void initializeSingletonClass() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            String beanName = entry.getKey();
            ScopeEnum scopeEnum = beanDefinition.getScope();
            if (scopeEnum.getValue().equals(ScopeEnum.singleton.getValue())) {
                Object object = doCreateBean(beanName, beanDefinition);
                //放入到单例池中
                singletonMap.put(beanName, object);
            }
        }

    }

    /**
     * <h2>从spring容器中获取bean对象</h2>
     *
     * @param beanName
     * @return 创建的bean对象
     */
    public Object getBean(String beanName) {
        if (singletonMap.containsKey(beanName)) {
            return singletonMap.get(beanName);
        } else {
            return doCreateBean(beanName, beanDefinitionMap.get(beanName));
        }
    }

    /**
     * <h2>
     * 根据 beanName 和 beanDefinition 来创建 bean 对象
     * </h2>
     * <p>
     * 1.调用类构造器来创建对象；
     * 2.属性赋值，依赖注入，问题：在进行依赖注入时若单例池中的所有对象未初始化，在获取构造器参数时可能返回为空，导致注入失败，需要对此类情况单独处理
     * </p>
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getClassType();
        try {
            Constructor declaredConstructor = beanClass.getDeclaredConstructor();
            Object newInstance = declaredConstructor.newInstance();

            //对newInstance对象进行属性赋值,依赖注入
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    //获取属性名称
                    String name = field.getName();
                    field.setAccessible(true);

                    //单例 or 多例，对多例进行单独处理
                    if (field.isAnnotationPresent(Scope.class)) {
                        Scope scope = field.getAnnotation(Scope.class);
                        String value = scope.value();
                        if (ScopeEnum.prototype.getValue().equals(value)) {
                            field.set(newInstance, getBean(name));
                        }
                    }

                    //处理单例
                    Class<?> fieldType = field.getType();
                    //依赖注入对象
                    Object object = getSingletonBean(fieldType, name);
                    field.set(newInstance, object);
                }
            }
            return newInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getBean(String beanName, Class classType) {
        Object bean = getBean(beanName);
        if (classType.isInstance(bean)) {
            return classType.cast(bean);
        } else {
            try {
                throw new Exception("cannot cast " + bean + " to " + classType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * <h2>根据ComponentScan定义的扫描路径，来获取该路径下所有Component注解标注的类</h2>
     * <p>需要注意的是：packagePath是相对路径，可以通过类加载器来获取classpath（D:\idea-workspace\tuling\spring\target\classes）下的类</p>
     *
     * @param packagePath
     * @return classList
     */
    private void getBeanClasses(String packagePath, List list, ClassLoader loader) {
        File file = new File(packagePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    getBeanClasses(f.getAbsolutePath(), list, loader);
                } else {
                    //通过类加载器来加载具体的class
                    String fileName = f.getAbsolutePath();
                    System.out.println(fileName);
                    if (fileName.endsWith(".class")) {
                        fileName = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        fileName = fileName.replace(File.separator, ".");
                        try {
                            Class<?> loadClass = loader.loadClass(fileName);
                            list.add(loadClass);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }


    /**
     * <h2>生成默认的bean name，类名首字母小写</h2>
     *
     * @param c
     * @return
     */
    private String getDefaultBeanName(Class c) {
        String className = c.getSimpleName();
        char[] chars = className.toCharArray();
        if (chars[0] >= 65 && chars[0] <= 90) {
            chars[0] = (char) (chars[0] + 32);
        }
        return new String(chars);
    }


    /**
     * <h2>从spring 单例池中获取对象</h2>
     * <p>
     * 根据属性名称来获取bean对象.spring先根据属性类型来获取bean对象，如果存在多个bean对象，在根据beanName来获取
     * </p>
     *
     * @param beanType
     * @return
     */
    private Object getSingletonBean(Class beanType, String beanName) {
        List<Object> list = new ArrayList<>();
        for (Map.Entry entry : singletonMap.entrySet()) {
            Object bean = entry.getValue();
            if (beanType.isInstance(bean)) {
                list.add(bean);
            }
        }
        int length = list.size();
        //list为空表明所需要的类还没有被加载到单例池中，为了避免依赖注入失败，需要创建此bean
        if (length == 0) {
            //如果该类上没有Component注解不予处理
            if (beanType.isAnnotationPresent(Component.class)) {
                return doCreateBean(beanName, beanDefinitionMap.get(beanName));
            }
        } else if (length == 1) {
            return list.get(0);
        } else {
            //存在多个则按照beanName查找
            return getBean(beanName);
        }
        return null;
    }
}
