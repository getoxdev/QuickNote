package com.quicknote.Database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Notes")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    private Date date;
    private String text;
    private String title;

    @Ignore
    public NoteEntity() {
    }

    public NoteEntity(Date date, String text, String title) {
        this.date = date;
        this.text = text;
        this.title = title;
    }

    @Ignore
    public NoteEntity(int ID, Date date, String text, String title) {
        this.ID = ID;
        this.date = date;
        this.text = text;
        this.title = title;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
