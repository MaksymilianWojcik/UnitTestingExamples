package com.example.unittestingexamples.persistence;


import androidx.room.Database;

@Database(entities = { NoteDao.class}, version = 1)
public abstract class NoteDatabase {

    public static final String DATABASE_NAME = "notes_db";

    public abstract NoteDao getNoteDao();
}
