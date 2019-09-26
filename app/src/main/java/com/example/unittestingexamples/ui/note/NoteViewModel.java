package com.example.unittestingexamples.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unittestingexamples.models.Note;
import com.example.unittestingexamples.repository.NoteRepository;
import com.example.unittestingexamples.ui.Resource;
import com.example.unittestingexamples.utils.DateUtil;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class NoteViewModel extends ViewModel {

    private static final String TAG = "NoteViewModel";
    public static final String NO_CONTENT_ERROR = "Can't save note with no content";

    public enum ViewState {VIEW, EDIT}

    private final NoteRepository noteRepository;

    private MutableLiveData<Note> note = new MutableLiveData<>();
    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    private boolean isNewNote;
    private Subscription updateSubscription;
    private Subscription inserSubscription;

    @Inject
    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<Resource<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.insertNote(note.getValue()) //refernece to the one in livedata
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        inserSubscription = subscription;
                    }
                })
        );
    }

    public LiveData<Resource<Integer>> updateNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.updateNote(note.getValue()) //refernece to the one in livedata
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                updateSubscription = updateSubscription;
                            }
                        })
        );
    }
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

    public LiveData<Resource<Integer>> saveNote() throws Exception {

        if (!shouldAllowSave()){
            throw new Exception(NO_CONTENT_ERROR);
        }

        cancelPendingTransactions();
        // here logic to decide if should insert note or update note

        return null;
    }

    private void cancelPendingTransactions(){
        if(inserSubscription != null) {
            cancelInsertTransaction();
        }
        if(inserSubscription != null) {
            cancelUpdateTransaction();
        }
    }

    private void cancelUpdateTransaction(){
        updateSubscription.cancel();
        updateSubscription = null;
    }

    private void cancelInsertTransaction(){
        inserSubscription.cancel();
        inserSubscription = null;
    }

    private boolean shouldAllowSave() {
        return removeWhiteSpace(note.getValue().getContent()).length() > 0;
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
