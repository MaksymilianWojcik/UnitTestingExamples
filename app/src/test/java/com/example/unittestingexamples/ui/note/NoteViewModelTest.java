package com.example.unittestingexamples.ui.note;

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
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.internal.operators.single.SingleToFlowable;

import static com.example.unittestingexamples.ui.note.NoteViewModel.NO_CONTENT_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(InstantExecutorExtension.class) // to work with background tests on juni5 - here we usel ivedata instead of rxjava
public class NoteViewModelTest {


    // under tests:
    private NoteViewModel noteViewModel;

    // mock: (another way fro mocking)
    @Mock
    private NoteRepository noteRepository; // we need it to insantaite NoteViewModel

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        noteViewModel = new NoteViewModel(noteRepository);
    }


    // cant observe a note that hasnt been set
    @Test
    void observeNote_whenNotSet() throws Exception {
        // Arrange
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<Note>();

        // Act
        Note note = liveDataTestUtil.getValue(noteViewModel.observeNote());

        // Assert
        assertNull(note);
    }

    // observe a note has been set and onChanged will trigger in activity
    @Test
    void observeNote_whenSet() throws Exception {
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<Note>();

        Note note = new Note(TestUtil.TEST_NOTE_1);
        noteViewModel.setNote(note);
        Note observedNote = liveDataTestUtil.getValue(noteViewModel.observeNote());

        assertEquals(note, observedNote);
    }

    // insert a new note and observe row returned
    @Test
    void insertNote_returnRow() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int insertedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(insertedRow, NoteRepository.INSERT_SUCCESS));
        when(noteRepository.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(true);
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(noteViewModel.saveNote()); // gives us note object

        // Assert
        assertEquals(Resource.success(insertedRow, NoteRepository.INSERT_SUCCESS), returnedValue);
    }

    // insert: dont return a new row without observer
    @Test
    void insertNote_withoutObserver_dontReturnRow() throws Exception {
        // arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // act
        noteViewModel.setNote(note);

        // assert
        verify(noteRepository, never()).insertNote(note); // noterepository never used and its method insert note
    }

    // set note, null title, throw exception
    @Test
    void setNote_withNullTitle_throwException() throws Exception {
        // Arrange
        final Note note = TestUtil.TEST_NOTE_1;
        note.setTitle(null);

        // Assert
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                // Act
                noteViewModel.setNote(note);
            }
        });
    }


    // update note and observe row returned
    @Test
    void updateNote_returnRow() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int updatedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(updatedRow, NoteRepository.UPDATE_SUCCESS));
        when(noteRepository.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(false);
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(noteViewModel.saveNote());

        // Assert
        assertEquals(Resource.success(updatedRow, NoteRepository.UPDATE_SUCCESS), returnedValue);
    }

    // dont return a new row without observer
    @Test
    void dontReturnUpdateRow_withoutObserver_dontReturnRow() throws Exception {
        // arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // act
        noteViewModel.setNote(note);

        // assert
        verify(noteRepository, never()).updateNote(note);
    }


    // testing the exception that save failed
    @Test
    void saveNote_shouldAllowSave_returnFalse() throws Exception {
        Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setContent(null);

        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(true);

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                noteViewModel.saveNote();
            }
        });

        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
    }

}
