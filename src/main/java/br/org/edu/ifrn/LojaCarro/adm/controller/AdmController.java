package br.org.edu.ifrn.LojaCarro.adm.controller;

import br.org.edu.ifrn.LojaCarro.adm.service.AdmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm")
public class AdmController {

    private final AdmService admService;

    public AdmController(AdmService admService) {
        this.admService = admService;
    }

    @GetMapping("/teste")
    public String testar() {
        return admService.verificarAcesso();
    }
}