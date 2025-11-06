package com.notesapp.notes_api.note.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.notesapp.notes_api.note.repository.NoteRepository;
import com.notesapp.notes_api.note.service.NoteService;
import com.notesapp.notes_api.note.model.Note;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteRepository.deleteAll();
    }


    @Test
    @DirtiesContext
    void shouldCreateNote() throws Exception {
        String newNoteJson = "{\"content\": \"This is a test note.\"}";

        mockMvc.perform(post("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newNoteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(emptyString())))
                .andExpect(jsonPath("$.content").value("This is a test note."));
    }

    @Test
    @DirtiesContext
    void shouldReturnAllNotes() throws Exception {
        String newNoteJson = "{\"content\": \"This is a test note for get all.\"}";

        mockMvc.perform(post("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newNoteJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content").value("This is a test note for get all."));
    }

    @Test
    void shouldReturnOneNoteById() throws Exception {
        Note noteToSave = new Note();
        noteToSave.setContent("This is a the specific note we want!.");
        Note savedNote = noteService.createNote(noteToSave);

        String savedId = savedNote.getId();

        mockMvc.perform(get("/api/v1/notes/" + savedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.content").value("This is a the specific note we want!."));
    }

    @Test
    void shouldUpdateNoteById() throws Exception {
        Note noteToSave = new Note();
        noteToSave.setContent("This is a note to be updated.");
        Note savedNote = noteService.createNote(noteToSave);
        String savedId = savedNote.getId();

        String updatedNoteJson = "{\"content\": \"This note has been updated.\"}";

        mockMvc.perform(put("/api/v1/notes/" + savedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedNoteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.content").value("This note has been updated."));
    }

    @Test
    void shouldDeleteNoteById() throws Exception {
        Note noteToSave = new Note();
        noteToSave.setContent("This is a note to be deleted.");
        Note savedNote = noteService.createNote(noteToSave);
        String savedId = savedNote.getId();

        mockMvc.perform(delete("/api/v1/notes/" + savedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/notes/" + savedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewNote() throws Exception {
        // --- JSON AHORA MÁS COMPLETO ---
        String newNoteJson = """
            {
                "content": "Contenido antiguo (opcional)",
                "body": "<p>¡Este es el cuerpo de la nota!</p>",
                "color": "{\\"id\\":\\"color-purple\\",\\"colorHeader\\":\\"#FED0FD\\",\\"colorBody\\":\\"#FEE5FD\\",\\"colorText\\":\\"#18181A\\"}",
                "position": "{\\"x\\":163,\\"y\\":273}"
            }
            """;

        mockMvc.perform(post("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newNoteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(emptyString())))
                // --- NUEVAS AFIRMACIONES ---
                .andExpect(jsonPath("$.body", is("<p>¡Este es el cuerpo de la nota!</p>")))
                .andExpect(jsonPath("$.color", is("{\"id\":\"color-purple\",\"colorHeader\":\"#FED0FD\",\"colorBody\":\"#FEE5FD\",\"colorText\":\"#18181A\"}")))
                .andExpect(jsonPath("$.position", is("{\"x\":163,\"y\":273}")));
    }

}