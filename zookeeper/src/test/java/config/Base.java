package config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Base {

    private static final String CONNECTION_STRING = "192.168.0.101:2181";
    private static final int SESSION_TIMEOUT = 30 * 1000;//30S
    private static ZooKeeper zooKeeper = null;
    //用于连接到zookeeper后通知主线程
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void before() {
        try {
            log.info("开始准备连接到zookeeper...");
            zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, event -> {
                if (Watcher.Event.EventType.None == event.getType()
                        && Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                    log.info("zookeeper已经连接成功！");
                    countDownLatch.countDown();
                }
            });
        } catch (IOException e) {
            log.error("连接到zookeeper时发生异常：{}", e);
        }
    }

    @After
    public void after() {
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ZooKeeper getZooKeeper(){
        return zooKeeper;
    }
}
