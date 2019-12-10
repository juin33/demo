package com.example.demo.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * @author kejun
 * @date 2019/12/6 上午11:19
 */
public class Client implements Watcher, Closeable {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    ZooKeeper zk;
    String hostPort;
    volatile boolean connected = false;
    volatile boolean expired = false;

    Client(String hostPort) {
        this.hostPort = hostPort;
    }

    void startClient() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    private String queueCommand(String command) {
        while (true) {
            try {
                String name = zk.create("/tasks/task-", command.getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
                return name;
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void process(WatchedEvent e) {
        System.out.println(e);
        if (e.getType() == Event.EventType.None) {
            switch (e.getState()) {
                case SyncConnected:
                    connected = true;
                    break;
                case Disconnected:
                    connected = false;
                    break;
                case Expired:
                    expired = true;
                    connected = false;
                    System.out.println("Exiting due to session expiration");
                default:
                    break;
            }
        }
    }

    /**
     * Check if this client is connected.
     *
     * @return
     */
    boolean isConnected() {
        return connected;
    }

    /**
     * Check if the ZooKeeper session is expired.
     *
     * @return
     */
    boolean isExpired() {
        return expired;
    }

    void submitTask(String task, TaskObject taskCtx) {
        taskCtx.setTask(task);
        zk.create("/tasks/task-",
                task.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL,
                createTaskCallback,
                taskCtx);
    }

    AsyncCallback.StringCallback createTaskCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                /*
                 * Handling connection loss for a sequential node is a bit
                 * delicate. Executing the ZooKeeper create command again
                 * might lead to duplicate tasks. For now, let's assume
                 * that it is ok to create a duplicate task.
                 */
                    submitTask(((TaskObject) ctx).getTask(), (TaskObject) ctx);

                    break;
                case OK:
                    logger.info("My created task name: " + name);
                    ((TaskObject) ctx).setTaskName(name);
                    watchStatus(name.replace("/tasks/", "/status/"), ctx);

                    break;
                default:
                    logger.error("Something went wrong" + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


    protected ConcurrentHashMap<String, Object> ctxMap = new ConcurrentHashMap<String, Object>();

    void watchStatus(String path, Object ctx) {
        ctxMap.put(path, ctx);
        zk.exists(path,
                statusWatcher,
                existsCallback,
                ctx);
    }

    Watcher statusWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            if (e.getType() == Event.EventType.NodeCreated) {
                assert e.getPath().contains("/status/task-");
                assert ctxMap.containsKey(e.getPath());

                zk.getData(e.getPath(),
                        false,
                        getDataCallback,
                        ctxMap.get(e.getPath()));
            }
        }
    };

    AsyncCallback.StatCallback existsCallback = new AsyncCallback.StatCallback() {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    watchStatus(path, ctx);

                    break;
                case OK:
                    if (stat != null) {
                        zk.getData(path, false, getDataCallback, ctx);
                        logger.info("Status node is there: " + path);
                    }

                    break;
                case NONODE:
                    break;
                default:
                    logger.error("Something went wrong when " +
                            "checking if the status node exists: " +
                            KeeperException.create(KeeperException.Code.get(rc), path));

                    break;
            }
        }
    };

    AsyncCallback.DataCallback getDataCallback = new AsyncCallback.DataCallback() {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                /*
                 * Try again.
                 */
                    zk.getData(path, false, getDataCallback, ctxMap.get(path));
                    return;
                case OK:
                /*
                 *  Print result
                 */
                    String taskResult = new String(data);
                    logger.info("Task " + path + ", " + taskResult);

                /*
                 *  Setting the status of the task
                 */
                    assert (ctx != null);
                    ((TaskObject) ctx).setStatus(taskResult.contains("done"));

                /*
                 *  Delete status znode
                 */
                    //zk.delete("/tasks/" + path.replace("/status/", ""), -1, taskDeleteCallback, null);
                    zk.delete(path, -1, taskDeleteCallback, null);
                    ctxMap.remove(path);
                    break;
                case NONODE:
                    logger.warn("Status node is gone!");
                    return;
                default:
                    logger.error("Something went wrong here, " +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    AsyncCallback.VoidCallback taskDeleteCallback = new AsyncCallback.VoidCallback() {
        public void processResult(int rc, String path, Object ctx) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    zk.delete(path, -1, taskDeleteCallback, null);
                    break;
                case OK:
                    logger.info("Successfully deleted " + path);
                    break;
                default:
                    logger.error("Something went wrong here, " +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


    static class TaskObject {
        private String task;
        private String taskName;
        private boolean done = false;
        private boolean succesful = false;
        private CountDownLatch latch = new CountDownLatch(1);

        String getTask() {
            return task;
        }

        void setTask(String task) {
            this.task = task;
        }

        void setTaskName(String name) {
            this.taskName = name;
        }

        String getTaskName() {
            return taskName;
        }

        void setStatus(boolean status) {
            succesful = status;
            done = true;
            latch.countDown();
        }

        void waitUntilDone() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.warn("InterruptedException while waiting for task to get done");
            }
        }

        synchronized boolean isDone() {
            return done;
        }

        synchronized boolean isSuccesful() {
            return succesful;
        }

    }

    @Override
    public void close()
            throws IOException {
        logger.info("Closing");
        try {
            zk.close();
        } catch (InterruptedException e) {
            logger.warn("ZooKeeper interrupted while closing");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client c = new Client("127.0.0.1:2181");
        c.startClient();

        while(!c.isConnected()){
            Thread.sleep(100);
        }

        TaskObject task1 = new TaskObject();
        TaskObject task2 = new TaskObject();

        c.submitTask("Sample task", task1);
        c.submitTask("Another sample task", task2);

        task1.waitUntilDone();
        task2.waitUntilDone();
    }
}
