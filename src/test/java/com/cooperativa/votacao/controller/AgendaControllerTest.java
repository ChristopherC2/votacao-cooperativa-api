package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.domain.Agenda;
import com.cooperativa.votacao.service.AgendaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AgendaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AgendaService service;

    @InjectMocks
    private AgendaController controller;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void deveCriarUmaPautaERetornarObjetoComUUID() throws Exception {

        String tituloFornecido = "Assembleia 2026";
        UUID idGerado = UUID.randomUUID();

        Agenda agendaRetorno = Agenda.builder()
                .id(idGerado)
                .title(tituloFornecido)
                .build();

        when(service.create(eq(tituloFornecido))).thenReturn(agendaRetorno);

        mockMvc.perform(post("/api/v1/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tituloFornecido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idGerado.toString()))
                .andExpect(jsonPath("$.title").value(tituloFornecido));

        verify(service, times(1)).create(tituloFornecido);
    }
}