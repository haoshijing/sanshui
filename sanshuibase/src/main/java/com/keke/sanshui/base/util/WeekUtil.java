package com.keke.sanshui.base.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
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
        calendar.set(Calendar.DATE,22);
        calendar.set(Calendar.HOUR,0);
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

    public static long getWeekStartTimestamp(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getWeekEndTimestamp(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_MONTH, cal.get(Calendar.WEEK_OF_MONTH)+1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.WEEK_OF_MONTH,cal.get(Calendar.WEEK_OF_MONTH)+1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    public static void main(String[] args) {
        System.out.println(getWeekStartTimestamp());
    }

}
