package com.example.unittestingexamples.repository;

import com.example.unittestingexamples.persistence.NoteDao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteRepositoryTest {

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

    @Test
    void dummyTest() throws Exception {
        assertNotNull(noteDao);
        assertNotNull(noteRepository);
    }
}
