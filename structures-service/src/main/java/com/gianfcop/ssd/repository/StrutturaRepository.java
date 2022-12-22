package com.gianfcop.ssd.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.gianfcop.ssd.model.Struttura;

public interface StrutturaRepository extends MongoRepository<Struttura, Integer>{

    
}
