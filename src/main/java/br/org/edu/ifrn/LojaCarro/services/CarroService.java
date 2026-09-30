package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.CarroException;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    private static final Logger logger = LoggerFactory.getLogger(CarroService.class);

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
                carroSalvo.getModelo(),
                carroSalvo.getId(),
                "ANO: " + carroSalvo.getAno()
                        + " | PRECO: " + carroSalvo.getPreco()
                        + " | RESULTADO: SUCESSO"
        );

        return carroSalvo;
    }

    public void deleteById(Long id) {

        if (id <= 0) {

            logService.error(
                    "EXCLUSAO",
                    "CARRO",
                    id,
                    "RESULTADO: ID INVALIDO"
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
                    "RESULTADO: CARRO NAO ENCONTRADO"
            );

            carroRepository.deleteById(id);
            return;
        }

        Carro carroExcluido = carro.get();

        carroRepository.deleteById(id);

        logService.info(
                "EXCLUSAO",
                carroExcluido.getModelo(),
                carroExcluido.getId(),
                "ANO: " + carroExcluido.getAno()
                        + " | PRECO: " + carroExcluido.getPreco()
                        + " | RESULTADO: SUCESSO"
        );
    }

    public Optional<Carro> findById(Long id) {

        if (id <= 0) {

            logService.error(
                    "CONSULTA",
                    "CARRO",
                    id,
                    "RESULTADO: ID INVALIDO"
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
                    "RESULTADO: CARRO NAO ENCONTRADO"
            );

        } else {

            logService.info(
                    "CONSULTA",
                    carro.get().getModelo(),
                    carro.get().getId(),
                    "ANO: " + carro.get().getAno()
                            + " | PRECO: " + carro.get().getPreco()
                            + " | RESULTADO: SUCESSO"
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
                "QUANTIDADE: " + carros.size()
                        + " | RESULTADO: SUCESSO"
        );

        return carros;
    }

    public Optional<Carro> findByModelo(String modelo) {

        validarModelo(modelo);

        Optional<Carro> carro = carroRepository.findFirstByModelo(modelo);

        if (carro.isEmpty()) {

            logService.warn(
                    "CONSULTA",
                    modelo,
                    null,
                    "RESULTADO: CARRO NAO ENCONTRADO"
            );

        } else {

            logService.info(
                    "CONSULTA",
                    carro.get().getModelo(),
                    carro.get().getId(),
                    "ANO: " + carro.get().getAno()
                            + " | PRECO: " + carro.get().getPreco()
                            + " | RESULTADO: SUCESSO"
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
                carroAtualizado.getModelo(),
                carroAtualizado.getId(),
                "PRECO ANTERIOR: " + precoAnterior
                        + " | PRECO NOVO: " + carroAtualizado.getPreco()
                        + " | ANO: " + carroAtualizado.getAno()
                        + " | RESULTADO: SUCESSO"
        );

        return carroAtualizado;
    }

    public Carro deleteByModelo(String modelo) {

        Carro carro = localizarCarroPorModelo(modelo);

        carroRepository.delete(carro);

        logService.info(
                "EXCLUSAO",
                carro.getModelo(),
                carro.getId(),
                "ANO: " + carro.getAno()
                        + " | PRECO: " + carro.getPreco()
                        + " | RESULTADO: SUCESSO"
        );

        return carro;
    }

    public Carro update(Carro c) {

        if (c.getId() == null) {

            logService.error(
                    "ATUALIZACAO",
                    c.getModelo(),
                    null,
                    "RESULTADO: ID NAO INFORMADO"
            );

            throw new CarroException(
                    "O ID do carro para atualização não pode ser nulo."
            );
        }

        if (!carroRepository.existsById(c.getId())) {

            logService.warn(
                    "ATUALIZACAO",
                    c.getModelo(),
                    c.getId(),
                    "RESULTADO: CARRO NAO ENCONTRADO"
            );

            throw new CarroException(
                    "Carro com ID " + c.getId()
                            + " não encontrado para atualização."
            );
        }

        validarModelo(c.getModelo());
        validarPreco(c.getPreco());

        Optional<Carro> carroAnterior = carroRepository.findById(c.getId());

        String modeloAnterior = carroAnterior
                .map(Carro::getModelo)
                .orElse("NAO INFORMADO");

        Double precoAnterior = carroAnterior
                .map(Carro::getPreco)
                .orElse(null);

        Carro carroAtualizado = carroRepository.save(c);

        logService.info(
                "ATUALIZACAO",
                carroAtualizado.getModelo(),
                carroAtualizado.getId(),
                "MODELO ANTERIOR: " + modeloAnterior
                        + " | MODELO NOVO: " + carroAtualizado.getModelo()
                        + " | PRECO ANTERIOR: " + precoAnterior
                        + " | PRECO NOVO: " + carroAtualizado.getPreco()
                        + " | ANO: " + carroAtualizado.getAno()
                        + " | RESULTADO: SUCESSO"
        );

        return carroAtualizado;
    }

    private void validarModelo(String modelo) {

        if (modelo == null || modelo.trim().isEmpty()) {

            logService.error(
                    "VALIDACAO",
                    "CARRO",
                    null,
                    "RESULTADO: MODELO VAZIO"
            );

            throw new CarroException(
                    "O modelo do carro não pode estar vazio."
            );
        }

        if (modelo.length() >= 5) {

            logService.error(
                    "VALIDACAO",
                    modelo,
                    null,
                    "RESULTADO: MODELO INVALIDO | TAMANHO: " + modelo.length()
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
                    "RESULTADO: PRECO INVALIDO | VALOR: " + preco
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
                    modelo,
                    null,
                    "RESULTADO: CARRO NAO ENCONTRADO"
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