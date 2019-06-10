package com.example.demo.hystrix;

import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
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
                                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                                        //配置熔断器
                                        .withCircuitBreakerEnabled(true)//是否启用熔断器，默认是TURE
                                        .withCircuitBreakerForceOpen(false)//熔断器强制打开，始终保持打开状态。默认值FLASE。
                                        .withCircuitBreakerForceClosed(false)//熔断器强制关闭，始终保持关闭状态。默认值FLASE。
                                        .withCircuitBreakerErrorThresholdPercentage(5)//(1)错误百分比超过5%----设定错误百分比，默认值50%，例如一段时间（10s）内有100个请求，其中有55个超时或者异常返回了，那么这段时间内的错误百分比是55%，大于了默认值50%，这种情况下触发熔断器-打开
                                        .withCircuitBreakerRequestVolumeThreshold(10)//(2)10s以内调用次数10次，同时满足(1)(2)熔断器打开
                                        .withCircuitBreakerSleepWindowInMilliseconds(5000))//隔5s之后，熔断器会尝试半开(关闭)，重新放进来请求
        );


        this.orderName = orderName;
    }

    @Override
    protected String run() throws Exception {
        logger.info("orderName:{}",orderName);
        Random random = new Random();
        if(1==random.nextInt(2)){
            throw new Exception("make exception");
        }
        TimeUnit.MILLISECONDS.sleep(100);
        return "orderName:{}"+orderName;
    }

    @Override
    //回退降级
    protected String getFallback() {
        return "fallback";
    }

    public static void main(String[] args) throws Exception {
//        CommandOrder commandPhone = new CommandOrder("手机");
//        CommandOrder command = new CommandOrder("电视");
//        //阻塞方式执行
//        String execute = commandPhone.execute();
//        logger.info("execute=[{}]", execute);
//        //异步非阻塞方式
//        Future<String> queue = command.queue();
//        String value = queue.get(200, TimeUnit.MILLISECONDS);
//        logger.info("value=[{}]", value);
//        CommandUser commandUser = new CommandUser("张三");
//        String name = commandUser.execute();
//        logger.info("name=[{}]", name);

        for(int i = 0;i<25 ;i++){
            TimeUnit.MILLISECONDS.sleep(200);
            CommandOrder order = new CommandOrder("testCommandOrder");
            String result = order.queue().get();
            System.out.println("call times:"+(i+1)+"   result:"+result +" isCircuitBreakerOpen: "+order.isCircuitBreakerOpen());
        }
    }
}
