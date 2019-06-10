package com.example.demo.hystrix.fallback;

import com.example.demo.hystrix.model.UserAccount;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author kejun
 * @date 2019/6/10 下午2:12
 * 降级回退:Stubbed 自己组装一个值返回
 */
public class CommandWithStubbedFallback extends HystrixCommand<UserAccount> {

    private String name;

    public CommandWithStubbedFallback(String name){
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name=name;
    }

    @Override
    protected UserAccount run() throws Exception {
        throw  new RuntimeException("this command always fail");
    }

    @Override
    protected UserAccount getFallback() {
        return new UserAccount("123",name);
    }
}
