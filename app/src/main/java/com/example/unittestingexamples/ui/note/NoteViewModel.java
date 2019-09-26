package com.example.unittestingexamples.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unittestingexamples.models.Note;
import com.example.unittestingexamples.repository.NoteRepository;
import com.example.unittestingexamples.ui.Resource;
import com.example.unittestingexamples.utils.DateUtil;

import javax.inject.Inject;

public class NoteViewModel extends ViewModel {

    private static final String TAG = "NoteViewModel";

    public enum ViewState {VIEW, EDIT}

    private final NoteRepository noteRepository;

    private MutableLiveData<Note> note = new MutableLiveData<>();
    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    private boolean isNewNote;

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

    public LiveData<ViewState> observeViewState() {
        return viewState;
    }

    public void setViewState(ViewState viewState) {
        this.viewState.setValue(viewState);
    }

    public void setIsNewNote(boolean isNewNote) {
        this.isNewNote = isNewNote;
    }

    public LiveData<Resource<Integer>> saveNote() {
        return null; // here logic to decide if should insert note or update note
    }

    /***
     * this is for updating properties of the note that is saved inside viewmodel
     */
    public void updateNote(String title, String content) throws Exception {
        if(title == null || title.equals("")){
            throw new NullPointerException("Title can't be null");
        }
        String temp = removeWhiteSpace(content);
        if(temp.length() > 0){
            Note updatedNote = new Note(note.getValue());
            updatedNote.setTitle(title);
            updatedNote.setContent(content);
            updatedNote.setTimestamp(DateUtil.getCurrentTimeStamp());

            note.setValue(updatedNote);
        }
    }

    private String removeWhiteSpace(String string){
        string = string.replace("\n", "");
        string = string.replace(" ", "");
        return string;
    }

    public void setNote(Note note) throws Exception {
        if (note.getTitle() == null || note.getTitle().equals("")) {
            throw new Exception(NoteRepository.NOTE_TITLE_NULL);
        }
        this.note.setValue(note);
    }

    public boolean shouldNavigateBack(){
        if(viewState.getValue() == ViewState.VIEW){
            return true;
        }
        else{
            return false;
        }
    }
}
