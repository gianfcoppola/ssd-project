package com.gianfcop.ssd.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncassiDTO {

    private int incassiCalcio;
    private int incassiTennis;
    private int incassiPiscina;
    private int incassiPalestra;
    
}
