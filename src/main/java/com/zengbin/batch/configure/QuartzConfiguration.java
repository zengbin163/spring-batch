package com.zengbin.batch.configure;

import com.zengbin.batch.quartz.ItemListQuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail jobDetail() {
        //指定任务描述具体的实现类
        return JobBuilder.newJob(ItemListQuartzJob.class)
                // 指定任务的名称
                .withIdentity("QuartzJobExec")
                // 任务描述
                .withDescription("任务描述：同步数据")
                // 每次任务执行后进行存储
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        //创建触发器
        return TriggerBuilder.newTrigger()
                // 绑定工作任务
                .forJob(jobDetail())
                // 每隔 5 秒执行一次 job
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(50000000))
                .build();
    }

}
