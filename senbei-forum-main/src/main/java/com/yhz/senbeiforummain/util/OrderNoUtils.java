package com.yhz.senbeiforummain.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNoUtils {

    private static int autoId = 0;
    /**
     * 获取订单编号
     * @return
     */
    public static String getOrderNo() {
        return "1" + getNo();
    }

    /**
     * 获取退款单编号
     * @return
     */
    public static String getRefundNo() {
        return "2" + getNo();
    }

    /**
     * 获取编号
     * @return
     */
    public static String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());

        Random random = new Random();
        String result = String.valueOf(random.nextInt(10));
        String no = newDate +result+ autoId;
        autoId++;
        return no;
    }

}