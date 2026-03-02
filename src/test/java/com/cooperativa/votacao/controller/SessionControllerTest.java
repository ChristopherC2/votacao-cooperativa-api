package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.Agenda;
import com.cooperativa.votacao.domain.VotingSession;
import com.cooperativa.votacao.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SessionService service;

    @InjectMocks
    private SessionController controller;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void deveAbrirSessaoComSucessoERetornarObjeto() throws Exception {

        UUID agendaId = UUID.randomUUID();
        Integer minutos = 10;
        UUID sessionId = UUID.randomUUID();

        VotingSession sessaoMock = VotingSession.builder()
                .id(sessionId)
                .agenda(Agenda.builder().id(agendaId).build())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(minutos))
                .closed(false)
                .build();

        when(service.open(eq(agendaId), eq(minutos))).thenReturn(sessaoMock);

        mockMvc.perform(post("/api/v1/sessions/{agendaId}", agendaId)
                        .param("minutes", minutos.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionId.toString()))
                .andExpect(jsonPath("$.closed").value(false))
                .andExpect(jsonPath("$.agenda.id").value(agendaId.toString()));

        verify(service, times(1)).open(agendaId, minutos);
    }

    @Test
    public void deveAbrirSessaoComTempoDefaultQuandoMinutosForNulo() throws Exception {

        UUID agendaId = UUID.randomUUID();

        VotingSession sessaoMock = VotingSession.builder()
                .id(UUID.randomUUID())
                .endTime(LocalDateTime.now().plusMinutes(1)) // 1 min default
                .build();

        when(service.open(eq(agendaId), isNull())).thenReturn(sessaoMock);

        mockMvc.perform(post("/api/v1/sessions/{agendaId}", agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(service, times(1)).open(agendaId, null);
    }
}