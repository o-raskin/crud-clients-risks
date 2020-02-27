package ru.olegraskin.testtask.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Client {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RiskProfile riskProfile;

    public enum RiskProfile {
        LOW,
        NORMAL,
        HIGH
    }
}
