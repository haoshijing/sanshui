package com.keke.sanshui.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static void main(String[] args) throws ParseException {
        Date InputDate = new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(20171025));
        Calendar cDate = Calendar.getInstance();
        cDate.setFirstDayOfWeek(Calendar.MONDAY);
        cDate.setTime(InputDate);

        Calendar firstDate = Calendar.getInstance();

        firstDate.setFirstDayOfWeek(Calendar.MONDAY);
        firstDate.setTime(InputDate);

        Calendar lastDate = Calendar.getInstance();
        lastDate.setFirstDayOfWeek(Calendar.MONDAY);
        lastDate.setTime(InputDate);

        if (cDate.get(Calendar.WEEK_OF_YEAR) == 1 && cDate.get(Calendar.MONTH) == 11) {
            firstDate.set(Calendar.YEAR, cDate.get(Calendar.YEAR) + 1);
            lastDate.set(Calendar.YEAR, cDate.get(Calendar.YEAR) + 1);
        }

        int typeNum = cDate.get(Calendar.WEEK_OF_YEAR);
        System.out.println(typeNum);

        firstDate.set(Calendar.WEEK_OF_YEAR, typeNum);
        firstDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // 所在周开始日期
        String beginDate = new SimpleDateFormat("yyyy/MM/dd").format(firstDate.getTime());
        System.out.println(beginDate);

        lastDate.set(Calendar.WEEK_OF_YEAR, typeNum);
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 所在周结束日期
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(lastDate.getTime());
        System.out.println(endDate);

    }
}
