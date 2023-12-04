package com.zengbin.batch.job.partitioner;

import com.zengbin.batch.quartz.ItemListQuartzJob;
import com.zengbin.batch.util.DateUtil;
import com.zengbin.batch.util.ShardingUtil;
import com.zengbin.batch.vo.Sharding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***分区器设置， 需要将时间分片并设置到从步骤中上下文中**/
public class ItemListPartitioner implements Partitioner {
    private static final Logger logger = LoggerFactory.getLogger(ItemListPartitioner.class);
    @Value("${begin.time}")
    private String beginTime;
    @Value("${end.time}")
    private String endTime;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<Sharding> shardingList = ShardingUtil.sharding(beginTime, endTime);
        String text = "----ItemListPartitioner---第%s分区-----开始时间：%s---结束时间：%s--------------";
        Map<String, ExecutionContext> map = new HashMap<>();
        if(CollectionUtils.isEmpty(shardingList) || shardingList.size() != gridSize) {
            throw new IllegalArgumentException("分片参数存在问题！");
        }
        /** shardingList.size 一定会等于gridSize **/
        for (int i = 0; i < shardingList.size(); i++) {
            Sharding sharding = shardingList.get(i);
            logger.info(String.format(text, i, DateUtil.getStr(sharding.getBeginDate()), DateUtil.getStr(sharding.getEndDate())));
            ExecutionContext ex = new ExecutionContext();
            ex.put("partitionBeginTime", sharding.getBeginDate());
            ex.put("partitionEndTime", sharding.getEndDate());
            map.put("partitioner_" + i, ex);
        }
        return map;
    }
}
