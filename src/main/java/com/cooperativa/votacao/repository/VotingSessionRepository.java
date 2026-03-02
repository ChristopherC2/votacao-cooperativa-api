
package com.cooperativa.votacao.repository;

import com.cooperativa.votacao.domain.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VotingSessionRepository extends JpaRepository<VotingSession, UUID> {
    Optional<VotingSession> findByAgendaId(UUID agendaId);
    List<VotingSession> findByEndTimeBeforeAndClosedFalse(LocalDateTime now);
}
