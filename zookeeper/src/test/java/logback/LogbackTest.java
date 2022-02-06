package logback;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogbackTest {

    private final static Logger log = LoggerFactory.getLogger(LogbackTest.class);
    private final static Logger log1 = LoggerFactory.getLogger("test");
    @Test
    public void testLogbackPrint() {
        log.info("test logback print!");
        log1.info("test log1" +
                "");
    }
    @Test
    public void testClassLoader(){
        System.out.println(LogbackTest.class.getResource(""));
        System.out.println(LogbackTest.class.getResource("/"));
        System.out.println(LogbackTest.class.getClassLoader().getResource("org/slf4j/impl/StaticLoggerBinder.class"));
        System.out.println(LogbackTest.class.getClassLoader().getResource("/"));

    }

}
