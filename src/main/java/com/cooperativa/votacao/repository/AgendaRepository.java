
package com.cooperativa.votacao.repository;

import com.cooperativa.votacao.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgendaRepository extends JpaRepository<Agenda, UUID> {
}
