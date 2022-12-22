package com.gianfcop.ssd.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gianfcop.ssd.model.CentroSportivo;

public interface CentroSportivoRepository extends MongoRepository<CentroSportivo, Integer>{
    
}
