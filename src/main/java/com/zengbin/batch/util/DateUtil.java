package com.zengbin.batch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static Date getDate(String dateStr) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            logger.error("Error Msg ", e);
        }
        return null;
    }

    public static String getStr(Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            return dateFormat.format(date);
        } catch (Exception e) {
            logger.error("Error Msg ", e);
        }
        return null;
    }

}
