package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.*;
import com.cooperativa.votacao.dto.VoteRequest;
import com.cooperativa.votacao.messaging.VoteEventPublisher;
import com.cooperativa.votacao.messaging.VoteRegisteredEvent;
import com.cooperativa.votacao.repository.AgendaRepository;
import com.cooperativa.votacao.repository.VoteRepository;
import com.cooperativa.votacao.service.client.CpfValidationClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceTest {

    @Mock private VoteRepository repo;
    @Mock private AgendaRepository agendaRepo;
    @Mock private SessionService sessionService;
    @Mock private CpfValidationClient cpfValidationClient;
    @Mock private VoteEventPublisher voteEventPublisher;

    @InjectMocks
    private VoteService service;

    private VoteRequest request;
    private UUID agendaId;

    @Before
    public void setup() {
        agendaId = UUID.randomUUID();
        request = new VoteRequest(agendaId, "12345678901", VoteValue.YES);
    }

    @Test
    public void deveVotarComSucesso() {

        VotingSession session = VotingSession.builder()
                .endTime(LocalDateTime.now().plusMinutes(5))
                .build();

        when(sessionService.getByAgenda(agendaId)).thenReturn(session);
        when(repo.existsByAgendaIdAndCpf(agendaId, request.cpf())).thenReturn(false);
        when(cpfValidationClient.isAbleToVote(request.cpf())).thenReturn(true);
        when(agendaRepo.findById(agendaId)).thenReturn(Optional.of(Agenda.builder().id(agendaId).build()));

        when(repo.save(any(Vote.class))).thenAnswer(i -> {
            Vote v = i.getArgument(0);
            return v;
        });

        service.vote(request);

        verify(repo, times(1)).save(any(Vote.class));
        verify(voteEventPublisher, times(1)).publish(any(VoteRegisteredEvent.class));
    }

    @Test(expected = RuntimeException.class)
    public void deveLancarExcecaoQuandoSessaoFechada() {
        VotingSession sessionFechada = VotingSession.builder()
                .endTime(LocalDateTime.now().minusMinutes(1))
                .build();

        when(sessionService.getByAgenda(agendaId)).thenReturn(sessionFechada);

        service.vote(request);
    }

    @Test(expected = RuntimeException.class)
    public void deveLancarExcecaoQuandoCpfJaVotou() {

        VotingSession session = VotingSession.builder().endTime(LocalDateTime.now().plusMinutes(5)).build();
        when(sessionService.getByAgenda(agendaId)).thenReturn(session);

        when(repo.existsByAgendaIdAndCpf(agendaId, request.cpf())).thenReturn(true);

        service.vote(request);
    }

    @Test
    public void deveCalcularResultadoCorretamente() {

        List<Vote> votosMock = List.of(
                Vote.builder().value(VoteValue.YES).build(),
                Vote.builder().value(VoteValue.YES).build(),
                Vote.builder().value(VoteValue.NO).build()
        );

        when(repo.findByAgendaId(agendaId)).thenReturn(votosMock);

        Map<String, Long> resultado = service.result(agendaId);

        Assert.assertEquals(Long.valueOf(2), resultado.get("YES"));
        Assert.assertEquals(Long.valueOf(1), resultado.get("NO"));
    }
}