package com.zengbin.batch.step;

import com.zengbin.batch.processor.FoodListProcessor;
import com.zengbin.batch.reader.FoodListReader;
import com.zengbin.batch.util.DateUtil;
import com.zengbin.batch.util.ShardingUtil;
import com.zengbin.batch.vo.Sharding;
import com.zengbin.batch.vo.reader.FoodR;
import com.zengbin.batch.vo.writer.FoodW;
import com.zengbin.batch.writer.FoodListWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class StepCreator {

    private static final Logger logger = LoggerFactory.getLogger(StepCreator.class);
    private static final Integer CHUNK_SIZE = 1000;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private FoodListReader reader;
    @Autowired
    private FoodListProcessor processor;
    @Autowired
    private FoodListWriter writer;

    public Step create(String stepName, Date beginTime, Date endTime) throws Exception {
        return stepBuilderFactory.get(stepName)
                .<FoodR, FoodW> chunk(CHUNK_SIZE)
                .reader(reader.create(beginTime, endTime))
                .processor(processor)
                .writer(writer)
                //打开spring多线程
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    public List<Step> createList(String beginTimeStr, String endTimeStr) {
        List<Sharding> shardingList = ShardingUtil.sharding(beginTimeStr, endTimeStr);
        List<Step> stepList = new ArrayList<>();
        for(Sharding sharding : shardingList) {
            String stepName = "{" + DateUtil.getStr(sharding.getBeginDate()) + "}->{" + DateUtil.getStr(sharding.getEndDate()) + "}";
            try {
                Step step = this.create(stepName, sharding.getBeginDate(), sharding.getEndDate());
                stepList.add(step);
            } catch (Exception e) {
                logger.error("Error Msg ", e);
            }
        }
        return stepList;
    }

}
