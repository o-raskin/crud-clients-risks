package ru.olegraskin.testtask.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClientDTO {

    private Long id;

    @NotNull(message = "riskProfile cannot be null")
    private RiskProfile riskProfile;

    public enum RiskProfile {
        LOW,
        NORMAL,
        HIGH
    }
}
