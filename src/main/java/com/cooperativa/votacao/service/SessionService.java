
package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.*;
import com.cooperativa.votacao.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final VotingSessionRepository repo;
    private final AgendaRepository agendaRepo;

    public VotingSession open(UUID agendaId, Integer minutes) {
        Agenda a = agendaRepo.findById(agendaId).orElseThrow();

        int m = minutes == null ? 1 : minutes;

        return repo.save(VotingSession.builder()
                .agenda(a)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(m))
                .build());
    }

    public VotingSession getByAgenda(UUID agendaId) {
        return repo.findByAgendaId(agendaId).orElseThrow();
    }
}
