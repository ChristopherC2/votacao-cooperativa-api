
package com.cooperativa.votacao.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"agenda_id", "cpf"}))
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Agenda agenda;

    private String cpf;

    @Enumerated(EnumType.STRING)
    private VoteValue value;
}
