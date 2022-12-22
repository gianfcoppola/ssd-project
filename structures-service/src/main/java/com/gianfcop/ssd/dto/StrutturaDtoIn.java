package com.gianfcop.ssd.dto;

import lombok.Data;

@Data
public class StrutturaDtoIn {

    private int id;
    private String descrizione;
    private int prezzoPrenotazione;
    private int prezzoAbbonamentoMensile;
}
