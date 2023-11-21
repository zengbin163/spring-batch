package com.zengbin.batch.reader;

import com.zengbin.batch.vo.reader.FoodR;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class FoodListReader implements StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(FoodListReader.class);
    private static final Integer DEFAULT_PAGE_SIZE = 1000;
    @Autowired
    @Qualifier("readerSessionFactory")
    private SqlSessionFactory readerSessionFactory;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        long threadId = Thread.currentThread().getId();
        logger.info("--------------------FoodListReader.beforeStep, threadId={}", threadId);
    }

    public MyBatisPagingItemReader<FoodR> create(Date beginTime, Date endTime) throws Exception {
        long threadId = Thread.currentThread().getId();
        logger.info("--------------------FoodListReader.create threadId={}", threadId);
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("beginTime", beginTime);
        parameterValues.put("endTime", endTime);
        return new MyBatisPagingItemReaderBuilder<FoodR>()
                .sqlSessionFactory(readerSessionFactory)
                .queryId("com.zengbin.batch.mapper.reader.FoodRMapper.findFoodListBySharding")
                .parameterValues(parameterValues)
                .pageSize(DEFAULT_PAGE_SIZE)
                .build();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long threadId = Thread.currentThread().getId();
        logger.info("--------------------FoodListReader.afterStep, threadId={}", threadId);
        return ExitStatus.COMPLETED;
    }
}
