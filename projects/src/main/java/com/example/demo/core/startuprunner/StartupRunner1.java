package com.example.demo.core.startuprunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author kejun
 * @date 2018/8/9 上午10:59
 * 在项目启动的时候需要做一些初始化的操作，比如读取配置文件信息，数据库连接，清除缓存信息等
 */

@Component
@Order(value = 1)
public class StartupRunner1 implements ApplicationRunner{

    private static final Logger logger = LoggerFactory.getLogger(StartupRunner1.class);

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("服务器启动成功！<<<<使用ApplicationRunner接口");
    }
}
