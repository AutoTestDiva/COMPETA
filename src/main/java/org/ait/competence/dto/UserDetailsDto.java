package org.ait.competence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserDetailsDto {
    private String nickName;
    private String firstName;
    private String lastName;
    private String email;
   // private String roles;

}
