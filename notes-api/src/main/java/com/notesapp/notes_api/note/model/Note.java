package com.notesapp.notes_api.note.model;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Document(collection = "notes")
@Data
public class Note {

    @Id
    @Schema(description = "Unique identifier of the Note", example = "64b7f8c2e4b0c5a1d2f3e4b5")
    private String id;

    @Schema(description = "Contenido principal de la nota en texto plano")
    private String content;

    @Schema(description = "Fecha y hora de creación de la nota")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización de la nota")
    private LocalDateTime updatedAt;

    @Schema(description = "Contenido enriquecido de la nota.", example = "<p>Este es un <strong>ejemplo</strong> de nota con formato HTML.</p>")
    private String body;

    @Schema(description = "Objeto JSON como string que contiene datos de color de la nota.", 
            example = "{\"id\":\"color-purple\",\"colorHeader\":\"#FED0FD\",\"colorBody\":\"#FEE5FD\",\"colorText\":\"#18181A\"}")
    private String color;

    @Schema(description = "Objeto JSON como string que contiene las coordenadas X/Y de la nota.", 
            example = "{\"x\":163,\"y\":273}")
    private String position;
}
