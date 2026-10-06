package br.org.edu.ifrn.LojaCarro.services;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private static final Logger logger =
            LoggerFactory.getLogger(LogService.class);

    private final HttpServletRequest request;
    private final UsuarioApiService usuarioApiService;

    public LogService(
            HttpServletRequest request,
            UsuarioApiService usuarioApiService) {

        this.request = request;
        this.usuarioApiService = usuarioApiService;
    }

    public String getQuemFez() {

        String usuarioId = request.getHeader("X-Usuario-Id");

        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            return "USUARIO_NAO_IDENTIFICADO";
        }

        try {
            Long id = Long.parseLong(usuarioId);

            return usuarioApiService.buscarNomePorId(id);

        } catch (NumberFormatException e) {

            return "USUARIO_ID_INVALIDO";

        } catch (Exception e) {

            logger.error(
                    "Erro ao consultar usuário na LojaUsuario. ID: {}",
                    usuarioId,
                    e
            );

            return "USUARIO_NAO_IDENTIFICADO";
        }
    }

    public void info(
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        logger.info(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | RESULTADO: {}{}",
                getQuemFez(),
                acao,
                afetado,
                id,
                resultado,
                detalhes == null || detalhes.isEmpty()
                        ? ""
                        : " | " + detalhes
        );
    }

    public void warn(
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        logger.warn(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | RESULTADO: {}{}",
                getQuemFez(),
                acao,
                afetado,
                id,
                resultado,
                detalhes == null || detalhes.isEmpty()
                        ? ""
                        : " | " + detalhes
        );
    }

    public void error(
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        logger.error(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | RESULTADO: {}{}",
                getQuemFez(),
                acao,
                afetado,
                id,
                resultado,
                detalhes == null || detalhes.isEmpty()
                        ? ""
                        : " | " + detalhes
        );
    }
}