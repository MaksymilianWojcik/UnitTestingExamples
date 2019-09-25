package com.example.unittestingexamples.di;

import android.app.Application;

import androidx.room.Room;

import com.example.unittestingexamples.persistence.NoteDao;
import com.example.unittestingexamples.persistence.NoteDatabase;
import com.example.unittestingexamples.repository.NoteRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

    @Provides
    @Singleton
    static NoteDatabase provideNoteDatabase(Application application){
        return Room.databaseBuilder(application, NoteDatabase.class, NoteDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    static NoteDao provideNoteDao(NoteDatabase noteDatabase) {
        return noteDatabase.getNoteDao();
    }

    @Singleton
    @Provides
    static NoteRepository provideNoteRepository(NoteDao noteDao) {
        return new NoteRepository(noteDao);
    }
}
