package com.cooperativa.votacao.scheduler;

import com.cooperativa.votacao.domain.VoteValue;
import com.cooperativa.votacao.messaging.VoteResultEvent;
import com.cooperativa.votacao.messaging.VoteResultProducer;
import com.cooperativa.votacao.repository.VoteRepository;
import com.cooperativa.votacao.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VotingSessionScheduler {

    private final VotingSessionRepository sessionRepository;
    private final VoteRepository voteRepository;
    private final VoteResultProducer voteResultProducer;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void closeExpiredSessions() {

        var now = LocalDateTime.now();

        var expiredSessions = sessionRepository
                .findByEndTimeBeforeAndClosedFalse(now);

        for (var session : expiredSessions) {

            UUID agendaId = session.getAgenda().getId();

            long yes = voteRepository.countByAgendaIdAndValue(
                    agendaId, VoteValue.YES);

            long no = voteRepository.countByAgendaIdAndValue(
                    agendaId, VoteValue.NO);

            var event = new VoteResultEvent(
                    agendaId,
                    yes,
                    no
            );

            voteResultProducer.sendResult(event);

            session.setClosed(true);
            sessionRepository.save(session);
        }
    }
}
