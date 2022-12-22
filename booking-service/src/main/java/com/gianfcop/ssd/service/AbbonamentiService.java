package com.gianfcop.ssd.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gianfcop.ssd.dto.AbbonamentoDTOIn;
import com.gianfcop.ssd.dto.AbbonamentoDTOOut;
import com.gianfcop.ssd.dto.DatiAbbonamentoDTO;
import com.gianfcop.ssd.dto.InfoAbbonamentoCreazioneDTO;
import com.gianfcop.ssd.dto.InfoAbbonamentoDTO;
import com.gianfcop.ssd.mapper.AbbonamentoMapper;
import com.gianfcop.ssd.model.Abbonamento;
import com.gianfcop.ssd.repository.AbbonamentiRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AbbonamentiService {

    @Autowired
    private RestTemplate restTemplate;

    private AbbonamentiRepository abbonamentiRepository;
    private SequenceGeneratorService sequenceGeneratorService;
    public AbbonamentiService(AbbonamentiRepository abbonamentiRepository,
            SequenceGeneratorService sequenceGeneratorService) {
        this.abbonamentiRepository = abbonamentiRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }


    public Abbonamento insertAbbonamento(AbbonamentoDTOIn abbonamentoDTOIn, String accessToken){

        
        int idAbbonamento = (int)sequenceGeneratorService.generateSequence(Abbonamento.SEQUENCE_NAME);

        int prezzoMensileAbbonamento = getInfoAbbonamenti().stream()
            .filter(i -> i.getIdAbbonamento() == abbonamentoDTOIn.getIdStruttura())
            .findAny()
            .get()
            .getPrezzoMensile();
        
        Abbonamento abbonamento = abbonamentiRepository.save(AbbonamentoMapper.toAbbonamento(abbonamentoDTOIn, idAbbonamento, prezzoMensileAbbonamento));

        //aggiorno dati centro sportivo
        String uri = "http://structures-service/centro-sportivo/nuovo-abbonamento";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        DatiAbbonamentoDTO datiAbbonamentoDTO = new DatiAbbonamentoDTO();
        datiAbbonamentoDTO.setIdStruttura(abbonamento.getIdStruttura());
        String dataFineAbbonamento = abbonamentoDTOIn.getDataFineAbbonamento();
        String dataInizioAbbonamento = abbonamentoDTOIn.getDataInizioAbbonamento();
        
        Period diff = Period.between(LocalDate.parse(dataInizioAbbonamento), LocalDate.parse(dataFineAbbonamento));
        int numeroMesiAbbonamento = diff.getMonths() + 1;
        datiAbbonamentoDTO.setNumeroMesiAbbonamento(numeroMesiAbbonamento);

        try {
            HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(datiAbbonamentoDTO), headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, Void.class);
            log.info("Abbonamento registrato");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        

        return abbonamento;

    }

    public List<InfoAbbonamentoDTO> getInfoAbbonamenti(){

        InfoAbbonamentoDTO abbonamentoPiscina = new InfoAbbonamentoDTO(3, "Piscina", 3, 60);
        InfoAbbonamentoDTO abbonamentoPalestra = new InfoAbbonamentoDTO(4, "Palestra", 3, 70);

        return Arrays.asList(abbonamentoPiscina, abbonamentoPalestra);
    }

    public List<InfoAbbonamentoCreazioneDTO> getCreazioneAbbonamentoInfo(){
        List<InfoAbbonamentoDTO> infoAbbonamentoDTOs = getInfoAbbonamenti();
        List<InfoAbbonamentoCreazioneDTO> creazioneAbbonamentoInfo = new ArrayList<>();
        for(InfoAbbonamentoDTO i: infoAbbonamentoDTOs){
            InfoAbbonamentoCreazioneDTO infoAbbonamentoCreazioneDTO = new InfoAbbonamentoCreazioneDTO();
            infoAbbonamentoCreazioneDTO.setDescrizione(i.getStruttura() + " - " + i.getNumeroIngressiSettimana() + " ingressi a settimana");
            infoAbbonamentoCreazioneDTO.setId(i.getIdAbbonamento());
            creazioneAbbonamentoInfo.add(infoAbbonamentoCreazioneDTO);
        }
        return creazioneAbbonamentoInfo;
    }

    public List<Abbonamento> getAllAbbonamenti(){
        return abbonamentiRepository.findAll();
    }

    public Abbonamento getAbbonamentoById(int idAbbonamento){

        return abbonamentiRepository.findById(idAbbonamento).get();
    }

    public List<AbbonamentoDTOOut> getAbbonamentiByIdUtente(String idUtente){

        List<Abbonamento> abbonamentiUtente = abbonamentiRepository.findByIdUtente(idUtente);
        String nomeStruttura = "";
        
           
        List<AbbonamentoDTOOut> abbonamentiDTO = new ArrayList<>();
        for(Abbonamento a: abbonamentiUtente){
            if(abbonamentiUtente.size() != 0){
                int idStruttura = a.getIdStruttura();
                String uri = "http://structures-service/strutture/nome/" + String.valueOf(idStruttura);
                nomeStruttura = restTemplate.getForObject(uri, String.class);
            }
            abbonamentiDTO.add(AbbonamentoMapper.toAbbonamentoDTOOut(a, nomeStruttura));
        }
        Collections.reverse(abbonamentiDTO);
            
        return abbonamentiDTO;
        
    }

    public void deleteAbbonamento(int idAbbonamento){
        abbonamentiRepository.deleteById(idAbbonamento);
    }
    


    
    
}
