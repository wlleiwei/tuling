package logback.appenders;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * @ClassName MyAppender.java
 * @Author weilei
 * @Description 自己实现一个logback的appender
 * @CreateTime 2022年02月08日 16:29
 */
public class MyAppender extends AppenderBase<LoggingEvent> {
    @Override
    protected void append(LoggingEvent eventObject) {
        System.out.println("test my appender");
    }
}
