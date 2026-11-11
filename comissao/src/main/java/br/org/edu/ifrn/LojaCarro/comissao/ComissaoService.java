
package br.org.edu.ifrn.LojaCarro.comissao;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComissaoService {

    private final ComissaoRepository comissaoRepository;
    private final ComissaoLogPublisher logPublisher;

    public ComissaoService(
            ComissaoRepository comissaoRepository,
            ComissaoLogPublisher logPublisher) {
        this.comissaoRepository = comissaoRepository;
        this.logPublisher = logPublisher;
    }

    public Comissao calcular(
            Long vendaId,
            Long vendedorId,
            double valorVenda,
            double percentual) {

        if (vendaId == null || vendaId <= 0
                || vendedorId == null || vendedorId <= 0
                || valorVenda <= 0) {
            throw new IllegalArgumentException(
                    "Dados da venda inválidos."
            );
        }

        if (percentual <= 0 || percentual > 100) {
            throw new IllegalArgumentException(
                    "O percentual deve estar entre 0 e 100."
            );
        }

        if (!comissaoRepository.findByVendaId(vendaId).isEmpty()) {
            throw new IllegalArgumentException(
                    "Já existe comissão cadastrada para essa venda."
            );
        }

        Comissao comissao = new Comissao();

        comissao.setVendaId(vendaId);
        comissao.setVendedorId(vendedorId);
        comissao.setPercentual(percentual);
        comissao.setValor(valorVenda * percentual / 100);
        comissao.setDataCalculo(LocalDateTime.now());

        Comissao salva = comissaoRepository.save(comissao);

        logPublisher.enviarLog(
                "INFO: Comissão calculada. ID: " + salva.getId()
        );

        return salva;
    }

    public List<Comissao> listarTodas() {
        return comissaoRepository.findAll();
    }

    public List<Comissao> listarPorVendedor(Long vendedorId) {
        return comissaoRepository.findByVendedorId(vendedorId);
    }

    public Comissao buscarPorId(Long id) {
        return comissaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Comissão não encontrada: " + id
                ));
    }

    public void excluir(Long id) {
        Comissao comissao = buscarPorId(id);

        comissaoRepository.delete(comissao);

        logPublisher.enviarLog(
                "WARN: Comissão excluída. ID: " + id
        );
    }
}
