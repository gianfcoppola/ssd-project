package com.gianfcop.ssd.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Abbonamento")
public class Abbonamento {

	@Transient
    public static final String SEQUENCE_NAME = "abbonamenti_sequence";

    private int id;
	private String timestampPrenotazione;
	private String idUtente;
	private int idStruttura;
	private String dataInizioAbbonamento;
	private String dataFineAbbonamento;
	private int prezzoTotale;
    

}
