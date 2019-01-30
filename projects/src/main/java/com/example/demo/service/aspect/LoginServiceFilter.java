package com.example.demo.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author kejun
 * @date 2019/1/30 下午4:28
 */
@Aspect
@Component
public class LoginServiceFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceFilter.class);

    @Pointcut("execution(* com.example.demo.service.impl.LoginServiceImpl.*(..))")
    public void dos() {
    }

    @Around("dos()")
    public Object doCut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("point cut");
        Object[] args = proceedingJoinPoint.getArgs();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object result = proceedingJoinPoint.proceed();
        logger.info("args:{}",args);
        logger.info("methodName:{}",methodName);
        logger.info("result:{}",result);
        return result;
    }


}
