package com.example.unittestingexamples.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.example.unittestingexamples.models.Note;
import com.example.unittestingexamples.repository.NoteRepository;
import com.example.unittestingexamples.ui.Resource;

import javax.inject.Inject;

public class NoteViewModel {

    private static final String TAG = "NoteViewModel";

    private final NoteRepository noteRepository;

    private MutableLiveData<Note> note = new MutableLiveData<>();

    @Inject
    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<Resource<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.insertNote(note.getValue()) //refernece to the one in livedata
        );
    }

//    public LiveData<Resource<Integer>> updateNote() throws Exception {
//
//    }
//
//    public LiveData<Resource<Integer>> saveNote() throws Exception {
//        // logic if to insert or update note
//    }

    public LiveData<Note> observeNote() {
        return note;
    }

    public void setNote(Note note) throws Exception {
        if (note.getTitle() == null || note.getTitle().equals("")) {
            throw new Exception(NoteRepository.NOTE_TITLE_NULL);
        }
        this.note.setValue(note);
    }
}
