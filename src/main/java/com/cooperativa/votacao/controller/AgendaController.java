
package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.dto.CreateAgendaRequest;
import com.cooperativa.votacao.service.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agendas")
@RequiredArgsConstructor
public class AgendaController {
    private final AgendaService service;

    @PostMapping
    public Object create(@RequestBody CreateAgendaRequest request) {
        return service.create(request.title());
    }
}
