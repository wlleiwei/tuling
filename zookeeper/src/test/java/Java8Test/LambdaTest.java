package Java8Test;

import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * 1.Consumer:accept(t)|消费 t
 * 2.Function:apply(t,r)|将 t 转化成 r
 * 3.Predicate:test(t)|对 t 进行判断并返回判断结果true：false
 * 4.Supplier:get(t)|提供 t
 */

public class LambdaTest {

    @Test
    public void test1() {
        Consumer<String> consumer = String::length;
        consumer.accept("hello lambda");
    }

    @Test
    public void test2() {
        consumer("consumer", System.out::println);
    }

    private void consumer(String s, Consumer<String> stringConsumer) {
        stringConsumer.accept(s);
    }

    @Test
    public void testPredict() {
        BiPredicate<String, String> result = String::equals;
        boolean a = result.test("demo", "demo");
        System.out.println(a);
    }
}
