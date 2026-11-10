
package br.org.edu.ifrn.LojaCarro.comissao;

import br.org.edu.ifrn.LojaCarro.model.Venda;
import br.org.edu.ifrn.LojaCarro.vendas.VendaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComissaoService {

    private final ComissaoRepository comissaoRepository;
    private final VendaRepository vendaRepository;
    private final ComissaoLogPublisher logPublisher;

    public ComissaoService(
            ComissaoRepository comissaoRepository,
            VendaRepository vendaRepository,
            ComissaoLogPublisher logPublisher) {
        this.comissaoRepository = comissaoRepository;
        this.vendaRepository = vendaRepository;
        this.logPublisher = logPublisher;
    }

    public Comissao calcular(Long vendaId, double percentual) {

        if (percentual <= 0 || percentual > 100) {
            throw new IllegalArgumentException(
                    "O percentual deve estar entre 0 e 100."
            );
        }

        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException(
                        "Venda não encontrada: " + vendaId
                ));

        if (comissaoRepository.findByVendaId(vendaId).size() > 0) {
            throw new IllegalArgumentException(
                    "Já existe comissão cadastrada para essa venda."
            );
        }

        Comissao comissao = new Comissao();

        comissao.setVendaId(vendaId);
        comissao.setVendedorId(venda.getVendedorId());
        comissao.setPercentual(percentual);
        comissao.setValor(venda.getValor() * percentual / 100);
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
