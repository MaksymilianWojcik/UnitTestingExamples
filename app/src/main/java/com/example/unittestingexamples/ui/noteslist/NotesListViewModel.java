package com.example.unittestingexamples.ui.noteslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.unittestingexamples.models.Note;
import com.example.unittestingexamples.repository.NoteRepository;
import com.example.unittestingexamples.ui.Resource;

import java.util.List;

import javax.inject.Inject;

public class NotesListViewModel extends ViewModel {

    private final NoteRepository noteRepository;

    private MediatorLiveData<List<Note>> notes = new MediatorLiveData<>();

    @Inject
    public NotesListViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<Resource<Integer>> deleteNote(final Note note) throws Exception {
        return noteRepository.deleteNote(note);
    }

    public LiveData<List<Note>> observeNotes() {
        return notes;
    }

    public void getNotes() { // triggering the actual quering for those notes
        final LiveData<List<Note>> source = noteRepository.getNotes();
        notes.addSource(source, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notesList) {
                if(notesList != null) {
                    notes.setValue(notesList);
                }
                notes.removeSource(source);
            }
        });
    }
}
