package com.lamdangfixbug.qmshoe.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String token;
    private boolean isRevoke;
    private String belongToUser;
    @Enumerated(EnumType.STRING)
    private TokenType type;
}
