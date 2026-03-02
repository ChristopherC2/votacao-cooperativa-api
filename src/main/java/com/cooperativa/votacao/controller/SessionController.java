
package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService service;

    @PostMapping("/{agendaId}")
    public Object open(@PathVariable UUID agendaId, @RequestParam(required = false) Integer minutes) {
        return service.open(agendaId, minutes);
    }
}
