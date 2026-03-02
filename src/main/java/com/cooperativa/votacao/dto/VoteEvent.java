package com.cooperativa.votacao.dto;

import com.cooperativa.votacao.domain.VoteValue;

import java.util.UUID;

public record VoteEvent(
        UUID agendaId,
        String cpf,
        VoteValue value
) {}