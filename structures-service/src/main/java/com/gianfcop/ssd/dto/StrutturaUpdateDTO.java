package com.gianfcop.ssd.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StrutturaUpdateDTO {

    private int id;
    private String descrizione;
    private int prezzoPrenotazione;
    private String prezzoAbbonamentoMensile;
    
}
