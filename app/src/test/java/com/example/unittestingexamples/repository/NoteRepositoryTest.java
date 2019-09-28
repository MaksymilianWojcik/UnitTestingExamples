package com.example.unittestingexamples.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unittestingexamples.models.Note;
import com.example.unittestingexamples.persistence.NoteDao;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static com.example.unittestingexamples.repository.NoteRepository.DELETE_FAILURE;
import static com.example.unittestingexamples.repository.NoteRepository.DELETE_SUCCESS;
import static com.example.unittestingexamples.repository.NoteRepository.INVALID_NOTE_ID;
import static com.example.unittestingexamples.repository.NoteRepository.NOTE_TITLE_NULL;
import static com.example.unittestingexamples.repository.NoteRepository.UPDATE_FAILURE;
import static com.example.unittestingexamples.repository.NoteRepository.UPDATE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(InstantExecutorExtension.class)
public class NoteRepositoryTest {


    private static final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);

    //system under test
    private NoteRepository noteRepository;

//    @Mock
    private NoteDao noteDao;

    @BeforeEach
    public void initEach(){
//        MockitoAnnotations.initMocks(this); // search the class for all @Mock and instantiate them and build mock
        // we can also forget about @Mock and do this manually:
        noteDao = mock(NoteDao.class);
        noteRepository = new NoteRepository(noteDao);
    }

    // Insert note, verify the correct method is called, confirm observer is triggered,
    // confirm the new rows are inserted
    @Test
    void insertNote_returnRow() throws Exception {

        // Arrange
        final Long insertedRow = 1L; // succesfull response
        final Single<Long> returnedData = Single.just(insertedRow);

        // what we want mockdao to do: (so we mock here)
        // what the insert method is called in dao, and any note object is inserted, than we want to retur
        // so this is defining what we want mock to do when i try to insert note object into db
        // so we define what we want to happen on the dao, which is the mock
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // ACT
        // this is gonna try and insert note into db
        // if i would use LiveData instead ofrxjava like in instrumentation tets, i wouldneed to add something for allowing to test in bacgkround, likw i did in instrumentation tests
        final Resource<Integer> returnedValue = noteRepository.insertNote(NOTE1).blockingFirst(); // blocking cause we return flowable

        //  Assert
        verify(noteDao).insertNote(any(Note.class)); // veryfing that the insertNote method was called with any Note object on dao
        verifyNoMoreInteractions(noteDao); // it says make sure the insdertNote method was called and after that nothing

        System.out.println("Returned value: " + returnedValue.data);
        assertEquals(Resource.success(1, NoteRepository.INSERT_SUCCESS), returnedValue);


        // or test using rxjava eexcept the whole code above
//        noteRepository.insertNote(NOTE1)
//                .test()
//                .await()
//                .assertValue(Resource.success(1, NoteRepository.INSERT_SUCCESS));

    }

    // Insert note, Failure (return -1)
    @Test
    void insertNote_returnError() throws Exception {

        final Long failedInsert = -1L;
        final Single<Long> returnedData = Single.just(failedInsert);

        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);
        final Resource<Integer> returnedValue = noteRepository.insertNote(NOTE1).blockingFirst();

        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.error(null, NoteRepository.INSERT_FAILURE), returnedValue);
    }

    // Insert note, null title, confirm throw exception
    @Test
    void insertNote_nullTitle_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.insertNote(note);
            }
        });

        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    // update, verify correct method is called, confirm observer is trigger (flowable), confirm number of rows updated (1)
    @Test
    void updateNote_returnNumRowsUpdated() throws Exception {
        // Arrange
        final int updatedRow = 1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(updatedRow));

        // Act
        final Resource<Integer> returnedValue = noteRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }

    // update note, return failure (-1)
    @Test
    void updateNote_returnFailure() throws Exception {
        // Arrange
        final int failedInsert = -1;
        final Single<Integer> returnedData = Single.just(failedInsert);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);
//      or  when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(failedInsert));

        // Act
        final Resource<Integer> returnedValue = noteRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.error(null, UPDATE_FAILURE), returnedValue);
    }

    // update note, null title, throw exception
    @Test
    void updateNote_nullTitle_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.updateNote(note);
            }
        });

        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    // delete note, null id, throw exzception (INVALID_NOTE_ID_EXCEPTION
    @Test
    void deleteNote_nullId_thowException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setId(-1);
                noteRepository.deleteNote(note);
            }
        });
        assertEquals(INVALID_NOTE_ID, exception.getMessage());
    }

    // delete note, delete success, return Resource.success with deleted row
    @Test
    void deleteNote_success_returnRow() throws Exception {
        final int deletedRow = 1;
        Resource<Integer> successResponse = Resource.success(deletedRow, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // act
        Resource<Integer> observedResponse= liveDataTestUtil.getValue(noteRepository.deleteNote(NOTE1));
        assertEquals(successResponse, observedResponse);
    }

    // delete note, delete failure, return Resource.error
    @Test
    void deleteNote_failure_returnError() throws Exception {
        final int deletedRow = -1;
        Resource<Integer> errorResponse = Resource.error(null, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // act
        Resource<Integer> observedResponse= liveDataTestUtil.getValue(noteRepository.deleteNote(NOTE1));

        // assert
        assertEquals(errorResponse, observedResponse);
    }

    // retrieve notes, return list of notes
    @Test
    void getNotes_returnListWithNotes() throws Exception {
        // Arrange
        List<Note> notes = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }

    // retrieve notes, return empty list
    @Test
    void getNotes_returnEmptyList() throws Exception {
        // Arrange
        List<Note> notes = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }

}
