
package com.cooperativa.votacao.dto;

import com.cooperativa.votacao.domain.VoteValue;

import java.util.UUID;

public record VoteRequest(UUID agendaId, String cpf, VoteValue vote) {
}
