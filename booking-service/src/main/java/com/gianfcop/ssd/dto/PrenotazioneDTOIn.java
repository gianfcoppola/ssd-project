package com.gianfcop.ssd.dto;

import lombok.Data;

@Data
public class PrenotazioneDTOIn {

    
    private String giorno;
    private String oraInizio;
    private String oraFine;
    private String idUtente;
    private int idStruttura;
    
}
