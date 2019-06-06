package com.example.demo.hystrix;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author kejun
 * 参考文章:http://www.sohu.com/a/131568369_494947
 * @date 2019/6/6 上午11:37
 */
public class CommandOrder extends HystrixCommand<String>{

    private static final Logger logger = LoggerFactory.getLogger(CommandOrder.class);

    private String orderName;

    public CommandOrder(String orderName){
        super(Setter.withGroupKey(
                //服务分组
                HystrixCommandGroupKey.Factory.asKey("OrderGroup"))
                //线程分组
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("OrderPool"))
                //线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        //配置核心线程池大小和线程池最大大小
                        .withCoreSize(10)
                        //线程池中空闲线程生存时间
                        .withKeepAliveTimeMinutes(60)
                        //配置线程池队列最大大小
                        .withMaximumSize(15)
                        //限定当前队列大小
                        .withQueueSizeRejectionThreshold(10000))
                //命令属性配置
                .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        //线程池隔离
                                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)));


        this.orderName = orderName;
    }

    @Override
    protected String run() throws Exception {
        logger.info("orderName:{}",orderName);
        TimeUnit.MILLISECONDS.sleep(100);
        return "orderName:{}"+orderName;
    }

    public static void main(String[] args) throws Exception {
        CommandOrder commandPhone = new CommandOrder("手机");
        CommandOrder command = new CommandOrder("电视");
        //阻塞方式执行
        String execute = commandPhone.execute();
        logger.info("execute=[{}]", execute);
        //异步非阻塞方式
        Future<String> queue = command.queue();
        String value = queue.get(200, TimeUnit.MILLISECONDS);
        logger.info("value=[{}]", value);
        CommandUser commandUser = new CommandUser("张三");
        String name = commandUser.execute();
        logger.info("name=[{}]", name);
    }
}
