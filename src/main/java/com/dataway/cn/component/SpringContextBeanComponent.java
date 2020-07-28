package com.dataway.cn.component;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring上下文
 * @author phil
 * @date 2020/06/05 10:35
 */
@Component
public class SpringContextBeanComponent  implements ApplicationContextAware {

    private static ApplicationContext context = null;
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    public static <T> T getBean(String name) {

        return (T) context.getBean(name);
    }

    public static <T> T getBean(Class<T> beanClass){

        return context.getBean(beanClass);
    }
}
