package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ConfigCenter.java
 * @Author weilei
 * @Description 配置中心
 * @CreateTime 2021年08月24日 17:39
 */

@Slf4j
public class ConfigCenter {
    private final static String CONNECTION_STRING = "10.211.55.4:2181";
    private final static Integer SESSION_TIMEOUT = 30 * 1000;
    private static ZooKeeper zookeeper = null;
    private static String CONFIG_PATH = "/config";
    //用于连接zookeeper成功后同步通知client端
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        log.info("开始准备链接zookeeper...");
        //1.获取zookeeper实例，异步进行zookeeper的连接，event为连接之后出发的监听事件
        zookeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, event -> {
            //事件类型：None（连接成功）
            //时间状态：SyncConnected（异步连接）
            if (Watcher.Event.EventType.None == event.getType()
                    && Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                log.info("连接已经建立...");
                //连接成功后-1，用来唤醒main线程继续向下处理
                countDownLatch.countDown();
            }
        }
        );
        //注意：zookeeper的连接为异步连接，必须等连接成功后才可以进行对zookeeper的操作，否则可能会报连接缺失异常
        //等待连接成功后被唤醒
        countDownLatch.await();
        //2.新增配置
        MyConfig myConfig = new MyConfig();
        myConfig.setKey("key1");
        myConfig.setName("name1");

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data = objectMapper.writeValueAsBytes(myConfig);
        //3.将配置写入到zookeeper中
        String s = zookeeper.create(CONFIG_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        log.info("zookeeper create return:[{}]", s);
        //数据发生变化时及时通知
        Watcher watcher = new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                if (Event.EventType.NodeDataChanged == event.getType()
                        && CONFIG_PATH.equals(event.getPath())) {
                    byte[] data = zookeeper.getData(CONFIG_PATH, this, null);
                    MyConfig newConfig = objectMapper.readValue(data, MyConfig.class);
                    log.info("PATH[{}]发生了数据变化:{}", event.getPath(), newConfig);
                }
            }
        };

        //4.获取zookeeper数据
        byte[] key_data = zookeeper.getData(CONFIG_PATH, watcher, null);
        MyConfig originConfig = objectMapper.readValue(key_data, MyConfig.class);
        log.info("原始数据为：{}", originConfig);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
