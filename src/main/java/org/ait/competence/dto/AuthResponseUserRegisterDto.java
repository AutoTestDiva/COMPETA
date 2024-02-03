package org.ait.competence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class AuthResponseUserRegisterDto {
    private String nickName;
    private String firstName;
    private String lastName;
    private String email;
    private String roles;
    private String avatarName;
    private String avatarImageData;
}
