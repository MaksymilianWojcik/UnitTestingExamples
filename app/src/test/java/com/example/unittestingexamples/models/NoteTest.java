package com.example.unittestingexamples.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NoteTest {

    public static final String TIMESTAMP_1 = "05-2019";
    public static final String TIMESTAMP_2 = "04-2019";

    @Test
    void isNotesEqual_identicalProperties_returnTrue() throws Exception {
        Note note1 = new Note(1, "Note #1", "This is note #1", TIMESTAMP_1);
        Note note2 = new Note(1, "Note #1", "This is note #1", TIMESTAMP_1);

        Assertions.assertEquals(note1, note2);
        System.out.println("The notes are equal!");
    }

    @Test
    void isNotesEqual_differentIds_returnFalse() throws Exception {
        Note note1 = new Note(1, "Note #1", "This is note #1", TIMESTAMP_1);
        Note note2 = new Note(2, "Note #1", "This is note #1", TIMESTAMP_1);

        Assertions.assertNotEquals(note1, note2);
        System.out.println("The notes are not equal!");
    }

    @Test
    void isNotesEqual_differentTimestamps_returnTrue() throws Exception {
        Note note1 = new Note(1, "Note #1", "This is note #1", TIMESTAMP_1);
        Note note2 = new Note(1, "Note #1", "This is note #1", TIMESTAMP_2);

        Assertions.assertEquals(note1, note2);
        System.out.println("The notes are not equal!");
    }

    @Test
    void isNotesEqual_differentTitles_returnFalse() throws Exception {
        Note note1 = new Note(1, "Note #1", "This is note #1", TIMESTAMP_1);
        Note note2 = new Note(1, "Note #2", "This is note #1", TIMESTAMP_1);

        Assertions.assertNotEquals(note1, note2);
        System.out.println("The notes are not equal!");
    }

    @Test
    void isNotesEqual_differentContent_returnFalse() throws Exception {
        Note note1 = new Note(1, "Note #1", "This is note #1", TIMESTAMP_1);
        Note note2 = new Note(1, "Note #2", "This is note #2", TIMESTAMP_1);

        Assertions.assertNotEquals(note1, note2);
        System.out.println("The notes are not equal!");
    }
}
