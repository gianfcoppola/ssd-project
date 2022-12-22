package com.gianfcop.ssd.mapper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import com.gianfcop.ssd.dto.AbbonamentoDTOIn;
import com.gianfcop.ssd.dto.AbbonamentoDTOOut;
import com.gianfcop.ssd.model.Abbonamento;

public class AbbonamentoMapper {

    public static Abbonamento toAbbonamento (AbbonamentoDTOIn abbonamentoDTOIn, int idAbbonamento, int prezzoMensileAbbonamento){

        String dataFineAbbonamento = abbonamentoDTOIn.getDataFineAbbonamento();
        String dataInizioAbbonamento = abbonamentoDTOIn.getDataInizioAbbonamento();
        
        Period diff = Period.between(LocalDate.parse(dataInizioAbbonamento), LocalDate.parse(dataFineAbbonamento));
        int numeroMesiAbbonamento = diff.getMonths() + 1;
        int prezzoAbbonamento = prezzoMensileAbbonamento * numeroMesiAbbonamento;

        Abbonamento abbonamento = Abbonamento.builder()
            .id(idAbbonamento)
            .idUtente(abbonamentoDTOIn.getIdUtente())
            .idStruttura(abbonamentoDTOIn.getIdStruttura())
            .dataInizioAbbonamento(LocalDate.parse(abbonamentoDTOIn.getDataInizioAbbonamento()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString())
            .dataFineAbbonamento(LocalDate.parse(abbonamentoDTOIn.getDataFineAbbonamento()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString())
            .prezzoTotale(prezzoAbbonamento)
            .timestampPrenotazione(new Timestamp(System.currentTimeMillis()).toString())
            .build();

        return abbonamento;
    }
    
    public static AbbonamentoDTOOut toAbbonamentoDTOOut(Abbonamento abbonamento, String struttura){

        return AbbonamentoDTOOut.builder()
            .dataFineAbbonamento(abbonamento.getDataFineAbbonamento())
            .dataInizioAbbonamento(abbonamento.getDataInizioAbbonamento())
            .idAbbonamento(abbonamento.getId())
            .numeroIngressiSettimana(3)
            .prezzoTotale(abbonamento.getPrezzoTotale())
            .struttura(struttura)
            .build();
    }
}
