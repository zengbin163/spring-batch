package com.zengbin.batch.job.processor;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.zengbin.batch.vo.reader.FoodR;
import com.zengbin.batch.vo.writer.FoodW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class ItemListProcessor implements ItemProcessor<FoodR, FoodW> {

    private static final Logger logger = LoggerFactory.getLogger(ItemListProcessor.class);

    @Override
    public FoodW process(FoodR foodR) throws Exception {
        if(null == foodR) {
            logger.error("------ItemListProcessor.process FoodR is null");
            return null;
        }
        long threadId = Thread.currentThread().getId();
        //logger.info("------ItemListProcessor.process threadId={}", threadId);
        BeanCopier<FoodW> beanCopier = BeanCopier.create(foodR, new FoodW(), CopyOptions.create());
        FoodW foodW = beanCopier.copy();
        String dateStr = DateUtil.formatDateTime(new Date());
        foodW.setFoodInfo(foodW.getFoodInfo() + "<" + dateStr + ">");
        return foodW;
    }

}
