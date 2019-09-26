package com.example.unittestingexamples.di;

import com.example.unittestingexamples.ui.note.NoteActivity;
import com.example.unittestingexamples.ui.noteslist.NotesListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract NotesListActivity contributeNotesListActivity();

    @ContributesAndroidInjector
    abstract NoteActivity contributeNoteActivity();
}
