package com.example.demo.zk;

import com.example.demo.zk.recovery.RecoveredAssignments;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.apache.zookeeper.KeeperException.Code.CONNECTIONLOSS;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * @author kejun
 * @date 2019/12/3 下午2:41
 */
public class Master implements Watcher, Closeable {

    private static final Logger logger = LoggerFactory.getLogger(Master.class);

    enum MasterStates {RUNNING, ELECTED, NOTELECTED}

    ;

    private volatile MasterStates state = MasterStates.RUNNING;

    MasterStates getState() {
        return state;
    }

    private Random random = new Random(this.hashCode());
    private ZooKeeper zk;
    private String hostPort;
    private String serverId = Integer.toHexString(random.nextInt());
    private volatile boolean connected = false;
    private volatile boolean expired = false;

    protected ChildrenCache tasksCache;
    protected ChildrenCache workersCache;

    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZk() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    void stopZk() throws InterruptedException {
        zk.close();
    }

    @Override
    public void process(WatchedEvent e) {
        logger.info("Processing event: " + e.toString());
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
                    logger.error("Session expiration");
                default:
                    break;
            }
        }
    }

    private void bootStrap() {
        createParent("/workers", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/status", new byte[0]);
    }

    private void createParent(String path, byte[] data) {
        zk.create(path, data, OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createParentCallback, data);
    }

    AsyncCallback.StringCallback createParentCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createParent(path, (byte[]) ctx);
                    break;
                case OK:
                    logger.info("Parent created");
                    break;
                case NODEEXISTS:
                    logger.warn("Parent already existed:{}", path);
                    break;
                default:
                    logger.error("Something error.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    /**
     * Check if this client is connected.
     *
     * @return boolean ZooKeeper client is connected
     */
    boolean isConnected() {
        return connected;
    }

    /**
     * Check if the ZooKeeper session has expired.
     *
     * @return boolean ZooKeeper session has expired
     */
    boolean isExpired() {
        return expired;
    }

    AsyncCallback.StringCallback masterCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            logger.info("---------------- async rc={}", rc);
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMasterAsync();
                    break;
                case OK:
                    state = MasterStates.ELECTED;
                    takeLeadership();
                    break;
                case NODEEXISTS:
                    state = MasterStates.NOTELECTED;
                    masterExists();
                    logger.error("node exists");
                default:
                    state = MasterStates.NOTELECTED;
                    logger.error("Something went wrong when running for master.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
            logger.info("I'm " + (state == MasterStates.ELECTED ? "" : "not ") + "the leader " + serverId);
        }
    };

    //异步
    void checkMasterAsync() {
        zk.getData("/master", false, masterCheckCallback, null);
    }

    AsyncCallback.DataCallback masterCheckCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat state) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMasterAsync();
                    break;
                case NONODE:
                    runForMasterAsyc();
                    break;
                case NODEEXISTS:
                    logger.error("------------------ node exists ----------------");
                default:
                    logger.error("Error when reading data.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


    //异步方式
    void runForMasterAsyc() {
        System.out.println("serverId:" + serverId);
        zk.create("/master", serverId.getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, masterCreateCallback, null);
    }

    //如果主节点被删除,那么重新选举
    Watcher masterExistsWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                if ("/master".equals(watchedEvent.getPath())) {
                    runForMasterAsyc();
                }
            }
        }
    };

    void takeLeadership() {
        logger.info("Going for list of workers");
        getWorkers();

        (new RecoveredAssignments(zk)).recover(new RecoveredAssignments.RecoveryCallback() {
            public void recoveryComplete(int rc, List<String> tasks) {
                if (rc == RecoveredAssignments.RecoveryCallback.FAILED) {
                    logger.error("Recovery of assigned tasks failed.");
                } else {
                    logger.info("Assigning recovered tasks");
                    getTasks();
                }
            }
        });
    }

    void masterExists() {
        zk.exists("/master",
                masterExistsWatcher,
                masterExistsCallback,
                null);
    }

    AsyncCallback.StatCallback masterExistsCallback = new AsyncCallback.StatCallback() {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    masterExists();

                    break;
                case OK:
                    break;
                case NONODE:
                    state = MasterStates.RUNNING;
                    runForMasterAsyc();
                    logger.info("It sounds like the previous master is gone, " +
                            "so let's run for master again.");

                    break;
                default:
                    checkMasterAsync();
                    break;
            }
        }
    };
    Watcher workersChangeWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            if (e.getType() == Event.EventType.NodeChildrenChanged) {
                assert "/workers".equals(e.getPath());

                getWorkers();
            }
        }
    };

    void getWorkers() {
        zk.getChildren("/workers",
                workersChangeWatcher,
                workersGetChildrenCallback,
                null);
    }

    AsyncCallback.ChildrenCallback workersGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getWorkers();
                    break;
                case OK:
                    logger.info("Succesfully got a list of workers: "
                            + children.size()
                            + " workers");
                    reassignAndSet(children);
                    break;
                default:
                    logger.error("getChildren failed",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    void reassignAndSet(List<String> children) {
        List<String> toProcess;

        if (workersCache == null) {
            workersCache = new ChildrenCache(children);
            toProcess = null;
        } else {
            logger.info("Removing and setting");
            toProcess = workersCache.removedAndSet(children);
        }

        if (toProcess != null) {
            for (String worker : toProcess) {
                getAbsentWorkerTasks(worker);
            }
        }
    }

    void getAbsentWorkerTasks(String worker) {
        zk.getChildren("/assign/" + worker, false, workerAssignmentCallback, null);
    }

    AsyncCallback.ChildrenCallback workerAssignmentCallback = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getAbsentWorkerTasks(path);

                    break;
                case OK:
                    logger.info("Succesfully got a list of assignments: "
                            + children.size()
                            + " tasks");

                /*
                 * Reassign the tasks of the absent worker.
                 */

                    for (String task : children) {
                        getDataReassign(path + "/" + task, task);
                    }
                    break;
                default:
                    logger.error("getChildren failed", KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    void getDataReassign(String path, String task) {
        zk.getData(path,
                false,
                getDataReassignCallback,
                task);
    }

    class RecreateTaskCtx {
        String path;
        String task;
        byte[] data;

        RecreateTaskCtx(String path, String task, byte[] data) {
            this.path = path;
            this.task = task;
            this.data = data;
        }
    }

    AsyncCallback.DataCallback getDataReassignCallback = new AsyncCallback.DataCallback() {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getDataReassign(path, (String) ctx);

                    break;
                case OK:
                    recreateTask(new RecreateTaskCtx(path, (String) ctx, data));

                    break;
                default:
                    logger.error("Something went wrong when getting data ",
                            KeeperException.create(KeeperException.Code.get(rc)));
            }
        }
    };

    void recreateTask(RecreateTaskCtx ctx) {
        zk.create("/tasks/" + ctx.task,
                ctx.data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                recreateTaskCallback,
                ctx);
    }

    /**
     * Recreate znode callback
     */
    AsyncCallback.StringCallback recreateTaskCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    recreateTask((RecreateTaskCtx) ctx);

                    break;
                case OK:
                    deleteAssignment(((RecreateTaskCtx) ctx).path);

                    break;
                case NODEEXISTS:
                    logger.info("Node exists already, but if it hasn't been deleted, " +
                            "then it will eventually, so we keep trying: " + path);
                    recreateTask((RecreateTaskCtx) ctx);

                    break;
                default:
                    logger.error("Something wwnt wrong when recreating task",
                            KeeperException.create(KeeperException.Code.get(rc)));
            }
        }
    };

    /**
     * Delete assignment of absent worker
     *
     * @param path Path of znode to be deleted
     */
    void deleteAssignment(String path) {
        zk.delete(path, -1, taskDeletionCallback, null);
    }

    AsyncCallback.VoidCallback taskDeletionCallback = new AsyncCallback.VoidCallback() {
        public void processResult(int rc, String path, Object rtx) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    deleteAssignment(path);
                    break;
                case OK:
                    logger.info("Task correctly deleted: " + path);
                    break;
                default:
                    logger.error("Failed to delete task data" +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    Watcher tasksChangeWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            if (e.getType() == Event.EventType.NodeChildrenChanged) {
                assert "/tasks".equals(e.getPath());

                getTasks();
            }
        }
    };

    void getTasks() {
        zk.getChildren("/tasks",
                tasksChangeWatcher,
                tasksGetChildrenCallback,
                null);
    }

    AsyncCallback.ChildrenCallback tasksGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();

                    break;
                case OK:
                    List<String> toProcess;
                    if (tasksCache == null) {
                        tasksCache = new ChildrenCache(children);

                        toProcess = children;
                    } else {
                        toProcess = tasksCache.addedAndSet(children);
                    }

                    if (toProcess != null) {
                        assignTasks(toProcess);
                    }

                    break;
                default:
                    logger.error("getChildren failed.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    void assignTasks(List<String> tasks) {
        for (String task : tasks) {
            getTaskData(task);
        }
    }

    void getTaskData(String task) {
        zk.getData("/tasks/" + task,
                false,
                taskDataCallback,
                task);
    }

    AsyncCallback.DataCallback taskDataCallback = new AsyncCallback.DataCallback() {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTaskData((String) ctx);

                    break;
                case OK:
                /*
                 * Choose worker at random.
                 */
                    List<String> list = workersCache.getList();
                    String designatedWorker = list.get(random.nextInt(list.size()));

                /*
                 * Assign task to randomly chosen worker.
                 */
                    String assignmentPath = "/assign/" +
                            designatedWorker +
                            "/" +
                            (String) ctx;
                    logger.info("Assignment path: " + assignmentPath);
                    createAssignment(assignmentPath, data);

                    break;
                default:
                    logger.error("Error when trying to get task data.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    void createAssignment(String path, byte[] data) {
        zk.create(path,
                data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                assignTaskCallback,
                data);
    }

    AsyncCallback.StringCallback assignTaskCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createAssignment(path, (byte[]) ctx);

                    break;
                case OK:
                    logger.info("Task assigned correctly: " + name);
                    deleteTask(name.substring(name.lastIndexOf("/") + 1));

                    break;
                case NODEEXISTS:
                    logger.warn("Task already assigned");

                    break;
                default:
                    logger.error("Error when trying to assign task.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    /*
     * Once assigned, we delete the task from /tasks
     */
    void deleteTask(String name) {
        zk.delete("/tasks/" + name, -1, taskDeleteCallback, null);
    }

    AsyncCallback.VoidCallback taskDeleteCallback = new AsyncCallback.VoidCallback() {
        public void processResult(int rc, String path, Object ctx) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    deleteTask(path);

                    break;
                case OK:
                    logger.info("Successfully deleted " + path);

                    break;
                case NONODE:
                    logger.info("Task has been deleted already");

                    break;
                default:
                    logger.error("Something went wrong here, " +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    /**
     * Closes the ZooKeeper session.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                logger.warn("Interrupted while closing ZooKeeper session.", e);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Master m = new Master("127.0.0.1:2181");
        m.startZk();

        while (!m.isConnected()) {
            Thread.sleep(100);
        }
        /*
         * bootstrap() creates some necessary znodes.
         */
        m.bootStrap();

        /*
         * now runs for master.
         */
        m.runForMasterAsyc();

        while (!m.isExpired()) {
            Thread.sleep(1000);
        }

        m.stopZk();
    }
}
