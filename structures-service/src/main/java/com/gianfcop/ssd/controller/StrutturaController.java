package com.gianfcop.ssd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gianfcop.ssd.dto.StrutturaDtoIn;
import com.gianfcop.ssd.dto.StrutturaDtoOut;
import com.gianfcop.ssd.model.Struttura;
import com.gianfcop.ssd.service.StrutturaService;


@RequestMapping("/strutture")
@Controller
public class StrutturaController {
    
    private StrutturaService strutturaService;

    

    public StrutturaController(StrutturaService strutturaService) {
        this.strutturaService = strutturaService;
    }

    @GetMapping("/info")
    public ResponseEntity<List<StrutturaDtoOut>> getInfoStrutture(){
        return new ResponseEntity<List<StrutturaDtoOut>>(strutturaService.getAllStruttureDtoOut(), HttpStatus.OK);
    }

    @GetMapping("/nomi")
    public ResponseEntity<List<String>> getNomiStrutture(){
        return new ResponseEntity<List<String>>(strutturaService.getAllNomiStrutture(), HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertStruttura(@RequestBody StrutturaDtoIn strutturaDtoIn){
        return new ResponseEntity<>("Struttura " + strutturaService.insertStruttura(strutturaDtoIn).getId() + " creata correttamente", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Struttura>> getAllStrutture(){
        return new ResponseEntity<List<Struttura>>(strutturaService.getAllStrutture(), HttpStatus.OK) ;
    }

    /* 
    @GetMapping("/tipo-struttura/{tipo}")
    public ResponseEntity<List<Struttura>> getAllStruttureBySport(@PathVariable("tipo") int tipo){
        return new ResponseEntity<List<Struttura>>(strutturaService.getStruttureByTipoStruttura(tipo), HttpStatus.OK) ;
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<Struttura> getStrutturaById(@PathVariable("id") int id){
        return new ResponseEntity<Struttura>(strutturaService.getStrutturaById(id), HttpStatus.OK) ;
    }

    /* 
    @PutMapping("/{id}")
    public ResponseEntity<Struttura> modificaStrutturaById(@PathVariable("id") int idStruttura, @RequestBody StrutturaDtoIn strutturaDtoIn){
        return new ResponseEntity<Struttura>(strutturaService.updateStruttura(idStruttura, strutturaDtoIn), HttpStatus.OK) ;
    }
    */

    @GetMapping("/nome/{id}")
    public ResponseEntity<String> idStrutturaToNome(@PathVariable("id") int idStruttura){
        return new ResponseEntity<String>(strutturaService.getStrutturaById(idStruttura).getDescrizione(), HttpStatus.OK);
    }

    @GetMapping("/dati")
    public String loadDatiStrutture(Model model){
        model.addAttribute("strutture", strutturaService.getDatiStrutture());
        return "dati-strutture";
    }

    @GetMapping("/edit/{id}")
	public String modificaDatiStruttura(@PathVariable("id") int id, Model model) {
        model.addAttribute("struttura", strutturaService.getStrutturaById(id));
        return "modifica-struttura";
        
	}



    @PostMapping("/edit/{id}")
	public String updateStruttura(@PathVariable("id") int id, @ModelAttribute("struttura") Struttura struttura, Model model) {
        strutturaService.modificaStruttura(id, struttura);
        model.addAttribute("strutturaModificata", "1");
        model.addAttribute("strutture", strutturaService.getDatiStrutture());
        return "dati-strutture";
        
	}




}
