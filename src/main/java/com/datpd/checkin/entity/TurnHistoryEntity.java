package com.datpd.checkin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "turn_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id", columnDefinition = "BIGINT COMMENT 'The ID of the user'")
    private long userId;

    @Column(name = "amount", columnDefinition = "BIGINT COMMENT 'The number of turns is added'")
    private long amount;

    @Column(name = "balance", columnDefinition = "BIGINT COMMENT 'The turn balance at the point is added'")
    private long balance;

    @Column(name = "create_at", columnDefinition = "TIMESTAMP COMMENT 'Date created'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
}
