package org.ait.competence.dto;

import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class RegisterUserWithoutNickNameDto {
    private String message;
}
