
package com.cooperativa.votacao.repository;

import com.cooperativa.votacao.domain.Vote;
import com.cooperativa.votacao.domain.VoteValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    boolean existsByAgendaIdAndCpf(UUID agendaId, String cpf);

    List<Vote> findByAgendaId(UUID agendaId);

    long countByAgendaIdAndValue(UUID agendaId, VoteValue value);
}
