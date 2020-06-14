package com.quicknote.Utils;

import com.quicknote.Database.NoteEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleData {

    private static final String TEXT_1 = "Note 1 by Rishi";
    private static final String TEXT_2 = "Note 2";
    private static final String TEXT_3 = "Note 3";

    private static Date getDate(int diffAmount)
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(Calendar.MILLISECOND,diffAmount);

        return gregorianCalendar.getTime();
    }

    public static List<NoteEntity> getSampleData()
    {
        List<NoteEntity> notesList = new ArrayList<>();

        notesList.add(new NoteEntity(getDate(0),TEXT_1));
        notesList.add(new NoteEntity(getDate(-1),TEXT_2));
        notesList.add(new NoteEntity(getDate(-2),TEXT_3));

        return notesList;
    }
}
