package com.zengbin.batch.web;

import com.zengbin.batch.service.reader.FoodReaderService;
import com.zengbin.batch.vo.reader.FoodR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@RestController()
@RequestMapping("/create")
public class CreateReaderDataController {

    @Autowired
    private FoodReaderService foodReaderService;
    private static final Logger logger = LoggerFactory.getLogger(CreateReaderDataController.class);

    /**造3000万数据用于迁移**/
    private static final int CREATE_DATA_SIZE = 30000000;

    @GetMapping("/reader")
    public String reader() {

        create();

        return "success";
    }

    private void create() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                8,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(20),
                new ThreadPoolExecutor.CallerRunsPolicy());
        List<FoodR> list = new ArrayList<>();
        Random random = new Random();
        for(int i=1;i<=CREATE_DATA_SIZE;i++) {
            FoodR food = new FoodR();
            food.setCategoryId(i);
            food.setFoodName("foodName-" + i);
            food.setFoodPic("foodPic-" + i);
            food.setFoodInfo("foodInfo-" + i);
            food.setPrice(new BigDecimal(i).add(new BigDecimal(random.nextDouble() * 3000)).setScale(2, BigDecimal.ROUND_HALF_UP));
            Date date = randomDate();
            food.setCreateTime(date);
            food.setUpdateTime(date);
            list.add(food);
            if(i % 1000 == 0) {
                SubCallable call = new SubCallable(foodReaderService, list);
                Future<Integer> future = executor.submit(call);
                list = new ArrayList<>();
                try {
                    logger.info("-----------------------第" + i + "批提交线程池," + "size=" + future.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Date randomDate() {
        String dateStr = null;
        Random random = new Random();
        //随机年份
        int year = random.nextInt(2023) % (2023-2015+1) + 2015;
        String sYear = String.valueOf(year);
        //随机月份
        int month = random.nextInt(12) % (12-1+1) + 1;
        String sMonth = String.valueOf(month);
        if(sMonth.length()!=2) {
            sMonth = "0" + sMonth;
        }
        //随机天数
        int day = random.nextInt(29) % (29-1+1) + 1;
        String sDay = String.valueOf(day);
        if(sDay.length()!=2) {
            sDay = "0" + sDay;
        }
        //随机小时
        int hour = random.nextInt(23) % (23-1+1) + 1;
        String sHour = String.valueOf(hour);
        if(sHour.length()!=2) {
            sHour = "0" + sHour;
        }
        //随机分钟
        int min = random.nextInt(59) % (59-1+1) + 1;
        String sMin = String.valueOf(min);
        if(sMin.length()!=2) {
            sMin = "0" + sMin;
        }
        //随机秒钟
        int sec = random.nextInt(59) % (59-1+1) + 1;
        String sSec = String.valueOf(sec);
        if(sSec.length()!=2) {
            sSec = "0" + sSec;
        }
        dateStr = sYear + sMonth + sDay + sHour + sMin + sSec;
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMddHHmmss").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

}

class SubCallable implements Callable<Integer> {

    private FoodReaderService foodReaderService;
    private List<FoodR> list;

    SubCallable() {

    }

    SubCallable(FoodReaderService foodReaderService, List<FoodR> list) {
        this.foodReaderService = foodReaderService;
        this.list = list;
    }

    @Override
    public Integer call() throws Exception {
        foodReaderService.insertBatch(list);
        return list.size();
    }
}
