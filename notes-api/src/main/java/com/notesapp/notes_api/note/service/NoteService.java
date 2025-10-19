package com.notesapp.notes_api.note.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.notesapp.notes_api.note.model.Note;
import com.notesapp.notes_api.note.repository.NoteRepository;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(Note note) {
        note.setCreatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

}
