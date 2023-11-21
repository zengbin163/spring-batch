package com.zengbin.batch.processor;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.zengbin.batch.vo.reader.FoodR;
import com.zengbin.batch.vo.writer.FoodW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FoodListProcessor implements ItemProcessor<FoodR, FoodW>,StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(FoodListProcessor.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        long threadId = Thread.currentThread().getId();
        //logger.info("--------------------FoodListProcessor.beforeStep execution, threadId={}",threadId);
    }

    @Override
    public FoodW process(FoodR foodR) throws Exception {
        if(null == foodR) {
            logger.error("--------------------FoodListProcessor.process FoodR is null");
            return null;
        }
        long threadId = Thread.currentThread().getId();
        //logger.info("--------------------FoodListProcessor.process, threadId={}", threadId);
        BeanCopier<FoodW> beanCopier = BeanCopier.create(foodR, new FoodW(), CopyOptions.create());
        FoodW foodW = beanCopier.copy();
        String dateStr = DateUtil.formatDateTime(new Date());
        foodW.setFoodInfo(foodW.getFoodInfo() + "<" + dateStr + ">");
        return foodW;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long threadId = Thread.currentThread().getId();
        //logger.info("--------------------FoodListProcessor.afterStep execution, threadId={}", threadId);
        return ExitStatus.COMPLETED;
    }
}
