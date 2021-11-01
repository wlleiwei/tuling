package Java8Test;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class StreamTest {

    @Test
    public void test() {
        List<String> list = new LinkedList<>();
        list.add("java");
        list.add("python");
        list.add("c#");
        list.add("javascript");
        list.forEach(System.out::println);

        list.stream()
                .filter(s -> s.startsWith("java"))
                .filter(s -> s.length() > 4)
                .forEach(s -> System.out.println(s));
    }
}
