package com.example.unittestingexamples.repository;

import androidx.annotation.NonNull;

import com.example.unittestingexamples.persistence.NoteDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NoteRepository {

    @NonNull
    private final NoteDao noteDao;

    @Inject
    public NoteRepository(@NonNull NoteDao noteDao){
        this.noteDao = noteDao;
    }
}
