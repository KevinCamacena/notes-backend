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
                    // Actualizamos todos los campos que nos lleguen
                    existingNote.setContent(noteUpdates.getContent());
                    existingNote.setBody(noteUpdates.getBody());
                    existingNote.setColor(noteUpdates.getColor());
                    existingNote.setPosition(noteUpdates.getPosition());
                    // (Aquí también podríamos setear un 'updatedAt = LocalDateTime.now()')
                    
                    return noteRepository.save(existingNote);
                });
    }

    public boolean deleteNoteById(String id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
