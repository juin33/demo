package com.example.demo.service.aspect;

import com.example.demo.dao.Student;
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
        Integer page = (Integer) args[0];
        Integer size = (Integer) args[1];
        String msg = (String) args[2];
        Student student = (Student) args[3];
        logger.info("args:{}",args);
        logger.info("page:{}",page);
        logger.info("msg:{}",msg);
        logger.info("methodName:{}",methodName);
        logger.info("result:{}",result);
        logger.info("student:{}",student.getId());
        return result;
    }


}
