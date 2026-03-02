package com.cooperativa.votacao.messaging;

import java.util.UUID;

public record VoteResultEvent(
        UUID agendaId,
        long yesVotes,
        long noVotes
) {}
