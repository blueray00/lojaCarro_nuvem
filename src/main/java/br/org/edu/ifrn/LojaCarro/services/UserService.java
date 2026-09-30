package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.User;
import br.org.edu.ifrn.LojaCarro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final LogService logService;

    public UserService(UserRepository repository, LogService logService) {
        this.repository = repository;
        this.logService = logService;
    }

    public User criar(User user) {

        User usuarioCriado = repository.save(user);

        logService.info(
                "CADASTRO",
                usuarioCriado.getNome(),
                usuarioCriado.getId(),
                "CARGO: " + usuarioCriado.getCargo()
                        + " | RESULTADO: SUCESSO"
        );

        return usuarioCriado;
    }

    public List<User> listar() {

        List<User> usuarios = repository.findAll();

        logService.info(
                "LISTAGEM",
                "USUARIOS",
                null,
                "QUANTIDADE: " + usuarios.size()
                        + " | RESULTADO: SUCESSO"
        );

        return usuarios;
    }

    public Optional<User> buscarPorId(Long id) {

        Optional<User> usuario = repository.findById(id);

        if (usuario.isEmpty()) {

            logService.warn(
                    "CONSULTA",
                    "USUARIO",
                    id,
                    "RESULTADO: USUARIO NAO ENCONTRADO"
            );

            return Optional.empty();
        }

        logService.info(
                "CONSULTA",
                usuario.get().getNome(),
                usuario.get().getId(),
                "CARGO: " + usuario.get().getCargo()
                        + " | RESULTADO: SUCESSO"
        );

        return usuario;
    }

    public User atualizar(Long id, User user) {

        Optional<User> usuarioExistente = repository.findById(id);

        if (usuarioExistente.isEmpty()) {

            logService.warn(
                    "ATUALIZACAO",
                    user.getNome(),
                    id,
                    "RESULTADO: USUARIO NAO ENCONTRADO"
            );

            return null;
        }

        User usuario = usuarioExistente.get();

        String nomeAnterior = usuario.getNome();
        String cargoAnterior = usuario.getCargo();

        usuario.setNome(user.getNome());
        usuario.setCargo(user.getCargo());

        User usuarioAtualizado = repository.save(usuario);

        logService.info(
                "ATUALIZACAO",
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getId(),
                "NOME ANTERIOR: " + nomeAnterior
                        + " | NOME NOVO: " + usuarioAtualizado.getNome()
                        + " | CARGO ANTERIOR: " + cargoAnterior
                        + " | CARGO NOVO: " + usuarioAtualizado.getCargo()
                        + " | RESULTADO: SUCESSO"
        );

        return usuarioAtualizado;
    }

    public boolean excluir(Long id) {

        Optional<User> usuarioExistente = repository.findById(id);

        if (usuarioExistente.isEmpty()) {

            logService.warn(
                    "EXCLUSAO",
                    "USUARIO",
                    id,
                    "RESULTADO: USUARIO NAO ENCONTRADO"
            );

            return false;
        }

        User usuario = usuarioExistente.get();

        repository.deleteById(id);

        logService.info(
                "EXCLUSAO",
                usuario.getNome(),
                usuario.getId(),
                "CARGO: " + usuario.getCargo()
                        + " | RESULTADO: SUCESSO"
        );

        return true;
    }
}