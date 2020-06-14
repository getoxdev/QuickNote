package com.quicknote.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity noteEntity);

    @Delete()
    void delete(NoteEntity noteEntity);

    @Query("SELECT * FROM Notes WHERE ID = :ID")
    NoteEntity getNoteByID(int ID);

    @Query("SELECT * FROM Notes ORDER BY date DESC")
    LiveData<List<NoteEntity>> getAllNotes();

    @Query("DELETE FROM Notes")
    int deleteAllNotes();

    @Query("SELECT COUNT(*) FROM Notes")
    int getNotesCount();
}
