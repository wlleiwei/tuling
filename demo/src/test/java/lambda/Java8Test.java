package lambda;



import org.junit.Test;

import java.util.function.Consumer;

/**
 * @ClassName Java8Test.java
 * @Author weilei
 * @Description TODO
 * @CreateTime 2021年10月25日 18:39
 */
public class Java8Test {
    private static void test(DemoInterface demoInterface) {
        demoInterface.test();
    }

    @Test
    public void test1() {
        //传递实现类
        test(new DemoInterfaceImpl());
        //传递匿名类
        test(new DemoInterface() {
            @Override
            public void test() {
                System.out.println("传递匿名类");
            }
        });
        test(() -> {
            System.out.println("lambda表达式");
        });
        //只有一条语句时可以省略
        test(() -> System.out.println("简化lambda表达式"));
    }

    @Test
    public void test2() {
        String content = "java";
        consumer(content, (s) -> System.out.println("consumer " + s));
        doubleConsumer(content,
                (s1) -> System.out.println("double consumer1 " + s1),
                (s2) -> System.out.println("double consumer2 " + s2));
        threeConsumer(content,
                (s) -> System.out.println("three consumer1 " + content),
                (s) -> System.out.println("three consumer2 " + content),
                (s -> System.out.println("three consumer3 " + content)));

    }

    private static void consumer(String content, Consumer<String> consumer) {
        consumer.accept(content);
    }

    private static void doubleConsumer(String content, Consumer<String> consumer1, Consumer<String> consumer2) {
        consumer1.andThen(consumer2).accept(content);
    }

    private static void threeConsumer(String content,
                                      Consumer<String> consumer1,
                                      Consumer<String> consumer2,
                                      Consumer<String> consumer3) {
        consumer1.andThen(consumer2).andThen(consumer3).accept(content);
    }
}
