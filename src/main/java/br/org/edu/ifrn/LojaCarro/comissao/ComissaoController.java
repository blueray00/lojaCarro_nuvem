package br.org.edu.ifrn.LojaCarro.comissao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comissao")
public class ComissaoController {

    private final ComissaoService comissaoService;

    public ComissaoController(ComissaoService comissaoService) {
        this.comissaoService = comissaoService;
    }

    @GetMapping("/teste")
    public String testar() {
        return comissaoService.verificarAcesso();
    }
}