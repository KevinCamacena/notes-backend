package com.notesapp.notes_api.note.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notesapp.notes_api.note.model.Note;
import com.notesapp.notes_api.note.repository.NoteRepository;

@Service
public class NoteService {
    @Autowired
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(Note note) {
        note.setCreatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> findNoteById(String id) {
        return noteRepository.findById(id);
    }

    public Optional<Note> updateNote(String id, Note noteUpdates) {
        return noteRepository.findById(id)
                .map(existingNote -> {
                    existingNote.setContent(noteUpdates.getContent());
                    existingNote.setUpdatedAt(LocalDateTime.now());
                    return noteRepository.save(existingNote);
                });
    }

}
