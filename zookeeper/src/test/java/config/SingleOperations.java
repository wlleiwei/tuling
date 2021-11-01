package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

@Slf4j
public class SingleOperations extends Base {
    private static final String nodePath = "/test";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreate() {
        ZooKeeper zooKeeper = getZooKeeper();
        MyConfig myConfig = MyConfig.builder().key("key").name("name").build();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            val bytes = objectMapper.writeValueAsBytes(myConfig);
            val s = zooKeeper.create(nodePath, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            log.info("创建节点成功：[{}]", s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDelete() {
        ZooKeeper zooKeeper = getZooKeeper();
        try {
            zooKeeper.delete(nodePath, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = getZooKeeper();
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (Watcher.Event.EventType.NodeDataChanged == watchedEvent.getType()
                        && nodePath.equals(watchedEvent.getPath())) {
                    try {
                        byte[] bytes = zooKeeper.getData(nodePath, this, null);
                        MyConfig myConfig = objectMapper.readValue(bytes, MyConfig.class);
                        log.info("[{}]节点数据发生了变化，新数据为：[{}]", nodePath, myConfig);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        byte[] data = zooKeeper.getData(nodePath, watcher, null);
        try {
            MyConfig myConfig = objectMapper.readValue(data, MyConfig.class);
            log.info("[{}]节点数据为：[{}]", nodePath, myConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        ZooKeeper zooKeeper = getZooKeeper();
        try {
            MyConfig myConfig = MyConfig.builder().key("key").name("name").build();
            byte[] bytes = objectMapper.writeValueAsBytes(myConfig);
            Stat stat = new Stat();
            zooKeeper.getData(nodePath, false, stat);
            zooKeeper.setData(nodePath, bytes, stat.getVersion());
        } catch (Exception e) {

        }
    }

    @Test
    public void testAsyncGet(){
        ZooKeeper zooKeeper = getZooKeeper();
        //zooKeeper.getData();
    }

}
