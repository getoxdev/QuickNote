package com.quicknote.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {NoteEntity.class},version = 2)
@TypeConverters(DateConverter.class)
public abstract class NotesDatabase extends RoomDatabase {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Notes "
                    + " ADD COLUMN title TEXT");
        }
    };

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
                    instance = Room.databaseBuilder(context.getApplicationContext(),NotesDatabase.class,DATABASE_NAME).addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return instance;
    }
}
