package com.example.unittestingexamples;

import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.unittestingexamples.models.Note;
import com.example.unittestingexamples.utils.LiveDataTestUtil;
import com.example.unittestingexamples.utils.TestUtil;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NoteDaoTest extends NoteDatabaseTest {

    public static final String TEST_TITLE = "Test title";
    public static final String TEST_CONTENT = "Test content";
    public static final String TEST_TIMESTAMP = "08-2019";

    @Rule // to test the background work stuff in junit4. Without this 2 lines we get errors
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // Insert, Read, Delete
    @Test
    public void insertReadDelete() throws Exception {
        Note note = TestUtil.TEST_NOTE_1;

        // insert
        getNoteDao().insertNote(note).blockingGet(); // blocking get - cause we are using rx java, we call this to wait until inserted

        // read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());

        assertNotNull(insertedNotes);
        assertEquals(note.getContent(), insertedNotes.get(0).getContent());
        assertEquals(note.getTimestamp(), insertedNotes.get(0).getTimestamp());
        assertEquals(note.getTitle(), insertedNotes.get(0).getTitle());

        note.setId(insertedNotes.get(0).getId()); // inserted note has id, but our mock note doesnt
        assertEquals(note, insertedNotes.get(0));

        // delete
        getNoteDao().deleteNote(note).blockingGet(); // again, rxjava transaction so we want to wait

        // configrm the database is empty
        insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertEquals(0, insertedNotes.size());
    }

    // Insert, Read, Update, Read, Delete
    @Test
    public void insertReadUpdateReadDelete() throws Exception {
        Note note = TestUtil.TEST_NOTE_1;

        // insert
        getNoteDao().insertNote(note).blockingGet(); // blocking get - cause we are using rx java, we call this to wait until inserted

        // read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());

        assertNotNull(insertedNotes);
        assertEquals(note.getContent(), insertedNotes.get(0).getContent());
        assertEquals(note.getTimestamp(), insertedNotes.get(0).getTimestamp());
        assertEquals(note.getTitle(), insertedNotes.get(0).getTitle());

        note.setId(insertedNotes.get(0).getId()); // inserted note has id, but our mock note doesnt
        assertEquals(note, insertedNotes.get(0));


        // update
        note.setTitle(TEST_TITLE);
        note.setContent(TEST_CONTENT);
        note.setTimestamp(TEST_TIMESTAMP);
        getNoteDao().updateNote(note).blockingGet();

        // read
        insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes()); // now we get newly updated list of notes
        assertEquals(TEST_TITLE, insertedNotes.get(0).getTitle());
        assertEquals(TEST_CONTENT, insertedNotes.get(0).getContent());
        assertEquals(TEST_TIMESTAMP, insertedNotes.get(0).getTimestamp());

        note.setId(insertedNotes.get(0).getId());
        assertEquals(note, insertedNotes.get(0));

        // delete
        getNoteDao().deleteNote(note).blockingGet(); // again, rxjava transaction so we want to wait

        // configrm the database is empty
        insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertEquals(0, insertedNotes.size());
    }

    // Insert note with null title, throw exception
    @Test(expected = SQLiteConstraintException.class) // by this we tell the test method that we expect an exception to be thrown
    public void insertNullTitle_throwSQLiteConstaintException() throws Exception {
        Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);

        // insert
        getNoteDao().insertNote(note).blockingGet();
    }

    // Insert, Update with null title, throw exception
    @Test(expected = SQLiteConstraintException.class)
    public void updateNullTitle_throwsSqliteConstraintException() throws Exception {
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // insert
        getNoteDao().insertNote(note).blockingGet();

        // read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertNotNull(insertedNotes);

        // update
        note = new Note(insertedNotes.get(0));
        note.setTitle(null);
        getNoteDao().updateNote(note).blockingGet();
    }
}
