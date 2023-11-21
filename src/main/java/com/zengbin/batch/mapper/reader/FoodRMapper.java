package com.zengbin.batch.mapper.reader;

import com.zengbin.batch.vo.reader.FoodR;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FoodRMapper {

	Long insert(FoodR food);

	List<FoodR> findFoodListBySharding(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

	List<FoodR> findFoodListByOrder(@Param("queryNum") Integer queryNum, @Param("beginUid") Long beginUid);

}
