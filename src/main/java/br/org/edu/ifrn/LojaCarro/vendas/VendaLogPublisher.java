package br.org.edu.ifrn.LojaCarro.vendas;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class VendaLogPublisher {

    private final RabbitTemplate rabbitTemplate;

    public VendaLogPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarLog(String mensagem) {
        rabbitTemplate.convertAndSend("sistema.logs", mensagem);
    }
}
