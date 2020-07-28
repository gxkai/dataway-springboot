package com.dataway.cn.component;

import org.jetbrains.annotations.NotNull;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author phil
 * @date 2020/06/23 12:31
 */
@Component
public class JobFactory extends AdaptableJobFactory {
    private final AutowireCapableBeanFactory capableBeanFactory;

    public JobFactory(AutowireCapableBeanFactory capableBeanFactory){
        this.capableBeanFactory = capableBeanFactory;
    }
    @NotNull
    @Override
    protected Object createJobInstance(@NotNull final TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
