package com.gianfcop.ssd.mapper;

import com.gianfcop.ssd.dto.IncassiDTO;
import com.gianfcop.ssd.dto.NumeroAbbonamentiDTO;
import com.gianfcop.ssd.dto.NumeroPrenotazioniDTO;
import com.gianfcop.ssd.model.CentroSportivo;

public class CentroSportivoMapper {

    public static NumeroPrenotazioniDTO toNumeroPrenotazioniDTO (CentroSportivo centroSportivo){
        return NumeroPrenotazioniDTO.builder()
            .numeroPrenotazioniCalcio(centroSportivo.getNumeroPrenotazioniCalcio())
            .numeroPrenotazioniTennis(centroSportivo.getNumeroPrenotazioniTennis())
            .numeroPrenotazioniPiscina(centroSportivo.getNumeroPrenotazioniPiscina())
            .numeroPrenotazioniPalestra(centroSportivo.getNumeroPrenotazioniPalestra())
            .build();
    }

    public static NumeroAbbonamentiDTO toNumeroAbbonamentiDTO(CentroSportivo centroSportivo){
        return NumeroAbbonamentiDTO.builder()
            .numeroAbbonamentiPiscina(centroSportivo.getNumeroAbbonamentiPiscina())
            .numeroAbbonamentiPalestra(centroSportivo.getNumeroAbbonamentiPalestra())
            .build();
    }

    public static IncassiDTO toIncassiDTO (CentroSportivo centroSportivo){
        return IncassiDTO.builder()
            .incassiCalcio(centroSportivo.getIncassiCalcio())
            .incassiTennis(centroSportivo.getIncassiTennis())
            .incassiPiscina(centroSportivo.getIncassiPiscina())
            .incassiPalestra(centroSportivo.getIncassiPalestra())
            .build();
    }
    
}
