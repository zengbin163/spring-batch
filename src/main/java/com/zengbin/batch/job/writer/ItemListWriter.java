package com.zengbin.batch.job.writer;

import cn.hutool.core.collection.CollectionUtil;
import com.zengbin.batch.service.writer.FoodWriterService;
import com.zengbin.batch.vo.writer.FoodW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ItemListWriter implements ItemWriter<FoodW> {

    private static final Logger logger = LoggerFactory.getLogger(ItemListWriter.class);
    @Autowired
    private FoodWriterService foodWriterService;

    @Override
    public void write(List<? extends FoodW> items) throws Exception {
        if(CollectionUtil.isEmpty(items)) {
            logger.info("------ItemListWriter.write items is null");
            return;
        }
        long threadId = Thread.currentThread().getId();
        logger.info("------ItemListWriter.write items.size={}, threadId={}", items.size(), threadId);
        List<FoodW> list = new ArrayList<>();
        for(FoodW w : items) {
            list.add(w);
        }
        this.foodWriterService.insertBatch(list);
    }

}
