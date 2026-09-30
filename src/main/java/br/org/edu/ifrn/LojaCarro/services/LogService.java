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

    public LogService(HttpServletRequest request) {
        this.request = request;
    }

    public String getQuemFez() {

        String usuario = request.getHeader("X-Usuario-Nome");

        if (usuario == null || usuario.trim().isEmpty()) {
            return "USUARIO_NAO_IDENTIFICADO";
        }

        return usuario;
    }

    public void info(
            String acao,
            String afetado,
            Long id,
            String detalhes) {

        logger.info(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | {}",
                getQuemFez(),
                acao,
                afetado,
                id,
                detalhes
        );
    }

    public void warn(
            String acao,
            String afetado,
            Long id,
            String detalhes) {

        logger.warn(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | {}",
                getQuemFez(),
                acao,
                afetado,
                id,
                detalhes
        );
    }

    public void error(
            String acao,
            String afetado,
            Long id,
            String detalhes) {

        logger.error(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | {}",
                getQuemFez(),
                acao,
                afetado,
                id,
                detalhes
        );
    }
}