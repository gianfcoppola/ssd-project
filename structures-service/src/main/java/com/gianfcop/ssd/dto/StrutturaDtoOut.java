package com.gianfcop.ssd.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StrutturaDtoOut {
    
    private int id;
    private String descrizione;
}
