package com.example.demo.beans.limit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import site.higgs.limiter.interceptor.LimiterFallbackResolver;

import java.lang.reflect.Method;

/**
 * @author kejun
 * @date 2018/11/23 下午3:42
 */
public class BusyFallbackResolver implements LimiterFallbackResolver<ResponseEntity> {
    @Override
    public ResponseEntity resolve(Method method, Class<?> aClass, Object[] objects, String s) {
        return new ResponseEntity("服务繁忙", HttpStatus.OK);
    }
}
