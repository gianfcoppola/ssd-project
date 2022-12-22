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
@Document(collection = "Centro Sportivo")
public class CentroSportivo {

    @Transient
    public static final String SEQUENCE_NAME = "cs_sequence";

    @Id
    private int id;

    //prenotazioni: calcio + tennis
    private int numeroPrenotazioniCalcio;
    private int numeroPrenotazioniTennis;
    private int numeroPrenotazioniPalestra;
    private int numeroPrenotazioniPiscina;
    //abbonamenti: piscina + tennis
    private int numeroAbbonamentiPiscina;   
    private int numeroAbbonamentiPalestra;
    
    private int incassiCalcio;
    private int incassiTennis;
    private int incassiPalestra;
    private int incassiPiscina;

    

}
