package com.zhenyu.zhenyu.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateControl {
    private Calendar calendar;
    private SimpleDateFormat sdf;
    public DateControl(){
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        calendar.setTime(new Date());
    }
    public String getFormatDate(){
        return sdf.format(calendar.getTime());
    }
    public void setTime(Date date){
        calendar.setTime(date);
    }
    public void resetDate(){
        calendar.setTime(new Date());
    }
    public String backday(){
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
        return getFormatDate();
    }
    public String backhour(){
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)-1);
        return getFormatDate();
    }
    public String addhour(){
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+1);
        return getFormatDate();
    }
    public String addday(){
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        return getFormatDate();
    }
}
