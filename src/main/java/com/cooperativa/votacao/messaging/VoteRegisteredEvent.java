package com.cooperativa.votacao.messaging;

import java.util.UUID;

public record VoteRegisteredEvent(
        UUID agendaId,
        String cpf,
        String voteValue
) {}
