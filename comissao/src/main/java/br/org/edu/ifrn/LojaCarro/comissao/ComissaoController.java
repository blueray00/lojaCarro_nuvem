package br.org.edu.ifrn.LojaCarro.comissao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comissoes")
public class ComissaoController {

    private final ComissaoService comissaoService;

    public ComissaoController(ComissaoService comissaoService) {
        this.comissaoService = comissaoService;
    }

    @PostMapping("/calcular")
    public ResponseEntity<Comissao> calcular(
            @RequestBody Map<String, Object> dados) {

        Long vendaId = Long.valueOf(dados.get("vendaId").toString());
        Long vendedorId = Long.valueOf(dados.get("vendedorId").toString());

        double valor = Double.parseDouble(
                dados.get("valor").toString()
        );

        double percentual = Double.parseDouble(
                dados.get("percentual").toString()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(comissaoService.calcular(
                        vendaId, vendedorId, valor, percentual
                ));
    }

    @GetMapping
    public ResponseEntity<List<Comissao>> listarTodas() {
        return ResponseEntity.ok(comissaoService.listarTodas());
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<Comissao>> listarPorVendedor(
            @PathVariable Long vendedorId) {

        return ResponseEntity.ok(
                comissaoService.listarPorVendedor(vendedorId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comissao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comissaoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        comissaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
