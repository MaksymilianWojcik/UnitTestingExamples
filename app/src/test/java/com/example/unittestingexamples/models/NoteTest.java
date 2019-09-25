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
}
