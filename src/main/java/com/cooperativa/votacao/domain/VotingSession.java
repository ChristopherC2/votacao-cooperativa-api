
package com.cooperativa.votacao.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Agenda agenda;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean closed = false;

    public boolean isOpen() {
        return LocalDateTime.now().isBefore(endTime);
    }
}
