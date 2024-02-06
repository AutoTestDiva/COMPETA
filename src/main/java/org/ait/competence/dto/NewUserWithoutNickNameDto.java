package org.ait.competence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class NewUserWithoutNickNameDto {
    private String email;
    private String password;
}
