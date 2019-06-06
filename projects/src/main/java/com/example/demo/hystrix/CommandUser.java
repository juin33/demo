package com.example.demo.hystrix;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author kejun
 * @date 2019/6/6 下午1:32
 */
public class CommandUser extends HystrixCommand<String> {

    private static final Logger logger  = LoggerFactory.getLogger(CommandUser.class);

    private String name;

    public CommandUser(String name){
        super(Setter.withGroupKey(
                //服务分组
                HystrixCommandGroupKey.Factory.asKey("UserGruop"))
                //线程分组
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserPool"))
                //线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10)
                        .withMaximumSize(15)
                        .withKeepAliveTimeMinutes(60)
                        .withQueueSizeRejectionThreshold(1000))
                //线程池隔离
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)));

        this.name = name;
    }


    @Override
    protected String run() throws Exception {
        logger.info("userName:{}",name);
        TimeUnit.MILLISECONDS.sleep(200);
        return "name: "+name;
    }
}
