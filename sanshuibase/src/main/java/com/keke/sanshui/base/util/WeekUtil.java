package com.keke.sanshui.base.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class WeekUtil {

    public static int getCurrentWeek(String dataStr){
      Calendar calendar = Calendar.getInstance();
      calendar.set(2017, 10, 25,0, 0, 0);
      calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println("dataStr = [" + calendar.getTime() + "]");

      return 0;
    }

    public static void main(String[] args) {
        getCurrentWeek("0000");
    }
}
