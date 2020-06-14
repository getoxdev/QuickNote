package com.quicknote.Database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Long toDateStamp(Date date)
    {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long dateStamp)
    {
        return dateStamp == null ? null : new Date(dateStamp);
    }
}
