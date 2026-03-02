package com.cooperativa.votacao.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class VoteEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public VoteEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(VoteRegisteredEvent event) {

        rabbitTemplate.convertAndSend(
                "vote.exchange",
                "vote.created",
                event
        );
    }
}
