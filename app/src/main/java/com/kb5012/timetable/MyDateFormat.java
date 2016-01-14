package com.kb5012.timetable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ronald on 14-1-2016.
 */
public class MyDateFormat extends SimpleDateFormat {

    private Date mDate;
    private Date currentDate;
    private final long ONEDAY = 86400000;
    private final long THREE = ONEDAY * 3;
    private String mFormat;

    public MyDateFormat(Date date) {
        this.mDate = date;
        Calendar mCaldendar = Calendar.getInstance();
        this.currentDate = mCaldendar.getTime();
        setFormat();
        super.applyLocalizedPattern(mFormat);
    }

    /*
     * set format to be used based on time difference
     * if within 24hrs. set format to hour:minutes in 24hr mode
     * if between 24hrs and 72hrs. set format to day-month
     * anything else set day-month hour:minutes in 24hr mode
     */
    private void setFormat() {
        long difference = mDate.getTime() - currentDate.getTime();

        if (difference >= 0 && difference <= ONEDAY) {
            mFormat = "HH:mm";
        }else if (difference >= THREE) {
            mFormat = "dd-MM";
        } else {
            mFormat = "dd-MM HH:mm";
        }
    }
}
