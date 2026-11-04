
package br.org.edu.ifrn.LojaCarro.caixa;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CaixaLogPublisher {

    private final RabbitTemplate rabbitTemplate;

    public CaixaLogPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarLog(String mensagem) {
        rabbitTemplate.convertAndSend("sistema.logs", mensagem);
    }
}
