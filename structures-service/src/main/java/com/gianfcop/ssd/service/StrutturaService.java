package com.gianfcop.ssd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gianfcop.ssd.dto.StrutturaDtoIn;
import com.gianfcop.ssd.dto.StrutturaDtoOut;
import com.gianfcop.ssd.dto.StrutturaUpdateDTO;
import com.gianfcop.ssd.mapper.StrutturaMapper;
import com.gianfcop.ssd.model.Struttura;
import com.gianfcop.ssd.repository.StrutturaRepository;

@Service
public class StrutturaService {
    
    private StrutturaRepository strutturaRepository;

    public StrutturaService(StrutturaRepository strutturaRepository) {
        this.strutturaRepository = strutturaRepository;
    }

    public Struttura insertStruttura(StrutturaDtoIn strutturaDtoIn){

        Struttura struttura = Struttura.builder()
            .descrizione(strutturaDtoIn.getDescrizione())
            .prezzoPrenotazione(strutturaDtoIn.getPrezzoPrenotazione())
            .prezzoAbbonamentoMensile(strutturaDtoIn.getPrezzoAbbonamentoMensile())
            .id(strutturaDtoIn.getId())
            .build();

        return strutturaRepository.save(struttura);
    }

    public Struttura getStrutturaById(int id){
        return strutturaRepository.findById(id).get();
    }

    /*

    public List<Struttura> getStruttureByTipoStruttura(int tipoStruttura){

        return strutturaRepository.findByTipoStruttura(tipoStruttura);

    }
     */

    public List<Struttura> getAllStrutture(){

        return strutturaRepository.findAll();

    }

    public List<StrutturaDtoOut> getAllStruttureDtoOut(){

        List<Struttura> strutture = strutturaRepository.findAll();
        List<StrutturaDtoOut> struttureDtoOuts = new ArrayList<>();
        for(Struttura s: strutture)
            struttureDtoOuts.add(StrutturaMapper.toStrutturaDtoOut(s));

        return struttureDtoOuts;

    }

    public List<StrutturaUpdateDTO> getDatiStrutture(){
        List<Struttura> strutture = strutturaRepository.findAll();
        List<StrutturaUpdateDTO> str = new ArrayList<>();

        for(Struttura s: strutture)
            str.add(StrutturaMapper.toStrutturaUpdateDTO(s));

        return str;
    }

    public List<String> getAllNomiStrutture(){

        return strutturaRepository.findAll()
            .stream()
            .map(s -> s.getDescrizione())
            .toList();

    }

    public Struttura modificaStruttura(int idStruttura, Struttura struttura){
        Struttura s = strutturaRepository.findById(idStruttura).get();
        s.setPrezzoAbbonamentoMensile(struttura.getPrezzoAbbonamentoMensile());
        s.setPrezzoPrenotazione(struttura.getPrezzoPrenotazione());
        return strutturaRepository.save(s);
    }


    


}
