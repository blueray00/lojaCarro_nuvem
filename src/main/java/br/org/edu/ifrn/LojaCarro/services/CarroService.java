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

    public Carro save(Carro c) {
        logger.info("Salvando carro: {}", c.getModelo());

        validarModelo(c.getModelo());
        validarPreco(c.getPreco());

        Carro carroSalvo = carroRepository.save(c);

        logger.info("Carro salvo com ID: {}", carroSalvo.getId());

        return carroSalvo;
    }

    public void deleteById(Long id) {
        logger.info("Excluindo carro com ID: {}", id);

        if (id <= 0) {
            logger.error("Tentativa de exclusão com ID inválido: {}", id);
            throw new CarroException("O ID do carro não pode ser negativo. ID fornecido: " + id);
        }

        carroRepository.deleteById(id);

        logger.info("Carro com ID {} excluído", id);
    }

    public Optional<Carro> findById(Long id) {
        logger.info("Buscando carro com ID: {}", id);

        if (id <= 0) {
            logger.error("Tentativa de busca com ID inválido: {}", id);
            throw new CarroException("O ID do carro não pode ser negativo. ID fornecido: " + id);
        }

        Optional<Carro> carro = carroRepository.findById(id);

        if (carro.isEmpty()) {
            logger.warn("Carro com ID {} não encontrado", id);
        }

        return carro;
    }

    public List<Carro> findAll() {
        logger.info("Listando todos os carros");

        return carroRepository.findAll();
    }

    public Optional<Carro> findByModelo(String modelo) {
        logger.info("Buscando carro pelo modelo: {}", modelo);

        validarModelo(modelo);

        Optional<Carro> carro = carroRepository.findFirstByModelo(modelo);

        if (carro.isEmpty()) {
            logger.warn("Carro com modelo {} não encontrado", modelo);
        }

        return carro;
    }

    public Carro saveFromLegacy(String modelo, double preco) {
        logger.info("Salvando carro pelo método legado. Modelo: {}", modelo);

        Carro carro = new Carro(modelo, LocalDate.now().getYear(), preco);

        return save(carro);
    }

    public Carro updateByModelo(String modelo, double preco) {
        logger.info("Atualizando preço do carro com modelo: {}", modelo);

        Carro carro = localizarCarroPorModelo(modelo);

        validarPreco(preco);

        carro.setPreco(preco);

        Carro carroAtualizado = carroRepository.save(carro);

        logger.info("Preço do carro {} atualizado", modelo);

        return carroAtualizado;
    }

    public Carro deleteByModelo(String modelo) {
        logger.info("Excluindo carro pelo modelo: {}", modelo);

        Carro carro = localizarCarroPorModelo(modelo);

        carroRepository.delete(carro);

        logger.info("Carro com modelo {} excluído", modelo);

        return carro;
    }

    public Carro update(Carro c) {
        logger.info("Atualizando carro com ID: {}", c.getId());

        if (c.getId() == null) {
            logger.error("Tentativa de atualização sem ID");
            throw new CarroException("O ID do carro para atualização não pode ser nulo.");
        }

        if (!carroRepository.existsById(c.getId())) {
            logger.warn("Carro com ID {} não encontrado para atualização", c.getId());
            throw new CarroException("Carro com ID " + c.getId() + " não encontrado para atualização.");
        }

        validarModelo(c.getModelo());
        validarPreco(c.getPreco());

        Carro carroAtualizado = carroRepository.save(c);

        logger.info("Carro com ID {} atualizado", c.getId());

        return carroAtualizado;
    }

    private void validarModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            logger.error("Tentativa de salvar carro sem modelo");
            throw new CarroException("O modelo do carro não pode estar vazio.");
        }

        if (modelo.length() >= 5) {
            logger.error("Modelo de carro inválido. Tamanho: {}", modelo.length());
            throw new CarroException("O modelo do carro deve ter menos de 5 caracteres. Tamanho atual: " + modelo.length());
        }
    }

    private void validarPreco(double preco) {
        if (preco < 0) {
            logger.error("Preço de carro inválido: {}", preco);
            throw new CarroException("O preço do carro não pode ser negativo. Valor fornecido: " + preco);
        }
    }

    private Carro localizarCarroPorModelo(String modelo) {
        validarModelo(modelo);

        Optional<Carro> carro = carroRepository.findFirstByModelo(modelo);

        if (carro.isEmpty()) {
            logger.warn("Carro com modelo {} não encontrado", modelo);
        }

        return carro.orElseThrow(
                () -> new CarroException("Carro com modelo " + modelo + " não encontrado.")
        );
    }
}