package com.gianfcop.ssd.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gianfcop.ssd.dto.PrenotazioneDTOIn;
import com.gianfcop.ssd.dto.PrenotazioneDTOOut;
import com.gianfcop.ssd.mapper.PrenotazioneMapper;
import com.gianfcop.ssd.model.Prenotazione;
import com.gianfcop.ssd.repository.PrenotazioniRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@RefreshScope
@Slf4j
public class PrenotazioniService {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;
    
   
    private SequenceGeneratorService sequenceGeneratorService;
    
    private PrenotazioniRepository prenotazioniRepository;
   
    public PrenotazioniService(SequenceGeneratorService sequenceGeneratorService,
            PrenotazioniRepository prenotazioniRepository) {
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.prenotazioniRepository = prenotazioniRepository;
    }

    public boolean insertPrenotazione(PrenotazioneDTOIn prenotazioneDTOIn, String accessToken){

        int idPrenotazione = (int)sequenceGeneratorService.generateSequence(Prenotazione.SEQUENCE_NAME);

        Prenotazione nuovaPrenotazione = PrenotazioneMapper.toPrenotazione(prenotazioneDTOIn, idPrenotazione);
        boolean salvaPrenotazione = checkPossibileEffetuarePrenotazione(nuovaPrenotazione);

        if(salvaPrenotazione == true){
            prenotazioniRepository.save(nuovaPrenotazione);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(accessToken);
            String uri = "http://structures-service/centro-sportivo/nuova-prenotazione/" + String.valueOf(prenotazioneDTOIn.getIdStruttura());
            restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(httpHeaders), Void.class);
            log.info("Prenotazione inserita");
        }
            

