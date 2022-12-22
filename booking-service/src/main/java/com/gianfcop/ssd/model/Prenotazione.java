package com.gianfcop.ssd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Prenotazione")
public class Prenotazione {

    @Transient
    public static final String SEQUENCE_NAME = "prenotazioni_sequence";

    @Id
    private int id;

    private String timestampPrenotazione;

    private String giorno;
    private String oraInizio;
    private String oraFine;
    private String idUtente;
    private int idStruttura;
    
}
