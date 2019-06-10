package com.example.demo.hystrix.fallback;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author kejun
 * @date 2019/6/10 下午2:12
 * 降级回退
 */
public class CommandHelloFailure extends HystrixCommand<String> {

    private String name;

    public CommandHelloFailure(String name){
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name=name;
    }

    @Override
    protected String run() throws Exception {
        throw  new RuntimeException("this command always fail");
    }

    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }
}
