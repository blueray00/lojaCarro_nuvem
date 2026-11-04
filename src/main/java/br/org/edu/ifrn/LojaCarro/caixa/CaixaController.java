
package br.org.edu.ifrn.LojaCarro.caixa;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/caixas")
public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @PostMapping("/abrir")
    public ResponseEntity<Caixa> abrir(
            @RequestBody Map<String, Double> dados) {

        Caixa caixa = caixaService.abrirCaixa(
                dados.get("saldoInicial")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(caixa);
    }

    @GetMapping
    public ResponseEntity<List<Caixa>> listar() {
        return ResponseEntity.ok(caixaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caixa> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(caixaService.buscarPorId(id));
    }

    @PostMapping("/{id}/entrada")
    public ResponseEntity<Caixa> entrada(
            @PathVariable Long id,
            @RequestBody Map<String, Double> dados) {

        return ResponseEntity.ok(
                caixaService.registrarEntrada(
                        id, dados.get("valor")
                )
        );
    }

    @PostMapping("/{id}/saida")
    public ResponseEntity<Caixa> saida(
            @PathVariable Long id,
            @RequestBody Map<String, Double> dados) {

        return ResponseEntity.ok(
                caixaService.registrarSaida(
                        id, dados.get("valor")
                )
        );
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<Caixa> fechar(@PathVariable Long id) {
        return ResponseEntity.ok(caixaService.fecharCaixa(id));
    }
}
