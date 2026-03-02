
package com.cooperativa.votacao.controller;

import com.cooperativa.votacao.dto.VoteRequest;
import com.cooperativa.votacao.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService service;

    @PostMapping
    public void vote(@RequestBody VoteRequest r) {
        service.vote(r);
    }

    @GetMapping("/result/{agendaId}")
    public Object result(@PathVariable UUID agendaId) {
        return service.result(agendaId);
    }
}
