package com.example.demo.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * @author kejun
 * @date 2019/12/6 上午10:07
 */
public class Worker implements Watcher, Closeable {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);

    ZooKeeper zk;
    private String hostport;
    String name;
    String status;
    private ThreadPoolExecutor executor;
    private String serverId = Integer.toHexString((new Random()).nextInt());
    private volatile boolean connected = false;
    private volatile boolean expired = false;

    public Worker(String hostPort) {
        this.hostport = hostPort;
        this.executor = new ThreadPoolExecutor(1, 1,
                1000L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(200));
    }

    void startZk() throws IOException {
        zk = new ZooKeeper(hostport, 15000, this);
    }

    @Override
    public void process(WatchedEvent e) {
        logger.info(e.toString() + ", " + hostport);
        if (e.getType() == Event.EventType.None) {
            switch (e.getState()) {
                case SyncConnected:
                /*
                 * Registered with ZooKeeper
                 */
                    connected = true;
                    break;
                case Disconnected:
                    connected = false;
                    break;
                case Expired:
                    expired = true;
                    connected = false;
                    logger.error("Session expired");
                default:
                    break;
            }
        }
    }

    /**
     * Checks if this client is connected.
     *
     * @return boolean
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Checks if ZooKeeper session is expired.
     *
     * @return
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Bootstrapping here is just creating a /assign parent
     * znode to hold the tasks assigned to this worker.
     */
    public void bootstrap() {
        createAssignNode();
    }

    void createAssignNode() {
        zk.create("/assign/worker-" + serverId, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
                createAssignCallback, null);
    }

    AsyncCallback.StringCallback createAssignCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                /*
                 * Try again. Note that registering again is not a problem.
                 * If the znode has already been created, then we get a
                 * NODEEXISTS event back.
                 */
                    createAssignNode();
                    break;
                case OK:
                    logger.info("Assign node created");
                    break;
                case NODEEXISTS:
                    logger.warn("Assign node already registered");
                    break;
                default:
                    logger.error("Something went wrong: " + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


    void register() {
        zk.create("/workers/worker-" + serverId, "Idle".getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback, null);
    }

    AsyncCallback.StringCallback createWorkerCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    logger.info("succeess to create worker node,serverId={}", serverId);
                    break;
                case NODEEXISTS:
                    logger.info("someone has already create serverId={}", serverId);
                    break;
                default:
                    logger.error("something error,rc={}", rc);
            }
        }
    };

    AsyncCallback.StatCallback statusUpdateCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    updateStatus((String) ctx);
                    break;
            }
        }
    };

    synchronized private void updateStatus(String status) {
        if (Objects.equals(status, this.status)) {
            zk.setData("/workers/" + name, status.getBytes(), -1, statusUpdateCallback, status);
        }
    }


    public void setStatus(String status) {
        this.status = status;
        updateStatus(status);
    }

    Watcher newTaskWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            if (e.getType() == Event.EventType.NodeChildrenChanged) {
                assert new String("/assign/worker-" + serverId).equals(e.getPath());

                getTasks();
            }
        }
    };

    void getTasks() {
        zk.getChildren("/assign/worker-" + serverId,
                newTaskWatcher,
                tasksGetChildrenCallback,
                null);
    }

    protected ChildrenCache assignedTasksCache = new ChildrenCache();

    AsyncCallback.ChildrenCallback tasksGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    if (children != null) {
                        executor.execute(new Runnable() {
                            List<String> children;
                            DataCallback cb;

                            /*
                             * Initializes input of anonymous class
                             */
                            public Runnable init(List<String> children, DataCallback cb) {
                                this.children = children;
                                this.cb = cb;

                                return this;
                            }

                            public void run() {
                                if (children == null) {
                                    return;
                                }

                                logger.info("Looping into tasks");
                                setStatus("Working");
                                for (String task : children) {
                                    logger.trace("New task: {}", task);
                                    zk.getData("/assign/worker-" + serverId + "/" + task,
                                            false,
                                            cb,
                                            task);
                                }
                            }
                        }.init(assignedTasksCache.addedAndSet(children), taskDataCallback));
                    }
                    break;
                default:
                    System.out.println("getChildren failed: " + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    AsyncCallback.DataCallback taskDataCallback = new AsyncCallback.DataCallback() {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    zk.getData(path, false, taskDataCallback, null);
                    break;
                case OK:
                /*
                 *  Executing a task in this example is simply printing out
                 *  some string representing the task.
                 */
                    executor.execute(new Runnable() {
                        byte[] data;
                        Object ctx;

                        /*
                         * Initializes the variables this anonymous class needs
                         */
                        public Runnable init(byte[] data, Object ctx) {
                            this.data = data;
                            this.ctx = ctx;

                            return this;
                        }

                        public void run() {
                            logger.info("Executing your task: " + new String(data));
                            zk.create("/status/" + (String) ctx, "done".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                                    CreateMode.PERSISTENT, taskStatusCreateCallback, null);
                            zk.delete("/assign/worker-" + serverId + "/" + (String) ctx,
                                    -1, taskVoidCallback, null);
                        }
                    }.init(data, ctx));

                    break;
                default:
                    logger.error("Failed to get task data: ", KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    AsyncCallback.StringCallback taskStatusCreateCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    zk.create(path + "/status", "done".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
                            taskStatusCreateCallback, null);
                    break;
                case OK:
                    logger.info("Created status znode correctly: " + name);
                    break;
                case NODEEXISTS:
                    logger.warn("Node exists: " + path);
                    break;
                default:
                    logger.error("Failed to create task data: ", KeeperException.create(KeeperException.Code.get(rc), path));
            }

        }
    };

    AsyncCallback.VoidCallback taskVoidCallback = new AsyncCallback.VoidCallback() {
        public void processResult(int rc, String path, Object rtx) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    break;
                case OK:
                    logger.info("Task correctly deleted: " + path);
                    break;
                default:
                    logger.error("Failed to delete task data" + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    /**
     * Closes the ZooKeeper session.
     */
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

    public static void main(String args[]) throws InterruptedException, IOException {
        Worker w = new Worker("127.0.0.1:2181");
        w.startZk();

        while(!w.isConnected()){
            Thread.sleep(100);
        }
        /*
         * bootstrap() create some necessary znodes.
         */
        w.bootstrap();

        /*
         * Registers this worker so that the leader knows that
         * it is here.
         */
        w.register();

        /*
         * Getting assigned tasks.
         */
        w.getTasks();

        while(!w.isExpired()){
            Thread.sleep(1000);
        }
    }
}
