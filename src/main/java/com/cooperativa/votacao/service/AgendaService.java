
package com.cooperativa.votacao.service;

import com.cooperativa.votacao.domain.Agenda;
import com.cooperativa.votacao.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository repo;

    public Agenda create(String title) {
        return repo.save(Agenda.builder().title(title).build());
    }
}
