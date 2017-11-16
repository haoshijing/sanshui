package com.keke.sanshui.base.util;

import java.util.Calendar;
import java.util.TimeZone;

public final class TimeUtil {

    public static final  Long getDayStartTimestamp(int day){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + day);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTimeInMillis();
    }

    public static final  Long getDayEndTimestamp(int day){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + day);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTimeInMillis();
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println(getDayStartTimestamp(-1));
        System.out.println(getDayEndTimestamp(-1));
    }
}
