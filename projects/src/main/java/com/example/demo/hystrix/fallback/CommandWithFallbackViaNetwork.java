package com.example.demo.hystrix.fallback;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * @author kejun
 * @date 2019/6/10 下午4:20
 * 通过远程缓存的方式。在失败的情况下再发起一次remote请求，
 * 不过这次请求的是一个缓存比如redis。
 * 由于是又发起一起远程调用，所以会重新封装一次Command，
 * 这个时候要注意，执行fallback的线程一定要跟主线程区分开，也就是重新命名一个ThreadPoolKey
 */
public class CommandWithFallbackViaNetwork extends HystrixCommand<String>{

    private final int id;

    public CommandWithFallbackViaNetwork(int id){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueCommand")));
        this.id = id;
    }

    @Override
    protected String run() throws Exception {
        throw new RuntimeException("force failure for example");
    }

    @Override
    protected String getFallback() {
        return new FallbackViaNetwork(id).execute();
    }

    private static class FallbackViaNetwork extends HystrixCommand<String> {
        private final int id;

        public FallbackViaNetwork(int id) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFallbackCommand"))
                    // use a different threadpool for the fallback command
                    // so saturating the RemoteServiceX pool won't prevent
                    // fallbacks from executing
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteServiceXFallback")));
            this.id = id;
        }

        @Override
        protected String run() {
//            MemCacheClient.getValue(id);
            return "重试中....id=" + id;
        }

        @Override
        protected String getFallback() {
            // the fallback also failed
            // so this fallback-of-a-fallback will
            // fail silently and return null
            return null;
        }
    }

    public static void main(String[] args) {
        CommandWithFallbackViaNetwork network = new CommandWithFallbackViaNetwork(1);
        String result = network.execute();
        System.out.println(result);
    }
}
