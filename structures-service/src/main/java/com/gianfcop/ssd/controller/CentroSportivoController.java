package com.gianfcop.ssd.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gianfcop.ssd.dto.DatiAbbonamentoDTO;
import com.gianfcop.ssd.dto.DatiStruttura1;
import com.gianfcop.ssd.dto.DatiStruttura2;
import com.gianfcop.ssd.dto.IncassiDTO;
import com.gianfcop.ssd.dto.NumeroAbbonamentiDTO;
import com.gianfcop.ssd.dto.NumeroPrenotazioniDTO;
import com.gianfcop.ssd.model.CentroSportivo;
import com.gianfcop.ssd.service.CentroSportivoService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CentroSportivoController {


    @Autowired
    private CentroSportivoService centroSportivoService;

    @GetMapping("/index")
    public String homePage(){
        return "index";
    }

    @GetMapping("/centro-sportivo/dati")
    public String datiCentroSportivo(Model model, @AuthenticationPrincipal Jwt jwt){
       
        String roles = jwt.getClaimAsMap("realm_access").get("roles").toString();
        List<String> roleList = Arrays.stream(roles.replace("[","").replace("]", "").replace("\"", "").split(",")).toList();
        if(!roleList.contains("admin")){
            log.error("ACCESS FORBIDDEN 403");
            return "access_forbidden";
        }
        model.addAttribute("datiPrenotazioni", centroSportivoService.getDatiPrenotazioni());
        model.addAttribute("datiAbbonamenti", centroSportivoService.getDatiAbbonamenti());
        model.addAttribute("datiIncassi", centroSportivoService.getIncassiCentroSportivo());
        return "dati-centro-sportivo";
    }

    @GetMapping("/centro-sportivo/prenotazioni/data-analysis")
    public String analisiDatiPrenotazioni(Model model, @AuthenticationPrincipal Jwt jwt){
       
        String roles = jwt.getClaimAsMap("realm_access").get("roles").toString();
        List<String> roleList = Arrays.stream(roles.replace("[","").replace("]", "").replace("\"", "").split(",")).toList();
        if(!roleList.contains("admin_centro_sportivo")){
            log.error("ACCESS FORBIDDEN 403");
            return "access_forbidden";
        }
        NumeroPrenotazioniDTO numeroPrenotazioniDTO = centroSportivoService.getDatiPrenotazioni();
        Map<String, Integer> graphData = new TreeMap<>();
        graphData.put("calcio", numeroPrenotazioniDTO.getNumeroPrenotazioniCalcio());
        graphData.put("tennis", numeroPrenotazioniDTO.getNumeroPrenotazioniTennis());
        graphData.put("piscina", numeroPrenotazioniDTO.getNumeroPrenotazioniPiscina());
        graphData.put("palestra", numeroPrenotazioniDTO.getNumeroPrenotazioniPalestra());
        model.addAttribute("chartData", graphData);

        String oggi = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        model.addAttribute("oggi", oggi);

        int numeroPrenotazioni = numeroPrenotazioniDTO.getNumeroPrenotazioniCalcio() + numeroPrenotazioniDTO.getNumeroPrenotazioniTennis() + numeroPrenotazioniDTO.getNumeroPrenotazioniPiscina() + numeroPrenotazioniDTO.getNumeroPrenotazioniPalestra();
        model.addAttribute("numeroPrenotazioni", numeroPrenotazioni);

        return "statistiche-prenotazioni";
    }

    @GetMapping("/centro-sportivo/abbonamenti/data-analysis")
    public String analisiDatiAbbonamenti(Model model, @AuthenticationPrincipal Jwt jwt){
        
        String roles = jwt.getClaimAsMap("realm_access").get("roles").toString();
        List<String> roleList = Arrays.stream(roles.replace("[","").replace("]", "").replace("\"", "").split(",")).toList();
        if(!roleList.contains("admin")){
            log.error("ACCESS FORBIDDEN 403");
            return "access_forbidden";
        }

        NumeroAbbonamentiDTO numeroAbbonamentiDTO = centroSportivoService.getDatiAbbonamenti();
        Map<String, Integer> graphData = new TreeMap<>();
        
        graphData.put("piscina", numeroAbbonamentiDTO.getNumeroAbbonamentiPiscina());
        graphData.put("palestra", numeroAbbonamentiDTO.getNumeroAbbonamentiPalestra());
        model.addAttribute("chartData", graphData);
        String oggi = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        model.addAttribute("oggi", oggi);

        int numeroAbbonamenti = numeroAbbonamentiDTO.getNumeroAbbonamentiPalestra() + numeroAbbonamentiDTO.getNumeroAbbonamentiPiscina();
        model.addAttribute("numeroAbbonamenti", numeroAbbonamenti);
        

        return "statistiche-abbonamenti";
    }

    @GetMapping("/centro-sportivo/incassi/data-analysis")
    public String analisiIncassiCentroSportivo(Model model, @AuthenticationPrincipal Jwt jwt){
        
        String roles = jwt.getClaimAsMap("realm_access").get("roles").toString();
        List<String> roleList = Arrays.stream(roles.replace("[","").replace("]", "").replace("\"", "").split(",")).toList();
        if(!roleList.contains("admin")){
            log.error("ACCESS FORBIDDEN 403");
            return "access_forbidden";
        }

        IncassiDTO incassiDTO = centroSportivoService.getIncassiCentroSportivo();
        Map<String, Integer> graphData = new TreeMap<>();
        
        graphData.put("calcio", incassiDTO.getIncassiCalcio());
        graphData.put("tennis", incassiDTO.getIncassiTennis());
        graphData.put("piscina", incassiDTO.getIncassiPiscina());
        graphData.put("palestra", incassiDTO.getIncassiPalestra());
        model.addAttribute("chartData", graphData);
        //String oggi = LocalDate.now().toString();
        String oggi = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        model.addAttribute("oggi", oggi);
        int incassiTotali = incassiDTO.getIncassiCalcio() + incassiDTO.getIncassiTennis() + incassiDTO.getIncassiPiscina() + incassiDTO.getIncassiPalestra();
        model.addAttribute("sommaIncassiTotali", incassiTotali);
        return "statistiche-incassi";
    }


    
    @PostMapping("/centro-sportivo/crea")
    public ResponseEntity<CentroSportivo> creaCentroSportivo(@RequestBody CentroSportivo centroSportivo){
        return new ResponseEntity<CentroSportivo>(centroSportivoService.creaCentroSportivo(centroSportivo), HttpStatus.CREATED);
    }

    @PutMapping("/centro-sportivo/nuova-prenotazione/{idStruttura}")
    public ResponseEntity<?> nuovaPrenotazione(@PathVariable("idStruttura") int idStruttura){

        if(idStruttura == 1 || idStruttura == 2)
            return new ResponseEntity<DatiStruttura1>(centroSportivoService.aggiornaDatiStruttura1(idStruttura), HttpStatus.OK);
        else
            return new ResponseEntity<DatiStruttura2>(centroSportivoService.aggiornaDatiStruttura2(idStruttura), HttpStatus.OK);

    }

    @PutMapping("/centro-sportivo/nuovo-abbonamento")
    public ResponseEntity<DatiStruttura2> nuovoAbbonamento(@RequestBody DatiAbbonamentoDTO datiAbbonamento){

        return new ResponseEntity<DatiStruttura2>(centroSportivoService.effettuaAbbonamento(datiAbbonamento.getIdStruttura(), datiAbbonamento.getNumeroMesiAbbonamento()), HttpStatus.OK);
    }

    
}
