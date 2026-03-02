package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.service.VoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class VoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VoteService service;

    @InjectMocks
    private VoteController controller;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void deveRetornarResultadoDaVotacaoFormatado() throws Exception {

        UUID agendaId = UUID.randomUUID();

        Map<String, Long> resultadoEsperado = Map.of(
                "YES", 15L,
                "NO", 5L
        );

        when(service.result(agendaId)).thenReturn(resultadoEsperado);

        mockMvc.perform(get("/api/v1/votes/result/{agendaId}", agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.YES").value(15))
                .andExpect(jsonPath("$.NO").value(5));

        verify(service, times(1)).result(agendaId);
    }
}