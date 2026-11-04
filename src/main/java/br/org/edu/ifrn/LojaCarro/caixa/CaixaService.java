
package br.org.edu.ifrn.LojaCarro.caixa;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final CaixaLogPublisher logPublisher;

    public CaixaService(
            CaixaRepository caixaRepository,
            CaixaLogPublisher logPublisher) {
        this.caixaRepository = caixaRepository;
        this.logPublisher = logPublisher;
    }

    public Caixa abrirCaixa(double saldoInicial) {

        if (saldoInicial < 0) {
            throw new IllegalArgumentException(
                    "O saldo inicial não pode ser negativo."
            );
        }

        boolean existeCaixaAberto = caixaRepository.findAll()
                .stream()
                .anyMatch(c -> "ABERTO".equals(c.getStatus()));

        if (existeCaixaAberto) {
            throw new IllegalArgumentException(
                    "Já existe um caixa aberto."
            );
        }

        Caixa caixa = new Caixa();
        caixa.setSaldoInicial(saldoInicial);
        caixa.setSaldoFinal(saldoInicial);
        caixa.setTotalEntradas(0);
        caixa.setTotalSaidas(0);
        caixa.setDataAbertura(LocalDateTime.now());
        caixa.setStatus("ABERTO");

        Caixa salvo = caixaRepository.save(caixa);

        logPublisher.enviarLog(
                "INFO: Caixa aberto. ID: " + salvo.getId()
        );

        return salvo;
    }

    public List<Caixa> listarTodos() {
        return caixaRepository.findAll();
    }

    public Caixa buscarPorId(Long id) {
        return caixaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Caixa não encontrado: " + id
                ));
    }

    public Caixa registrarEntrada(Long id, double valor) {

        if (valor <= 0) {
            throw new IllegalArgumentException(
                    "O valor da entrada deve ser maior que zero."
            );
        }

        Caixa caixa = buscarPorId(id);
        validarCaixaAberto(caixa);

        caixa.setTotalEntradas(
                caixa.getTotalEntradas() + valor
        );

        caixa.setSaldoFinal(
                caixa.getSaldoFinal() + valor
        );

        Caixa salvo = caixaRepository.save(caixa);

        logPublisher.enviarLog(
                "INFO: Entrada registrada no caixa " + id
        );

        return salvo;
    }

    public Caixa registrarSaida(Long id, double valor) {

        if (valor <= 0) {
            throw new IllegalArgumentException(
                    "O valor da saída deve ser maior que zero."
            );
        }

        Caixa caixa = buscarPorId(id);
        validarCaixaAberto(caixa);

        if (valor > caixa.getSaldoFinal()) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente no caixa."
            );
        }

        caixa.setTotalSaidas(
                caixa.getTotalSaidas() + valor
        );

        caixa.setSaldoFinal(
                caixa.getSaldoFinal() - valor
        );

        Caixa salvo = caixaRepository.save(caixa);

        logPublisher.enviarLog(
                "WARN: Saída registrada no caixa " + id
        );

        return salvo;
    }

    public Caixa fecharCaixa(Long id) {

        Caixa caixa = buscarPorId(id);
        validarCaixaAberto(caixa);

        caixa.setDataFechamento(LocalDateTime.now());
        caixa.setStatus("FECHADO");

        Caixa salvo = caixaRepository.save(caixa);

        logPublisher.enviarLog(
                "INFO: Caixa fechado. ID: " + id
        );

        return salvo;
    }

    private void validarCaixaAberto(Caixa caixa) {
        if (!"ABERTO".equals(caixa.getStatus())) {
            throw new IllegalArgumentException(
                    "O caixa não está aberto."
            );
        }
    }
}
