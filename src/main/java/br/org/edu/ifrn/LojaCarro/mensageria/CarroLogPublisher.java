
package br.org.edu.ifrn.LojaCarro.mensageria;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CarroLogPublisher {

    private static final Logger logger =
            LoggerFactory.getLogger(CarroLogPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public CarroLogPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarLog(
            String quemFez,
            String acao,
            String afetado,
            Long id,
            String resultado,
            String detalhes) {

        Map<String, Object> mensagem = new LinkedHashMap<>();

        mensagem.put("quemFez", quemFez);
        mensagem.put("acao", acao);
        mensagem.put("afetado", afetado);
        mensagem.put("id", id);
        mensagem.put("resultado", resultado);
        mensagem.put("detalhes", detalhes);
        mensagem.put("dataHora", LocalDateTime.now().toString());

        try {
            rabbitTemplate.convertAndSend("sistema.logs", mensagem);

            logger.info("Log enviado ao RabbitMQ: {} - {}", acao, afetado);

        } catch (AmqpException e) {
            logger.error(
                    "Não foi possível enviar o log ao RabbitMQ. Ação: {}. Motivo: {}",
                    acao,
                    e.getMessage()
            );
        }
    }
}
