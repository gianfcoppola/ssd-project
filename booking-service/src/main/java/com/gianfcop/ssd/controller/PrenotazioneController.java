package com.gianfcop.ssd.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gianfcop.ssd.dto.PrenotazioneCercaDTOIn;
import com.gianfcop.ssd.dto.PrenotazioneDTOIn;
import com.gianfcop.ssd.service.PrenotazioniService;
import com.gianfcop.ssd.service.StruttureService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PrenotazioneController {
    
    @Autowired
    private PrenotazioniService prenotazioniService;

    @Autowired
    private StruttureService struttureService;


    


    // ################         SPRING MVC             #######################

    @GetMapping("/prenotazioni/index")
    public String home(){
        return "index";
    } 

    //@RolesAllowed("admin_role")
    @GetMapping("/prenotazioni/new")
    public String returnCreaPrenotazione(Model model, @AuthenticationPrincipal Jwt jwt){

        //String authorizationHeader = headers.get(HttpHeaders.AUTHORIZATION.toLowerCase());
        //String accessToken = authorizationHeader.replace("Bearer ", "");
      //
        String idUtente = jwt.getClaims().get("sub").toString();
        String nomeUtente = jwt.getClaimAsString("name");
        String roles = jwt.getClaimAsMap("realm_access").get("roles").toString();
        List<String> roleList = Arrays.stream(roles.replace("[","").replace("]", "").replace("\"", "").split(",")).toList();
        if(!roleList.contains("user")){
            log.error("ACCESS FORBIDDEN 403");
            return "forbidden_page";
        }

        //log.info("USERNAME PASSED FROM API GATEWAY: ", request.getHeader("username_header"));
         /*
         String idUtente = headers.get("username_header");
        String nomeUtente = headers.get("name_header");
        headers.forEach((key, value) -> {
            log.info(String.format("Header '%s' = %s", key, value));
        });
        log.info("USERNAME OF THE USER LOGGED: {}", headers.get("username_header"));
*/
        // create prenotazione object to hold prenotazione form data
		PrenotazioneDTOIn prenotazioneDTOIn = new PrenotazioneDTOIn();

 

        prenotazioneDTOIn.setIdUtente(idUtente);
        model.addAttribute("idUtente", idUtente);
        model.addAttribute("nomeUtente", nomeUtente);
        model.addAttribute("oggi", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("prenotazioneDTOIn", prenotazioneDTOIn);
        model.addAttribute("infoStrutture", struttureService.getInfoStrutture());
        return "crea_prenotazione";
    }

    @PostMapping("/prenotazioni/new")
	public String savePrenotazione(@ModelAttribute("prenotazioneDTOIn") PrenotazioneDTOIn prenotazioneDTOIn, Model model, @AuthenticationPrincipal Jwt jwt) {
        String idUtente = jwt.getClaims().get("sub").toString();
        String nomeUtente = jwt.getClaimAsString("name");
        model.addAttribute("idUtente", idUtente);
        prenotazioneDTOIn.setIdUtente(idUtente);
        model.addAttribute("nomeUtente", nomeUtente);
        if(prenotazioniService.insertPrenotazione(prenotazioneDTOIn, jwt.getTokenValue())){
            log.info("prenotazione inserita");
            //redirectAttributes.addAttribute("utente", prenotazioneDTOIn.getIdUtente()).addFlashAttribute("prenotazioneInserita", "1");
            model.addAttribute("prenotazioneInserita", "1");
            model.addAttribute("prenotazioni", prenotazioniService.getPrenotazioneByIdUtente(prenotazioneDTOIn.getIdUtente()));
            return "prenotazioni-utente";
            //return "redirect:/prenotazioni/" + String.valueOf(prenotazioneDTOIn.getIdUtente());
        }
        else{
            String data = prenotazioneDTOIn.getGiorno();
            int idStruttura = prenotazioneDTOIn.getIdStruttura();
            int giorno, mese, anno;
            giorno = Integer.valueOf(data.substring(8));
            mese = Integer.valueOf(data.substring(5, 7));
            anno = Integer.valueOf(data.substring(0, 4));
            String dataPrenotazione = LocalDate.of(anno, mese, giorno).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
            model.addAttribute("nomeStruttura", struttureService.getNomeStruttura(idStruttura));
            model.addAttribute("dataPrenotazioneRichiesta", dataPrenotazione);
            model.addAttribute("prenotazioniDisponibili", prenotazioniService.getPrenotazioneLibere(idStruttura, dataPrenotazione));
            return "prenotazioni-disponibili";
            //return "redirect:/prenotazioni/disponibili/" + String.valueOf(prenotazioneDTOIn.getIdStruttura()) + "/" + dataPrenotazione;
            //return "redirect:/prenotazioni/disponibili";
        }
            
        
	}

    @GetMapping("/prenotazioni/{idUtente}")
    public String listaMiePrenotazioni(Model model, @PathVariable("idUtente") String IDUtente, @AuthenticationPrincipal Jwt jwt){
        String idUtente = jwt.getClaims().get("sub").toString();
        String nomeUtente = jwt.getClaimAsString("name");
        model.addAttribute("nomeUtente", nomeUtente);
        model.addAttribute("idUtente", idUtente);
        model.addAttribute("prenotazioni", prenotazioniService.getPrenotazioneByIdUtente(IDUtente));
        return "prenotazioni-utente";
    }
  

    @GetMapping("/prenotazioni/disponibili/{idStruttura}/{data}")
    public String listaPrenotazioniDisponibili(Model model, @PathVariable("idStruttura") int idStruttura, @PathVariable("data") String data, @AuthenticationPrincipal Jwt jwt){

        String idUtente = jwt.getClaims().get("sub").toString();
        String nomeUtente = jwt.getClaimAsString("name");
        model.addAttribute("nomeUtente", nomeUtente);
        model.addAttribute("idUtente", idUtente);

        model.addAttribute("nomeStruttura", struttureService.getNomeStruttura(idStruttura));
        model.addAttribute("dataPrenotazioneRichiesta", data);
        model.addAttribute("prenotazioniDisponibili", prenotazioniService.getPrenotazioneLibere(idStruttura, data));
        return "prenotazioni-disponibili";
    }

    @GetMapping("/prenotazioni/disponibili/cerca") 
    public String cercaPrenotazioniDisponibili(Model model, @AuthenticationPrincipal Jwt jwt){

        String idUtente = jwt.getClaims().get("sub").toString();
        String nomeUtente = jwt.getClaimAsString("name");
        model.addAttribute("nomeUtente", nomeUtente);
        model.addAttribute("idUtente", idUtente);
        model.addAttribute("oggi", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        PrenotazioneCercaDTOIn prenotazioneCercaDTOIn = new PrenotazioneCercaDTOIn();
        model.addAttribute("prenotazioneCercaDTOIn", prenotazioneCercaDTOIn);
        model.addAttribute("infoStrutture", struttureService.getInfoStrutture());
        return "prenotazioni-disponibili-cerca";
    }

    @PostMapping("/prenotazioni/disponibili/cerca")
	public String cercaPrenotazione(@ModelAttribute("prenotazioneCercaDTOIn") PrenotazioneCercaDTOIn prenotazioneCercaDTOIn, Model model, @AuthenticationPrincipal Jwt jwt) {
        String data = prenotazioneCercaDTOIn.getData();
        int giorno, mese, anno;
        giorno = Integer.valueOf(data.substring(8));
        mese = Integer.valueOf(data.substring(5, 7));
        anno = Integer.valueOf(data.substring(0, 4));
        data = LocalDate.of(anno, mese, giorno).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
        //String url = "/prenotazioni/disponibili/" + String.valueOf(prenotazioneCercaDTOIn.getIdStruttura()) + "/" + data;
        //return new RedirectView(url);
        //return "redirect:/prenotazioni/disponibili/" + String.valueOf(prenotazioneCercaDTOIn.getIdStruttura()) + "/" + data;
        String idUtente = jwt.getClaims().get("sub").toString();
        String nomeUtente = jwt.getClaimAsString("name");
        model.addAttribute("idUtente", idUtente);
		model.addAttribute("nomeUtente", nomeUtente);
        model.addAttribute("nomeStruttura", struttureService.getNomeStruttura(prenotazioneCercaDTOIn.getIdStruttura()));
        model.addAttribute("dataPrenotazioneRichiesta", data);
        model.addAttribute("prenotazioniDisponibili", prenotazioniService.getPrenotazioneLibere(prenotazioneCercaDTOIn.getIdStruttura(), data));
        return "prenotazioni-disponibili";
	}
    
    @GetMapping("/prenotazioni/cancella/{id}")
    public String cancellaPrenotazione(@PathVariable("id") int idPrenotazione, Model model, @AuthenticationPrincipal Jwt jwt){
        String idUtente = jwt.getClaims().get("sub").toString();
        String nomeUtente = jwt.getClaimAsString("name");
		model.addAttribute("nomeUtente", nomeUtente);

        prenotazioniService.deletePrenotazione(idPrenotazione);
        model.addAttribute("idUtente", idUtente);
        model.addAttribute("prenotazioni", prenotazioniService.getPrenotazioneByIdUtente(idUtente));

        return "prenotazioni-utente";
    }
    




    /////////////////////////////////////////////////////////////////////////////////////////////

    //##########################           UTILI PER DEBUG           ########################
    


/* 
@GetMapping("prenotazioni/nomiStrutture")
    public ResponseEntity<List<String>> getNomiStrutture(){
        return new ResponseEntity<List<String>>(struttureService.getNomiStrutture(), HttpStatus.OK) ;
    }

    @GetMapping("prenotazioni/infoStrutture")
    public ResponseEntity<List<StrutturaDTOOut>> getInfoStrutture(){
        return new ResponseEntity<List<StrutturaDTOOut>>(struttureService.getInfoStrutture(), HttpStatus.OK) ;
    }
    @GetMapping("/prenotazioni/{struttura}/{data}")
    public ResponseEntity<List<PrenotazioneDTOOut>> listaMiePrenotazioni(Model model, @PathVariable("struttura") int idStruttura, @PathVariable("data") String data){
        return new ResponseEntity<List<PrenotazioneDTOOut>>(prenotazioniService.getPrenotazioneLibere(idStruttura, data), HttpStatus.OK);
    }
    */


    /* 
    @PostMapping("/prenotazioni/insert")
    public ResponseEntity<String> addPrenotazione(@RequestBody PrenotazioneDTOIn prenotazione){
        Prenotazione p = prenotazioniService.insertPrenotazione(prenotazione);
        return new ResponseEntity<String>("Prenotazione " + p.getId() + " inserita", HttpStatus.CREATED);
    }

    @GetMapping("/prenotazioni/all")
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni(){
        return new ResponseEntity<List<Prenotazione>>(prenotazioniService.getAllPrenotazioni(), HttpStatus.OK);
    }

    
    @GetMapping("/prenotazioni/{idUtente}/all")
    public ResponseEntity<List<Prenotazione>> getPrenotazioniUtente(@PathVariable("idUtente") int idUtente){
        return new ResponseEntity<List<Prenotazione>>(prenotazioniService.getPrenotazioneByIdUtente(idUtente), HttpStatus.OK);
    }
    */

    /* 
    @GetMapping("/prenotazioni/{id}")
    public ResponseEntity<Prenotazione> getPrenotazione(@PathVariable("id") int idPrenotazione){
        return new ResponseEntity<Prenotazione>(prenotazioniService.getPrenotazioneById(idPrenotazione), HttpStatus.OK);
    }
    */

    
    /* 
    @PutMapping("/{id}")
    public ResponseEntity<String> modificaPrenotazione(@RequestBody PrenotazioneDTOIn prenotazione, @PathVariable("id") int idPrenotazione){

        Prenotazione p = prenotazioniService.updatePrenotazione(idPrenotazione, prenotazione);
        return new ResponseEntity<String>("Prenotazione " + p.getId() + " modificata con successo", HttpStatus.OK);
    }
    
   

    
    */
}
