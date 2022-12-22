package com.gianfcop.ssd.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NumeroPrenotazioniDTO {
    
    private int numeroPrenotazioniCalcio;
    private int numeroPrenotazioniTennis;
    private int numeroPrenotazioniPiscina;
    private int numeroPrenotazioniPalestra;
}
