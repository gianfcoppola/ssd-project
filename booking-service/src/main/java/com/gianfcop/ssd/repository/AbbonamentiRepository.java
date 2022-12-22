package com.gianfcop.ssd.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gianfcop.ssd.model.Abbonamento;

public interface AbbonamentiRepository extends MongoRepository<Abbonamento, Integer>{

    List<Abbonamento> findByIdUtente(String idUtente);
    
}
