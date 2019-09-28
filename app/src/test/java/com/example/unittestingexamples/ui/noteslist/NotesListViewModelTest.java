package com.example.unittestingexamples.ui.noteslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unittestingexamples.models.Note;
import com.example.unittestingexamples.repository.NoteRepository;
import com.example.unittestingexamples.ui.Resource;
import com.example.unittestingexamples.utils.InstantExecutorExtension;
import com.example.unittestingexamples.utils.LiveDataTestUtil;
import com.example.unittestingexamples.utils.TestUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.example.unittestingexamples.repository.NoteRepository.DELETE_FAILURE;
import static com.example.unittestingexamples.repository.NoteRepository.DELETE_SUCCESS;
import static org.mockito.Mockito.*;

@ExtendWith(InstantExecutorExtension.class)
public class NotesListViewModelTest {

    private NotesListViewModel viewModel;

    @Mock
    private NoteRepository noteRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        viewModel = new NotesListViewModel(noteRepository);
    }

    // retrieve a list of notes, observe list, return list
    @Test
    public void retrieveNotes_returnNotesList() throws Exception {

        List<Note> returnedData = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);


        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        Assertions.assertEquals(returnedData, observedData);
    }

    // retrieve list of notes, observe list, return empty list
    @Test
    public void retrieveNotes_returnEmptyList() throws Exception {

        List<Note> returnedData = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);


        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        Assertions.assertEquals(returnedData, observedData);
    }

    // delte note, observe Resource.success, return Resrouce.success
    @Test
    public void deleteNote_returnSuccess() throws Exception {

        Note deleteNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.success(1, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);


        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deleteNote));

        Assertions.assertEquals(returnedData, observedValue);
    }

    // delete note, observer Resoruce.error, return Resource.error
    @Test
    public void deleteNote_returnError() throws Exception {

        Note deleteNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.error(1, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);

        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deleteNote));

        Assertions.assertEquals(returnedData, observedValue);
    }
}
