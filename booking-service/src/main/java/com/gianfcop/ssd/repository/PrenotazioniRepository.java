package com.gianfcop.ssd.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gianfcop.ssd.model.Prenotazione;

public interface PrenotazioniRepository extends MongoRepository<Prenotazione, Integer>{

    List<Prenotazione> findByIdUtente(String idUtente);
    List<Prenotazione> findByIdStrutturaAndGiorno(int idStruttura, String giorno);
    
}
