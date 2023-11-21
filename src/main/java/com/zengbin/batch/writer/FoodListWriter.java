package com.zengbin.batch.writer;

import cn.hutool.core.collection.CollectionUtil;
import com.zengbin.batch.processor.FoodListProcessor;
import com.zengbin.batch.service.writer.FoodWriterService;
import com.zengbin.batch.vo.writer.FoodW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FoodListWriter implements ItemWriter<FoodW>, StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(FoodListWriter.class);
    @Autowired
    private FoodWriterService foodWriterService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        long threadId = Thread.currentThread().getId();
        logger.info("--------------------FoodListWriter.beforeStep execution, threadId={}", threadId);
    }

    @Override
    public void write(List<? extends FoodW> items) throws Exception {
        if(CollectionUtil.isEmpty(items)) {
            logger.info("--------------------FoodListWriter.write items is null");
            return;
        }
        long threadId = Thread.currentThread().getId();
        logger.info("--------------------FoodListWriter.write items.size={}, threadId={}", items.size(), threadId);
        List<FoodW> list = new ArrayList<>();
        for(FoodW w : items) {
            list.add(w);
        }
        this.foodWriterService.insertBatch(list);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long threadId = Thread.currentThread().getId();
        logger.info("--------------------FoodListWriter.afterStep execution, threadId={}", threadId);
        return ExitStatus.COMPLETED;
    }
}
