
package br.org.edu.ifrn.LojaCarro.comissao;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ComissaoLogPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ComissaoLogPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarLog(String mensagem) {
        rabbitTemplate.convertAndSend("sistema.logs", mensagem);
    }
}
