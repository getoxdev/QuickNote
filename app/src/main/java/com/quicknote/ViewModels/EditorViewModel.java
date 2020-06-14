package com.quicknote.ViewModels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.quicknote.Database.NoteEntity;
import com.quicknote.Database.NotesRepo;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<NoteEntity> liveNote = new MutableLiveData<>();
    private NotesRepo notesRepo;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);

        notesRepo = NotesRepo.getInstance(application.getApplicationContext());
    }

    public void loadNote(final int noteID)
    {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity noteEntity = notesRepo.loadNote(noteID);
                liveNote.postValue(noteEntity);
            }
        });
    }

    public void saveAndExit(String noteText)
    {
        NoteEntity noteEntity = liveNote.getValue();

        if(noteEntity==null)
        {
            if(TextUtils.isEmpty(noteText.trim()))
            {
                return;
            }
            else
            {
                noteEntity = new NoteEntity(new Date(),noteText.trim());
            }
        }
        else
        {
            noteEntity.setText(noteText.trim());
            noteEntity.setDate(new Date());
        }

        notesRepo.insertNote(noteEntity);
    }

    public void deleteNote()
    {
        notesRepo.deleteNote(liveNote.getValue());
    }
}