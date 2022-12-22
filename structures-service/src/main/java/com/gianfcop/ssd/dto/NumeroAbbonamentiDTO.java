package com.gianfcop.ssd.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NumeroAbbonamentiDTO {
    
    private int numeroAbbonamentiPiscina;
    private int numeroAbbonamentiPalestra;
}
