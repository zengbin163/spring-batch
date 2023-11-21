package com.zengbin.batch.vo.reader;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FoodR {
	
    private Long uid;
    private Integer categoryId;
    private String foodName;
    private String foodPic;
    private String foodInfo;
    private BigDecimal price;
    private Date createTime;
    private Date updateTime;

}
