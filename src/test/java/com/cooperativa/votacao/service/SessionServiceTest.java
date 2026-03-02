package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.Agenda;
import com.cooperativa.votacao.domain.VotingSession;
import com.cooperativa.votacao.repository.AgendaRepository;
import com.cooperativa.votacao.repository.VotingSessionRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionServiceTest {

    @Mock
    private VotingSessionRepository repo;

    @Mock
    private AgendaRepository agendaRepo;

    @InjectMocks
    private SessionService service;

    @Test
    public void deveAbrirSessaoComTempoInformado() {

        UUID agendaId = UUID.randomUUID();
        Integer minutos = 10;
        Agenda agenda = Agenda.builder().id(agendaId).title("Pauta Teste").build();

        when(agendaRepo.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(repo.save(any(VotingSession.class))).thenAnswer(i -> i.getArguments()[0]);

        VotingSession sessaoCriada = service.open(agendaId, minutos);

        Assert.assertEquals(agenda, sessaoCriada.getAgenda());
        Assert.assertTrue(sessaoCriada.getEndTime().isAfter(sessaoCriada.getStartTime().plusSeconds(599)));
        verify(repo).save(any(VotingSession.class));
    }

    @Test
    public void deveAbrirSessaoComTempoPadraoDeUmMinuto() {

        UUID agendaId = UUID.randomUUID();
        Agenda agenda = Agenda.builder().id(agendaId).build();

        when(agendaRepo.findById(agendaId)).thenReturn(Optional.of(agenda));

        ArgumentCaptor<VotingSession> sessionCaptor = ArgumentCaptor.forClass(VotingSession.class);

        service.open(agendaId, null);

        verify(repo).save(sessionCaptor.capture());
        VotingSession sessaoSalva = sessionCaptor.getValue();

        long diferencaMinutos = java.time.Duration.between(
                sessaoSalva.getStartTime(),
                sessaoSalva.getEndTime()
        ).toMinutes();

        Assert.assertEquals(1, diferencaMinutos);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void deveLancatExcecaoQuandoAgendaNaoExistir() {

        UUID agendaId = UUID.randomUUID();
        when(agendaRepo.findById(agendaId)).thenReturn(Optional.empty());

        service.open(agendaId, 5);
    }
}