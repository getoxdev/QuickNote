package com.quicknote.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quicknote.Database.NoteEntity;
import com.quicknote.Database.NotesRepo;
import com.quicknote.Utils.SampleData;

import java.util.List;

public class ListViewModel extends AndroidViewModel {

    public LiveData<List<NoteEntity>> mNotesList;
    private NotesRepo notesRepo;

    public ListViewModel(@NonNull Application application) {
        super(application);
        notesRepo = NotesRepo.getInstance(application.getApplicationContext());
        mNotesList = notesRepo.mNotesList;
    }

    public void addSampleData()
    {
        notesRepo.addSampleData();
    }

    public void deleteAllData()
    {
        notesRepo.deleteAllData();
    }

    public void deleteNote(NoteEntity noteEntity) {
        notesRepo.deleteNote(noteEntity);
    }
}
