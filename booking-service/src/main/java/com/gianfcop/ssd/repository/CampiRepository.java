package com.gianfcop.ssd.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gianfcop.ssd.model.Campo;

public interface CampiRepository extends MongoRepository<Campo, Integer>{
    
}
