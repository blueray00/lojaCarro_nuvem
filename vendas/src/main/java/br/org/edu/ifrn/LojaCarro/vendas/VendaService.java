
package br.org.edu.ifrn.LojaCarro.vendas;

import br.org.edu.ifrn.LojaCarro.model.Venda;
import br.org.edu.ifrn.LojaCarro.caixa.Caixa;
import br.org.edu.ifrn.LojaCarro.caixa.CaixaService;
import br.org.edu.ifrn.LojaCarro.comissao.ComissaoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final VendaLogPublisher logPublisher;
    private final CaixaService caixaService;
    private final ComissaoService comissaoService;

    public VendaService(
            VendaRepository vendaRepository,
            VendaLogPublisher logPublisher,
            CaixaService caixaService,
            ComissaoService comissaoService) {

        this.vendaRepository = vendaRepository;
        this.logPublisher = logPublisher;
        this.caixaService = caixaService;
        this.comissaoService = comissaoService;
    }

    @Transactional
    public Venda cadastrar(Venda venda) {

        if (venda == null) {
            throw new IllegalArgumentException("Venda inválida.");
        }

        if (venda.getCarroId() == null || venda.getCarroId() <= 0) {
            throw new IllegalArgumentException("Informe um carro válido.");
        }

        if (venda.getVendedorId() == null || venda.getVendedorId() <= 0) {
            throw new IllegalArgumentException("Informe um vendedor válido.");
        }

        if (venda.getValor() <= 0) {
            throw new IllegalArgumentException(
                    "O valor da venda deve ser maior que zero."
            );
        }

        if (venda.getDataVenda() == null) {
            throw new IllegalArgumentException(
                    "Informe a data da venda."
            );
        }

        Caixa caixaAberto = caixaService.listarTodos()
                .stream()
                .filter(c -> "ABERTO".equals(c.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Não existe caixa aberto para registrar a venda."
                ));

        Venda salva = vendaRepository.save(venda);

        caixaService.registrarEntrada(
                caixaAberto.getId(),
                salva.getValor()
        );

        comissaoService.calcular(
                salva.getId(),
                salva.getVendedorId(),
                salva.getValor(),
                5.0
        );

        logPublisher.enviarLog(
                "INFO: Venda cadastrada e integrada ao caixa e comissão. ID: "
                        + salva.getId()
        );

        return salva;
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Venda não encontrada: " + id
                ));
    }

    @Transactional
    public Venda atualizar(Long id, Venda vendaAtualizada) {

        if (vendaAtualizada == null
                || vendaAtualizada.getCarroId() == null
                || vendaAtualizada.getVendedorId() == null
                || vendaAtualizada.getValor() <= 0
                || vendaAtualizada.getDataVenda() == null) {

            throw new IllegalArgumentException(
                    "Dados inválidos para atualizar a venda."
            );
        }

        Venda venda = buscarPorId(id);

        venda.setCarroId(vendaAtualizada.getCarroId());
        venda.setVendedorId(vendaAtualizada.getVendedorId());
        venda.setValor(vendaAtualizada.getValor());
        venda.setDataVenda(vendaAtualizada.getDataVenda());

        Venda salva = vendaRepository.save(venda);

        logPublisher.enviarLog(
                "INFO: Venda atualizada. ID: " + id
        );

        return salva;
    }

    public void excluir(Long id) {

        Venda venda = buscarPorId(id);

        vendaRepository.delete(venda);

        logPublisher.enviarLog(
                "WARN: Venda excluída. ID: " + id
        );
    }
}
