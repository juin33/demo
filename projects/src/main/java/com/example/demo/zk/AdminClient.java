package com.example.demo.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * @author kejun
 * @date 2019/12/9 上午9:57
 */
public class AdminClient implements Watcher {
    private static final Logger logger = LoggerFactory.getLogger(AdminClient.class);

    ZooKeeper zk;
    String hostPort;

    AdminClient(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZk() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    void listStat(){
        try {
            Stat stat = new Stat();
            byte masterData[] = zk.getData("/master",false,stat);
            Date startDate = new Date(stat.getCtime());
            logger.info("Master - {},since-{}",new String(masterData),startDate);
            for(String worker : zk.getChildren("/workers",false)){
                byte workerData[] = zk.getData("/workers/"+worker,false,null);
                String state = new String(workerData);
                logger.info("Worker-{}-state:{}",worker,state);
                for(String task : zk.getChildren("/assign",false)){
                    logger.info("Task-{}",task);
                }
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.info("e:{}", watchedEvent);
    }

    public static void main(String[] args) throws IOException {
        AdminClient adminClient = new AdminClient("127.0.0.1:2181");
        adminClient.startZk();
        adminClient.listStat();
    }
}