        return salvaPrenotazione;

    }

    public List<Prenotazione> getAllPrenotazioni(){
        return prenotazioniRepository.findAll();
    }

    public Prenotazione getPrenotazioneById(int id){
        return prenotazioniRepository.findById(id).orElse(null);
    }

    public List<PrenotazioneDTOOut> getPrenotazioneByIdUtente(String idUtente){
        List<Prenotazione> prenotazioniUtente = prenotazioniRepository.findByIdUtente(idUtente);
        List<PrenotazioneDTOOut> prenotazioniUtenteDTO = new ArrayList<>();
        
        for(Prenotazione p: prenotazioniUtente){
            String uri = "http://structures-service/strutture/nome/" + String.valueOf(p.getIdStruttura());
            String nomeStruttura = restTemplate.getForObject(uri, String.class);
            prenotazioniUtenteDTO.add(PrenotazioneMapper.toPrenotazioneDTOOut(p, nomeStruttura));
        }
        Collections.reverse(prenotazioniUtenteDTO);
        return prenotazioniUtenteDTO;
    }


    public boolean deletePrenotazione(int idPrenotazione){
        prenotazioniRepository.deleteById(idPrenotazione);
        return true;
    }

    public List<PrenotazioneDTOOut> getPrenotazioneLibere(int idStruttura, String data) {
        
        String uri = "http://structures-service/strutture/nome/" + String.valueOf(idStruttura);
        String nomeStruttura = restTemplate.getForObject(uri, String.class);

       
        Prenotazione p1 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("09:00")
            .oraFine("10:30")
            .build();
        Prenotazione p2 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("10:30")
            .oraFine("12:00")
            .build();
        Prenotazione p3 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("12:00")
            .oraFine("13:30")
            .build();
        Prenotazione p4 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("13:30")
            .oraFine("15:00")
            .build();
        Prenotazione p5 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("15:00")
            .oraFine("16:30")
            .build();
        Prenotazione p6 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("16:30")
            .oraFine("18:00")
            .build();
        Prenotazione p7 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("18:00")
            .oraFine("19:30")
            .build();
        Prenotazione p8 = Prenotazione.builder()
            .idStruttura(idStruttura)
            .giorno(data)
            .oraInizio("19:30")
            .oraFine("21:00")
            .build();

        List<Prenotazione> prenotazioniLibere = new ArrayList<>();
        prenotazioniLibere.add(p1);
        prenotazioniLibere.add(p2);
        prenotazioniLibere.add(p3);
        prenotazioniLibere.add(p4);
        prenotazioniLibere.add(p5);
        prenotazioniLibere.add(p6);
        prenotazioniLibere.add(p7);
        prenotazioniLibere.add(p8);


        int numeroPostiDisponibili;
        if(idStruttura == 1 || idStruttura == 2)
            numeroPostiDisponibili = 1;
        else
            numeroPostiDisponibili = 5;
        List<PrenotazioneDTOOut> prenotazioneDTOOutList = PrenotazioneMapper.toPrenotazioneDTOOutList2(prenotazioniLibere, nomeStruttura, numeroPostiDisponibili);

        List<Prenotazione> prenotazioniEffettuate = prenotazioniRepository.findByIdStrutturaAndGiorno(idStruttura, data);
        if(idStruttura == 1 || idStruttura == 2){
            for(int i=0; i<prenotazioniLibere.size(); i++){
                for(Prenotazione pe: prenotazioniEffettuate){
                    if(prenotazioniLibere.get(i).getGiorno().equals(pe.getGiorno()) && prenotazioniLibere.get(i).getIdStruttura() == pe.getIdStruttura() &&
                    prenotazioniLibere.get(i).getOraInizio().equals(pe.getOraInizio())){
                        
                        prenotazioneDTOOutList.get(i).setNumeroPostiDisponibili(0);
                        break;
                    }
                }
            }
        }
        else{
            for(int i=0; i<prenotazioniLibere.size(); i++){
                for(Prenotazione pe: prenotazioniEffettuate){
                    if(prenotazioniLibere.get(i).getGiorno().equals(pe.getGiorno()) && prenotazioniLibere.get(i).getIdStruttura() == pe.getIdStruttura() &&
                    prenotazioniLibere.get(i).getOraInizio().equals(pe.getOraInizio())){
                        prenotazioneDTOOutList.get(i).setNumeroPostiDisponibili(prenotazioneDTOOutList.get(i).getNumeroPostiDisponibili() - 1);
                        if(prenotazioneDTOOutList.get(i).getNumeroPostiDisponibili() == 0){
                            break;
                        }
                    }
                }
            }
        }

        return prenotazioneDTOOutList;

        /*

        //VERSIONE 1
        if(idStruttura == 1 || idStruttura == 2){
            for(int i=0; i<prenotazioniLibere.size(); i++){
                for(Prenotazione pe: prenotazioniEffettuate){
                    if(prenotazioniLibere.get(i).getGiorno().equals(pe.getGiorno()) && prenotazioniLibere.get(i).getIdStruttura() == pe.getIdStruttura() &&
                        prenotazioniLibere.get(i).getOraInizio().equals(pe.getOraInizio())){
                        prenotazioniLibere.remove(i);
                        break;
                    }
                }
            }
        }
        else{
            int numeroPrenotazioniStessoOrario = 0;
            for(int i=0; i<prenotazioniLibere.size(); i++){
                for(Prenotazione pe: prenotazioniEffettuate){
                    if(prenotazioniLibere.get(i).getGiorno().equals(pe.getGiorno()) && prenotazioniLibere.get(i).getIdStruttura() == pe.getIdStruttura() &&
                    prenotazioniLibere.get(i).getOraInizio().equals(pe.getOraInizio())){
                        numeroPrenotazioniStessoOrario++;
                        if(numeroPrenotazioniStessoOrario == 5){
                            prenotazioniLibere.remove(i);
                            break;
                        }
                    }
                }
            }
        }
         return PrenotazioneMapper.toPrenotazioneDTOOutList(prenotazioniLibere, nomeStruttura);
         */

        
        

       
    }


    private boolean checkPossibileEffetuarePrenotazione(Prenotazione nuovaPrenotazione){
        

            boolean possibileEffettuarePrenotazione = true;
            List<Prenotazione> prenotazioniEffettuate = prenotazioniRepository.findByIdStrutturaAndGiorno(nuovaPrenotazione.getIdStruttura(), nuovaPrenotazione.getGiorno());
            
            //CALCIO O TENNIS
            if(nuovaPrenotazione.getIdStruttura() == 1 || nuovaPrenotazione.getIdStruttura() == 2){
                for(Prenotazione p: prenotazioniEffettuate){
                    if(checkPrenotazioneDisponibile(nuovaPrenotazione, p) == false) {
                        possibileEffettuarePrenotazione = false;
                        break;
                    }
                }
            }

            //PISCINA O PALESTRA
            else{
                int numeroPrenotazioniStessoOrario = 0;
                for(Prenotazione p: prenotazioniEffettuate){
                    if(checkPrenotazioneDisponibile(nuovaPrenotazione, p) == false) {
                        numeroPrenotazioniStessoOrario++;
                        if(numeroPrenotazioniStessoOrario == 5){
                            possibileEffettuarePrenotazione = false;
                            break;
                        }
                    }
                }
            }
            
            return possibileEffettuarePrenotazione;
        
    }


    private boolean checkPrenotazioneDisponibile(Prenotazione nuova, Prenotazione check){
        if(nuova.getGiorno().equals(check.getGiorno()) && nuova.getOraInizio().equals(check.getOraInizio())
        && nuova.getIdStruttura() == check.getIdStruttura())
            return false;
        else
            return true;
    }
}
