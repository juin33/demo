package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.beans.Car;
import com.example.demo.beans.MyTest;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertEquals;

/**
 * @author kejun
 * @date 2019/9/3 下午4:25
 */
@SuppressWarnings("deprecation")
public class BeanTest {
    @Test
    public void testSimpleLoad(){
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring/appContext.xml"));
        MyTest myTest = (MyTest) beanFactory.getBean("myTest");
        assertEquals("test",myTest.getStr());
    }

    @Test
    public void testCar(){
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring/appContext.xml"));
        Car car = (Car) beanFactory.getBean("&car");
        System.out.println(JSON.toJSONString(car));
    }
}
