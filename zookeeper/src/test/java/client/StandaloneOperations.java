package client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName StandaloneOperations.java
 * @Author weilei
 * @Description 单机版基础操作
 * @CreateTime 2021年08月26日 18:36
 */
@Slf4j
public class StandaloneOperations extends StandaloneBase {
    private static String nodePath = "/test-node";

    @Test
    public void testCreateNode() throws Exception {
        String s = getZookeeper().create(nodePath, "data".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        log.info("create node:[{}]", s);
    }

    @Test
    public void testGetData() {
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (Event.EventType.NodeDataChanged == event.getType()
                        && nodePath.equals(event.getPath())) {
                    try {
                        byte[] data = getZookeeper().getData(nodePath, this, null);
                        String s = new String(data);
                        log.info("[{}]节点发生了数据变化,新数据为：{}", nodePath, s);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        try {
            byte[] data = getZookeeper().getData(nodePath, watcher, null);
            String s = new String(data);
            log.info("path[{}],data[{}]", nodePath, s);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSet() {
        String s = "new data";
        try {
            Stat stat = new Stat();
            byte[] data = getZookeeper().getData(nodePath, false, stat);
            int version = stat.getVersion();
            getZookeeper().setData(nodePath, s.getBytes(StandardCharsets.UTF_8), version);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
