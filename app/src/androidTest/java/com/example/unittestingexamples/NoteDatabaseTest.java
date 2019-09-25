package com.example.unittestingexamples;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.unittestingexamples.persistence.NoteDao;
import com.example.unittestingexamples.persistence.NoteDatabase;

import org.junit.After;
import org.junit.Before;

public abstract class NoteDatabaseTest {

    // system under test
    private NoteDatabase noteDatabase;

    public NoteDao getNoteDao(){
        return noteDatabase.getNoteDao();
    }

    @Before
    public void init() {
        noteDatabase = Room.inMemoryDatabaseBuilder( // this will mock database for testing, it will construct database that will exist as long as the app is alive
                ApplicationProvider.getApplicationContext(), // ApplicationProvider is a way we can get a reference to android framework within tests
                NoteDatabase.class
        ).build(); // so this is our temporary databse
    }

    @After // called after each test
    public void finish() {
        noteDatabase.close();
    }

}
