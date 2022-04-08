package logback;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LogbackTest {

    private final static Logger log = LoggerFactory.getLogger(LogbackTest.class);

    @Test
    public void testLogbackPrint() {
        String TEST = "TEST";
        log.info("test logback print! {}", TEST);

    }

    @Test
    public void testClassLoader() {
        System.out.println(LogbackTest.class.getResource(""));
        System.out.println(LogbackTest.class.getResource("/"));
        System.out.println(LogbackTest.class.getClassLoader().getResource("org/slf4j/impl/StaticLoggerBinder.class"));
        System.out.println(LogbackTest.class.getClassLoader().getResource("/"));

    }

    private  static volatile Integer count = 0;

    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() ->{
            add();
        });
        Thread t2 = new Thread(() ->{
            add();
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(count);
    }

    private void add() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
        Map map = new HashMap();
        List list = new ArrayList();
    }
}
