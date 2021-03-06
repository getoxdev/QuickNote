package com.quicknote.Database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotesRepo {

    private static NotesRepo instance;
    private NotesDatabase notesDatabase;

    public LiveData<List<NoteEntity>> mNotesList;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public static NotesRepo getInstance(Context context)
    {
        return instance = new NotesRepo(context);
    }

    private NotesRepo(Context context)
    {
        notesDatabase = NotesDatabase.getInstance(context);
        mNotesList = getNotes();
    }

    private LiveData<List<NoteEntity>> getNotes()
    {
        return notesDatabase.notesDao().getAllNotes();
    }

    public void deleteAllData()
    {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notesDatabase.notesDao().deleteAllNotes();
            }
        });
    }

    public void insertNote(final NoteEntity noteEntity)
    {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notesDatabase.notesDao().insertNote(noteEntity);
            }
        });
    }

    public NoteEntity loadNote(int noteID)
    {
        return notesDatabase.notesDao().getNoteByID(noteID);
    }

    public void deleteNote(NoteEntity noteEntity)
    {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notesDatabase.notesDao().delete(noteEntity);
            }
        });
    }
}
