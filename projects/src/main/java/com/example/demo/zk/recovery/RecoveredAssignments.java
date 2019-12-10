package com.example.demo.zk.recovery;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kejun
 * @date 2019/12/9 上午11:45
 */
public class RecoveredAssignments {
    private static final Logger LOG = LoggerFactory.getLogger(RecoveredAssignments.class);
    List<String> tasks;
    List<String> assignments;
    List<String> status;
    List<String> activeWorkers;
    List<String> assignedWorkers;

    RecoveryCallback cb;

    ZooKeeper zk;

    public interface RecoveryCallback {
        final static int OK = 0;
        final static int FAILED = -1;

        public void recoveryComplete(int rc, List<String> tasks);
    }

    public RecoveredAssignments(ZooKeeper zk){
        this.zk = zk;
        this.assignments = new ArrayList<String>();
    }

    public void recover(RecoveryCallback recoveryCallback){
        // Read task list with getChildren
        cb = recoveryCallback;
        getTasks();
    }

    private void getTasks(){
        zk.getChildren("/tasks", false, tasksCallback, null);
    }

    AsyncCallback.ChildrenCallback tasksCallback = new AsyncCallback.ChildrenCallback(){
        public void processResult(int rc, String path, Object ctx, List<String> children){
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();

                    break;
                case OK:
                    LOG.info("Getting tasks for recovery");
                    tasks = children;
                    getAssignedWorkers();

                    break;
                default:
                    LOG.error("getChildren failed",  KeeperException.create(KeeperException.Code.get(rc), path));
                    cb.recoveryComplete(RecoveryCallback.FAILED, null);
            }
        }
    };

    private void getAssignedWorkers(){
        zk.getChildren("/assign", false, assignedWorkersCallback, null);
    }

    AsyncCallback.ChildrenCallback assignedWorkersCallback = new AsyncCallback.ChildrenCallback(){
        public void processResult(int rc, String path, Object ctx, List<String> children){
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getAssignedWorkers();

                    break;
                case OK:
                    assignedWorkers = children;
                    getWorkers(children);

                    break;
                default:
                    LOG.error("getChildren failed",  KeeperException.create(KeeperException.Code.get(rc), path));
                    cb.recoveryComplete(RecoveryCallback.FAILED, null);
            }
        }
    };

    private void getWorkers(Object ctx){
        zk.getChildren("/workers", false, workersCallback, ctx);
    }

    AsyncCallback.ChildrenCallback workersCallback = new AsyncCallback.ChildrenCallback(){
        public void processResult(int rc, String path, Object ctx, List<String> children){
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getWorkers(ctx);
                    break;
                case OK:
                    LOG.info("Getting worker assignments for recovery: " + children.size());

                /*
                 * No worker available yet, so the master is probably let's just return an empty list.
                 */
                    if(children.size() == 0) {
                        LOG.warn( "Empty list of workers, possibly just starting" );
                        cb.recoveryComplete(RecoveryCallback.OK, new ArrayList<String>());

                        break;
                    }

                /*
                 * Need to know which of the assigned workers are active.
                 */

                    activeWorkers = children;

                    for(String s : assignedWorkers){
                        getWorkerAssignments("/assign/" + s);
                    }

                    break;
                default:
                    LOG.error("getChildren failed",  KeeperException.create(KeeperException.Code.get(rc), path));
                    cb.recoveryComplete(RecoveryCallback.FAILED, null);
            }
        }
    };

    private void getWorkerAssignments(String s) {
        zk.getChildren(s, false, workerAssignmentsCallback, null);
    }

    AsyncCallback.ChildrenCallback workerAssignmentsCallback = new AsyncCallback.ChildrenCallback(){
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getWorkerAssignments(path);
                    break;
                case OK:
                    String worker = path.replace("/assign/", "");

                /*
                 * If the worker is in the list of active
                 * workers, then we add the tasks to the
                 * assignments list. Otherwise, we need to
                 * re-assign those tasks, so we add them to
                 * the list of tasks.
                 */
                    if(activeWorkers.contains(worker)) {
                        assignments.addAll(children);
                    } else {
                        for( String task : children ) {
                            if(!tasks.contains( task )) {
                                tasks.add( task );
                                getDataReassign( path, task );
                            } else {
                            /*
                             * If the task is still in the list
                             * we delete the assignment.
                             */
                                deleteAssignment(path + "/" + task);
                            }

                        /*
                         * Delete the assignment parent.
                         */
                            deleteAssignment(path);
                        }

                    }

                    assignedWorkers.remove(worker);

                /*
                 * Once we have checked all assignments,
                 * it is time to check the status of tasks
                 */
                    if(assignedWorkers.size() == 0){
                        LOG.info("Getting statuses for recovery");
                        getStatuses();
                    }

                    break;
                case NONODE:
                    LOG.info( "No such znode exists: " + path );

                    break;
                default:
                    LOG.error("getChildren failed",  KeeperException.create(KeeperException.Code.get(rc), path));
                    cb.recoveryComplete(RecoveryCallback.FAILED, null);
            }
        }
    };

    /**
     * Get data of task being reassigned.
     *
     * @param path
     * @param task
     */
    void getDataReassign(String path, String task) {
        zk.getData(path,
                false,
                getDataReassignCallback,
                task);
    }

    /**
     * Context for recreate operation.
     *
     */
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

    /**
     * Get task data reassign callback.
     */
    AsyncCallback.DataCallback getDataReassignCallback = new AsyncCallback.DataCallback() {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat)  {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getDataReassign(path, (String) ctx);

                    break;
                case OK:
                    recreateTask(new RecreateTaskCtx(path, (String) ctx, data));

                    break;
                default:
                    LOG.error("Something went wrong when getting data ",
                            KeeperException.create(KeeperException.Code.get(rc)));
            }
        }
    };

    /**
     * Recreate task znode in /tasks
     *
     * @param ctx Recreate text context
     */
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
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    recreateTask((RecreateTaskCtx) ctx);

                    break;
                case OK:
                    deleteAssignment(((RecreateTaskCtx) ctx).path);

                    break;
                case NODEEXISTS:
                    LOG.warn("Node shouldn't exist: " + path);

                    break;
                default:
                    LOG.error("Something wwnt wrong when recreating task",
                            KeeperException.create(KeeperException.Code.get(rc)));
            }
        }
    };

    /**
     * Delete assignment of absent worker
     *
     * @param path Path of znode to be deleted
     */
    void deleteAssignment(String path){
        zk.delete(path, -1, taskDeletionCallback, null);
    }

    AsyncCallback.VoidCallback taskDeletionCallback = new AsyncCallback.VoidCallback(){
        public void processResult(int rc, String path, Object rtx){
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    deleteAssignment(path);
                    break;
                case OK:
                    LOG.info("Task correctly deleted: " + path);
                    break;
                default:
                    LOG.error("Failed to delete task data" +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


    void getStatuses(){
        zk.getChildren("/status", false, statusCallback, null);
    }

    AsyncCallback.ChildrenCallback statusCallback = new AsyncCallback.ChildrenCallback(){
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  List<String> children){
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getStatuses();

                    break;
                case OK:
                    LOG.info("Processing assignments for recovery");
                    status = children;
                    processAssignments();

                    break;
                default:
                    LOG.error("getChildren failed",  KeeperException.create(KeeperException.Code.get(rc), path));
                    cb.recoveryComplete(RecoveryCallback.FAILED, null);
            }
        }
    };

    private void processAssignments(){
        LOG.info("Size of tasks: " + tasks.size());
        // Process list of pending assignments
        for(String s: assignments){
            LOG.info("Assignment: " + s);
            deleteAssignment("/tasks/" + s);
            tasks.remove(s);
        }

        LOG.info("Size of tasks after assignment filtering: " + tasks.size());

        for(String s: status){
            LOG.info( "Checking task: {} ", s );
            deleteAssignment("/tasks/" + s);
            tasks.remove(s);
        }
        LOG.info("Size of tasks after status filtering: " + tasks.size());

        // Invoke callback
        cb.recoveryComplete(RecoveryCallback.OK, tasks);
    }
}
