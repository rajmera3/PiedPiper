package com.piedpiper;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
/**
 * Created by vamseegangaram on 11/19/17.
 * Tests the setDate inner class from SightingsMapActivity and LoginActivityTest
 */

public class SetDateTest {

    private final int offset = 1900;
    private Date start = new Date(2015 - offset, 1, 1);
    private Date end = new Date(2017 - offset, 1, 1);
    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        int mYear;
        int mMonth;
        int mDay;
        final int date;
        // if date is 0, then we are updating the start date, else we are updating the end variable
        public mDateSetListener(int date) {
            this.date = date;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // getCalender();
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
//            String add = mMonth + "/" + mDay + "/" + mYear + " 00:00:00 0";
            if (date == 0) {
                start = new Date(mYear - offset, mMonth, mDay);
            } else {
                end = new Date(mYear - offset, mMonth, mDay);
            }
//            updateMap();
//            date.setYear(mYear);
//            date.setMonth(mMonth);
//            date.setDate(mDay);
            //updateMap();
        }

        public void onDateSetTest(int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (date == 0) {
                start = new Date(mYear - offset, mMonth, mDay);
            } else {
                end = new Date(mYear - offset, mMonth, mDay);
            }
        }
    }
    @Test
    public void setStartDate() throws Exception {
        mDateSetListener date = new mDateSetListener(0);
        date.onDateSetTest(2016, 12, 2);
        assertEquals(start, new Date(2016 - offset, 12, 2));
        assertEquals(end, new Date(2017 - offset, 1, 1));
    }

    @Test
    public void setEndDate() throws Exception {
        mDateSetListener date = new mDateSetListener(1);
        date.onDateSetTest(2018, 11, 3);
        assertEquals(start, new Date(2015 - offset, 1, 1));
        assertEquals(end, new Date(2018 - offset, 11, 3));

        date = new mDateSetListener(-1);
        date.onDateSetTest(2018, 11, 3);
        assertEquals(start, new Date(2015 - offset, 1, 1));
        assertEquals(end, new Date(2018 - offset, 11, 3));
    }

}

