package com.gianfcop.ssd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Strutture")
public class Struttura {

    @Id
    private int id;
    private String descrizione;
    private int prezzoPrenotazione;
    private int prezzoAbbonamentoMensile;

}
