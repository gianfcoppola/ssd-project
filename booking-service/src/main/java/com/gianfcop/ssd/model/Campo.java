package com.gianfcop.ssd.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Campo")
public class Campo {

    @Id
    private int id;

    private String sport;
    private int prezzoOrario;
    private String oraInizioDisponibilita;
    private String oraFineDisponibilita;


    
}
