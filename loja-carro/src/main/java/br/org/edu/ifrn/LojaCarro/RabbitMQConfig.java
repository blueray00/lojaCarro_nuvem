package br.org.edu.ifrn.LojaCarro;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue filaLogs() {
        return new Queue("sistema.logs", true);
    }
}
