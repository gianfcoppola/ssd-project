package com.gianfcop.ssd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbbonamentoDTOIn {
    
	private String idUtente;
	private String dataInizioAbbonamento;
	private String dataFineAbbonamento;
	private int idStruttura;
}
