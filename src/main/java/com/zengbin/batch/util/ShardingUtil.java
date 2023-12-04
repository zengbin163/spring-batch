package com.zengbin.batch.util;

import com.zengbin.batch.vo.Sharding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShardingUtil {

    public static List<Sharding> sharding(String beginTimeStr, String endTimeStr) {
        Date beginDate = DateUtil.getDate(beginTimeStr);
        Date endDate = DateUtil.getDate(endTimeStr);
        List<Sharding> list = new ArrayList<>();
        while(endDate.after(beginDate)) {
            Sharding sharding = new Sharding();
            sharding.setBeginDate(beginDate);
            Date nextDate = addMonth(beginDate, 1);
            if(nextDate.after(endDate)) {
                nextDate = endDate;
            }
            sharding.setEndDate(nextDate);
            beginDate = nextDate;
            list.add(sharding);
        }
        return list;
    }

    public static Date addMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

}
