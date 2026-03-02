package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.Agenda;
import com.cooperativa.votacao.repository.AgendaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AgendaServiceTest {

    @Mock
    private AgendaRepository repo;

    @InjectMocks
    private AgendaService service;

    @Test
    public void deveSalvarAgendaComSucesso() {

        String tituloPauta = "Nova Pauta de Votação";
        UUID idSimulado = UUID.randomUUID();

        Agenda agendaSalva = Agenda.builder()
                .id(idSimulado)
                .title(tituloPauta)
                .build();

        when(repo.save(any(Agenda.class))).thenReturn(agendaSalva);

        Agenda resultado = service.create(tituloPauta);

        Assert.assertNotNull(resultado);
        Assert.assertEquals(idSimulado, resultado.getId());
        Assert.assertEquals(tituloPauta, resultado.getTitle());

        ArgumentCaptor<Agenda> agendaCaptor = ArgumentCaptor.forClass(Agenda.class);
        verify(repo).save(agendaCaptor.capture());

        Agenda agendaEnviadaAoRepo = agendaCaptor.getValue();
        Assert.assertEquals(tituloPauta, agendaEnviadaAoRepo.getTitle());
    }
}