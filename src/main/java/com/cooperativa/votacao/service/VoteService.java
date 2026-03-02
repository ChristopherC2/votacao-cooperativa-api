
package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.*;
import com.cooperativa.votacao.messaging.VoteRegisteredEvent;
import com.cooperativa.votacao.dto.VoteRequest;
import com.cooperativa.votacao.repository.*;
import com.cooperativa.votacao.service.client.CpfValidationClient;
import com.cooperativa.votacao.messaging.VoteEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository repo;
    private final AgendaRepository agendaRepo;
    private final SessionService sessionService;
    private final CpfValidationClient cpfValidationClient;
    private final VoteEventPublisher voteEventPublisher;
    @Transactional
    public void vote(VoteRequest r) {

        var session = sessionService.getByAgenda(r.agendaId());

        if (!session.isOpen())
            throw new RuntimeException("Sessão fechada");

        if (repo.existsByAgendaIdAndCpf(r.agendaId(), r.cpf()))
            throw new RuntimeException("Voto já realizado");

        if (!cpfValidationClient.isAbleToVote(r.cpf()))
            throw new RuntimeException("CPF não está apto a votar");

        var agenda = agendaRepo.findById(r.agendaId()).orElseThrow();

        repo.save(
                Vote.builder()
                        .agenda(agenda)
                        .cpf(r.cpf())
                        .value(r.vote())
                        .build()
        );

        var event = new VoteRegisteredEvent(
                r.agendaId(),
                r.cpf(),
                r.vote().name()
        );

        voteEventPublisher.publish(event);
    }

    public Map<String, Long> result(UUID agendaId) {
        var votes = repo.findByAgendaId(agendaId);
        return Map.of(
                "YES", votes.stream().filter(v -> v.getValue() == VoteValue.YES).count(),
                "NO", votes.stream().filter(v -> v.getValue() == VoteValue.NO).count()
        );
    }
}
