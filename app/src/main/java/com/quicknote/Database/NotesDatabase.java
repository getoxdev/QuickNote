package com.quicknote.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {NoteEntity.class},version = 1)
@TypeConverters(DateConverter.class)
public abstract class NotesDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "QuickNote.db";

    public static volatile NotesDatabase instance;

    private static final Object object = new Object();

    public abstract NotesDao notesDao();

    public static NotesDatabase getInstance(Context context)
    {
        if(instance==null)
        {
            synchronized (object)
            {
                if(instance==null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(),NotesDatabase.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
