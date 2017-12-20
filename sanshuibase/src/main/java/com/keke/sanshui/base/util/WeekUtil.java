package com.keke.sanshui.base.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;

public final class WeekUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final long WEEK_MILL =  7 *3600*24*1000;
    public static int getCurrentWeek(){
        return getCurrentWeek("");
    }


    public static int getCurrentWeek(String dateStr){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2017);
        calendar.set(Calendar.MONTH,Calendar.OCTOBER);
        calendar.set(Calendar.DATE,23);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        long startTimestamp = calendar.getTimeInMillis();
        long endTimestamp = Calendar.getInstance().getTimeInMillis();
        if(StringUtils.isNotEmpty(dateStr)){
            try {
                Date date = format.parse(dateStr);
                Long diffWeek = (date.getTime() - startTimestamp) / WEEK_MILL;
                return  diffWeek.intValue() + 1;
            }catch (Exception e){
                return 0;
            }
        }else{
            Long diffWeek =  (endTimestamp - startTimestamp) /WEEK_MILL;
            return diffWeek.intValue() + 1;
        }

    }

    public static long getWeekStartTimestamp(int week){
//        Calendar cal = Calendar.getInstance();
//        cal.set(2017,Calendar.OCTOBER,23,0,0,0);
//        cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)+(week-1));
//        return cal.getTimeInMillis();
        DateTime dateTime = new DateTime(2017,10,29,0,0,0);
        return dateTime.plusWeeks(week-1).getMillis();
    }
    public static long getWeekEndTimestamp(int week){
//        Calendar cal = Calendar.getInstance();
//        cal.set(2017,Calendar.OCTOBER,29,23,59,59);
//        cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)+(week-1));
//        return cal.getTimeInMillis();
        DateTime dateTime = new DateTime(2017,10,29,23,59,59);
        return dateTime.plusWeeks(week-1).getMillis();
    }
    public static long getWeekStartTimestamp(){
        return getWeekStartTimestamp(getCurrentWeek());
    }

    public static long getWeekEndTimestamp(){
        return   getWeekEndTimestamp(getCurrentWeek());
    }

    public static void main(String[] args) {
        System.out.println(getWeekStartTimestamp());
    }

}
