package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.CarroException;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    public CarroRepository carroRepository;

    @Autowired
    private LogService logService;

    public Carro save(Carro c) {

        validarModelo(c.getModelo());
        validarPreco(c.getPreco());

        Carro carroSalvo = carroRepository.save(c);

        logService.info(
                "CADASTRO",
                "CARRO",
                carroSalvo.getId(),
                "SUCESSO",
                "MODELO: " + carroSalvo.getModelo()
                        + " | ANO: " + carroSalvo.getAno()
                        + " | PRECO: " + carroSalvo.getPreco()
        );

        return carroSalvo;
    }

    public void deleteById(Long id) {

        if (id <= 0) {

            logService.error(
                    "EXCLUSAO",
                    "CARRO",
                    id,
                    "ERRO",
                    "ID INVALIDO"
            );

            throw new CarroException(
                    "O ID do carro não pode ser negativo. ID fornecido: " + id
            );
        }

        Optional<Carro> carro = carroRepository.findById(id);

        if (carro.isEmpty()) {

            logService.warn(
                    "EXCLUSAO",
                    "CARRO",
                    id,
                    "ERRO",
                    "CARRO NAO ENCONTRADO"
            );

            return;
        }

        Carro carroExcluido = carro.get();

        carroRepository.deleteById(id);

        logService.info(
                "EXCLUSAO",
                "CARRO",
                carroExcluido.getId(),
                "SUCESSO",
                "MODELO: " + carroExcluido.getModelo()
                        + " | ANO: " + carroExcluido.getAno()
                        + " | PRECO: " + carroExcluido.getPreco()
        );
    }

    public Optional<Carro> findById(Long id) {

        if (id <= 0) {

            logService.error(
                    "CONSULTA",
                    "CARRO",
                    id,
                    "ERRO",
                    "ID INVALIDO"
            );

            throw new CarroException(
                    "O ID do carro não pode ser negativo. ID fornecido: " + id
            );
        }

        Optional<Carro> carro = carroRepository.findById(id);

        if (carro.isEmpty()) {

            logService.warn(
                    "CONSULTA",
                    "CARRO",
                    id,
                    "ERRO",
                    "CARRO NAO ENCONTRADO"
            );

        } else {

            logService.info(
                    "CONSULTA",
                    "CARRO",
                    carro.get().getId(),
                    "SUCESSO",
                    "MODELO: " + carro.get().getModelo()
                            + " | ANO: " + carro.get().getAno()
                            + " | PRECO: " + carro.get().getPreco()
            );
        }

        return carro;
    }

    public List<Carro> findAll() {

        List<Carro> carros = carroRepository.findAll();

        logService.info(
                "LISTAGEM",
                "CARROS",
                null,
                "SUCESSO",
                "QUANTIDADE: " + carros.size()
        );

        return carros;
    }

    public Optional<Carro> findByModelo(String modelo) {

        validarModelo(modelo);

        Optional<Carro> carro =
                carroRepository.findFirstByModelo(modelo);

        if (carro.isEmpty()) {

            logService.warn(
                    "CONSULTA",
                    "CARRO",
                    null,
                    "ERRO",
                    "CARRO NAO ENCONTRADO | MODELO: " + modelo
            );

        } else {

            logService.info(
                    "CONSULTA",
                    "CARRO",
                    carro.get().getId(),
                    "SUCESSO",
                    "MODELO: " + carro.get().getModelo()
                            + " | ANO: " + carro.get().getAno()
                            + " | PRECO: " + carro.get().getPreco()
            );
        }

        return carro;
    }

    public Carro saveFromLegacy(String modelo, double preco) {

        Carro carro = new Carro(
                modelo,
                LocalDate.now().getYear(),
                preco
        );

        return save(carro);
    }

    public Carro updateByModelo(String modelo, double preco) {

        Carro carro = localizarCarroPorModelo(modelo);

        validarPreco(preco);

        double precoAnterior = carro.getPreco();

        carro.setPreco(preco);

        Carro carroAtualizado = carroRepository.save(carro);

        logService.info(
                "ATUALIZACAO",
                "CARRO",
                carroAtualizado.getId(),
                "SUCESSO",
                "MODELO: " + carroAtualizado.getModelo()
                        + " | PRECO ANTERIOR: " + precoAnterior
                        + " | PRECO NOVO: " + carroAtualizado.getPreco()
                        + " | ANO: " + carroAtualizado.getAno()
        );

        return carroAtualizado;
    }

    public Carro deleteByModelo(String modelo) {

        Carro carro = localizarCarroPorModelo(modelo);

        carroRepository.delete(carro);

        logService.info(
                "EXCLUSAO",
                "CARRO",
                carro.getId(),
                "SUCESSO",
                "MODELO: " + carro.getModelo()
                        + " | ANO: " + carro.getAno()
                        + " | PRECO: " + carro.getPreco()
        );

        return carro;
    }

    public Carro update(Carro c) {

        if (c.getId() == null) {

            logService.error(
                    "ATUALIZACAO",
                    "CARRO",
                    null,
                    "ERRO",
                    "ID NAO INFORMADO"
            );

            throw new CarroException(
                    "O ID do carro para atualização não pode ser nulo."
            );
        }

        if (!carroRepository.existsById(c.getId())) {

            logService.warn(
                    "ATUALIZACAO",
                    "CARRO",
                    c.getId(),
                    "ERRO",
                    "CARRO NAO ENCONTRADO"
            );

            throw new CarroException(
                    "Carro com ID " + c.getId()
                            + " não encontrado para atualização."
            );
        }

        validarModelo(c.getModelo());
        validarPreco(c.getPreco());

        Optional<Carro> carroAnterior =
                carroRepository.findById(c.getId());

        String modeloAnterior = carroAnterior
                .map(Carro::getModelo)
                .orElse("NAO INFORMADO");

        Double precoAnterior = carroAnterior
                .map(Carro::getPreco)
                .orElse(null);

        Carro carroAtualizado = carroRepository.save(c);

        logService.info(
                "ATUALIZACAO",
                "CARRO",
                carroAtualizado.getId(),
                "SUCESSO",
                "MODELO ANTERIOR: " + modeloAnterior
                        + " | MODELO NOVO: " + carroAtualizado.getModelo()
                        + " | PRECO ANTERIOR: " + precoAnterior
                        + " | PRECO NOVO: " + carroAtualizado.getPreco()
                        + " | ANO: " + carroAtualizado.getAno()
        );

        return carroAtualizado;
    }

    private void validarModelo(String modelo) {

        if (modelo == null || modelo.trim().isEmpty()) {

            logService.error(
                    "VALIDACAO",
                    "CARRO",
                    null,
                    "ERRO",
                    "MODELO VAZIO"
            );

            throw new CarroException(
                    "O modelo do carro não pode estar vazio."
            );
        }

        if (modelo.length() >= 5) {

            logService.error(
                    "VALIDACAO",
                    "CARRO",
                    null,
                    "ERRO",
                    "MODELO INVALIDO | TAMANHO: " + modelo.length()
            );

            throw new CarroException(
                    "O modelo do carro deve ter menos de 5 caracteres. "
                            + "Tamanho atual: " + modelo.length()
            );
        }
    }

    private void validarPreco(double preco) {

        if (preco < 0) {

            logService.error(
                    "VALIDACAO",
                    "CARRO",
                    null,
                    "ERRO",
                    "PRECO INVALIDO | VALOR: " + preco
            );

            throw new CarroException(
                    "O preço do carro não pode ser negativo. "
                            + "Valor fornecido: " + preco
            );
        }
    }

    private Carro localizarCarroPorModelo(String modelo) {

        validarModelo(modelo);

        Optional<Carro> carro =
                carroRepository.findFirstByModelo(modelo);

        if (carro.isEmpty()) {

            logService.warn(
                    "CONSULTA",
                    "CARRO",
                    null,
                    "ERRO",
                    "CARRO NAO ENCONTRADO | MODELO: " + modelo
            );
        }

        return carro.orElseThrow(
                () -> new CarroException(
                        "Carro com modelo " + modelo
                                + " não encontrado."
                )
        );
    }
}