package com.cooperativa.votacao.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class VoteResultProducer {
    private final RabbitTemplate rabbitTemplate;

    public VoteResultProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendResult(VoteResultEvent event) {
        rabbitTemplate.convertAndSend(
                "votacao.exchange",
                "votacao.result",
                event
        );
    }
}
