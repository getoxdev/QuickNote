package com.quicknote.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quicknote.Database.NoteEntity;
import com.quicknote.Database.NotesDao;
import com.quicknote.Database.NotesDatabase;
import com.quicknote.Database.NotesRepo;

import java.util.List;

public class ListViewModel extends AndroidViewModel {

    public LiveData<List<NoteEntity>> mNotesList;
    private NotesRepo notesRepo;
    private NotesDao notesDao;
    private NotesDatabase database;

    private LiveData<List<NoteEntity>> mAllNotes;

    public ListViewModel(@NonNull Application application) {
        super(application);
        notesRepo = NotesRepo.getInstance(application.getApplicationContext());
        mNotesList = notesRepo.mNotesList;
        database = NotesDatabase.getInstance(getApplication());
        notesDao = database.notesDao();

        mAllNotes = notesDao.getAllNotes();


    }

    public void deleteAllData()
    {
        notesRepo.deleteAllData();
    }

    public void deleteNote(NoteEntity noteEntity) {
        notesRepo.deleteNote(noteEntity);
    }

    //method to get all notes as live data from the database
    public LiveData<List<NoteEntity>> getAllNotes(){
        return mAllNotes;

    }


}
