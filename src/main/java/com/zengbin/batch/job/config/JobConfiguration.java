package com.zengbin.batch.job.config;

import com.zengbin.batch.job.listener.WorkStepListener;
import com.zengbin.batch.job.partitioner.ItemListPartitioner;
import com.zengbin.batch.job.processor.ItemListProcessor;
import com.zengbin.batch.job.writer.ItemListWriter;
import com.zengbin.batch.util.ShardingUtil;
import com.zengbin.batch.vo.Sharding;
import com.zengbin.batch.vo.reader.FoodR;
import com.zengbin.batch.vo.writer.FoodW;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class JobConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(JobConfiguration.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("readerSessionFactory")
    private SqlSessionFactory readerSessionFactory;
    @Value("${default.pageSize}")
    private Integer pageSize;
    @Value("${default.chunkSize}")
    private Integer chunkSize;
    @Value("${begin.time}")
    private String beginTime;
    @Value("${end.time}")
    private String endTime;

    @Bean
    @StepScope
    public MyBatisPagingItemReader<FoodR> itemReader(
            @Value("#{stepExecutionContext[partitionBeginTime]}")Date partitionBeginTime,
            @Value("#{stepExecutionContext[partitionEndTime]}")Date partitionEndTime) {
        Long threadId = Thread.currentThread().getId();
        logger.info("------JobConfiguration.itemReader begin execute, threadId={}", threadId);
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("beginTime", partitionBeginTime);
        parameterValues.put("endTime", partitionEndTime);
        return new MyBatisPagingItemReaderBuilder<FoodR>()
                .sqlSessionFactory(readerSessionFactory)
                .queryId("com.zengbin.batch.mapper.reader.FoodRMapper.findFoodListBySharding")
                .parameterValues(parameterValues)
                .pageSize(pageSize)
                .build();
    }

    @Bean
    public ItemListWriter itemWriter() {
        Long threadId = Thread.currentThread().getId();
        logger.info("------JobConfiguration.itemWriter begin execute, threadId={}", threadId);
        return new ItemListWriter();
    }

    @Bean
    public ItemListProcessor itemProcessor() {
        Long threadId = Thread.currentThread().getId();
        logger.info("------JobConfiguration.itemProcessor begin execute, threadId={}", threadId);
        return new ItemListProcessor();
    }

    @Bean
    public WorkStepListener workStepListener() {
        return new WorkStepListener();
    }

    @Bean
    public Step workStep() {
        return stepBuilderFactory.get("workStep")
                .<FoodR, FoodW>chunk(chunkSize)
                .reader(itemReader(null, null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .listener(workStepListener())
                .build();
    }

    /**分区器**/
    @Bean
    public ItemListPartitioner itemListPartitioner(){
        return new ItemListPartitioner();
    }

    /**分区处理器**/
    @Bean
    public PartitionHandler itemListPartitionHandler(){
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        List<Sharding> shardingList = ShardingUtil.sharding(beginTime, endTime);
        handler.setGridSize(shardingList.size());
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor());
        handler.setStep(workStep());
        try {
            handler.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handler;
    }

    /**主步骤**/
    @Bean
    public Step masterStep(){
        return stepBuilderFactory.get("masterStep")
                .partitioner(workStep().getName(), itemListPartitioner())
                .partitionHandler(itemListPartitionHandler())
                .build();
    }
    /**作业**/
    @Bean
    public Job itemListJob(){
        return jobBuilderFactory.get("itemListJob")
                .start(masterStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

}
