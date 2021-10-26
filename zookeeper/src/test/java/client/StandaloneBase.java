package client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName StandaloneBase.java
 * @Author weilei
 * @Description 用于zookeeper的连接
 * @CreateTime 2021年08月26日 18:27
 */
@Slf4j
public class StandaloneBase {
    private final static String CONNECTION_STRING = "10.211.55.4:2181";
    private final static Integer SESSION_TIMEOUT = 30 * 1000;
    private static ZooKeeper zookeeper = null;
    //用于连接zookeeper成功后同步通知client端
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 用于初始化zookeeper连接
     */
    @Before
    public void init() {
        log.info("开始连接zookeeper...");
        try {
            zookeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, event -> {
                if (Watcher.Event.EventType.None == event.getType()
                        && Watcher.Event.KeeperState.SyncConnected == event.getState()){
                    log.info("连接zookeeper成功！");
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不断开连接，用于观察数据修改后的监听事件
     */
    @After
    public void end(){
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ZooKeeper getZookeeper(){
        return zookeeper;
    }
}
