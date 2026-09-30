package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.User;
import br.org.edu.ifrn.LojaCarro.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User criar(User user) {
        logger.info("Criando usuário: {}", user.getNome());

        User usuarioCriado = repository.save(user);

        logger.info("Usuário criado com ID: {}", usuarioCriado.getId());

        return usuarioCriado;
    }

    public List<User> listar() {
        logger.info("Listando todos os usuários");

        return repository.findAll();
    }

    public Optional<User> buscarPorId(Long id) {
        logger.info("Buscando usuário com ID: {}", id);

        Optional<User> usuario = repository.findById(id);

        if (usuario.isEmpty()) {
            logger.warn("Usuário com ID {} não encontrado", id);
        }

        return usuario;
    }

    public User atualizar(Long id, User user) {
        logger.info("Atualizando usuário com ID: {}", id);

        Optional<User> usuarioExistente = repository.findById(id);

        if (usuarioExistente.isEmpty()) {
            logger.warn("Não foi possível atualizar. Usuário com ID {} não encontrado", id);
            return null;
        }

        User usuario = usuarioExistente.get();
        usuario.setNome(user.getNome());
        usuario.setCargo(user.getCargo());

        User usuarioAtualizado = repository.save(usuario);

        logger.info("Usuário com ID {} atualizado", id);

        return usuarioAtualizado;
    }

    public boolean excluir(Long id) {
        logger.info("Excluindo usuário com ID: {}", id);

        if (!repository.existsById(id)) {
            logger.warn("Não foi possível excluir. Usuário com ID {} não encontrado", id);
            return false;
        }

        repository.deleteById(id);

        logger.info("Usuário com ID {} excluído", id);

        return true;
    }
}