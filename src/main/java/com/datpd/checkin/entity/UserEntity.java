package com.datpd.checkin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", columnDefinition = "VARCHAR(255) COMMENT 'name of User'")
    private String name;

    @Column(name = "turn", columnDefinition = "BIGINT COMMENT 'Current turn number'")
    private long turn;
}
