
package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.mensageria.CarroLogPublisher;
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
    private final CarroLogPublisher carroLogPublisher;

    public LogService(
            HttpServletRequest request,
            UsuarioApiService usuarioApiService,
            CarroLogPublisher carroLogPublisher) {

        this.request = request;
        this.usuarioApiService = usuarioApiService;
        this.carroLogPublisher = carroLogPublisher;
    }


    public String getQuemFez() {
        return "RAYNARA";
    }


    public void info(
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        String quemFez = getQuemFez();

        logger.info(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | RESULTADO: {}{}",
                quemFez,
                acao,
                afetado,
                id,
                resultado,
                detalhes == null || detalhes.isEmpty()
                        ? ""
                        : " | " + detalhes
        );

        enviarLogRabbitMQ(
                quemFez, acao, afetado, id, resultado, detalhes
        );
    }

    public void warn(
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        String quemFez = getQuemFez();

        logger.warn(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | RESULTADO: {}{}",
                quemFez,
                acao,
                afetado,
                id,
                resultado,
                detalhes == null || detalhes.isEmpty()
                        ? ""
                        : " | " + detalhes
        );

        enviarLogRabbitMQ(
                quemFez, acao, afetado, id, resultado, detalhes
        );
    }

    public void error(
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        String quemFez = getQuemFez();

        logger.error(
                "QUEM FEZ: {} | ACAO: {} | AFETADO: {} | ID: {} | RESULTADO: {}{}",
                quemFez,
                acao,
                afetado,
                id,
                resultado,
                detalhes == null || detalhes.isEmpty()
                        ? ""
                        : " | " + detalhes
        );

        enviarLogRabbitMQ(
                quemFez, acao, afetado, id, resultado, detalhes
        );
    }

    private void enviarLogRabbitMQ(
            String quemFez,
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        try {
            carroLogPublisher.enviarLog(
                    quemFez,
                    acao,
                    afetado,
                    id,
                    resultado,
                    detalhes
            );

        } catch (Exception e) {

            logger.error(
                    "Erro ao enviar log para o RabbitMQ. Ação: {}",
                    acao,
                    e
            );
        }
    }
}
