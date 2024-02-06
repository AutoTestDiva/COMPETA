package org.ait.competence.tests.restAssuredTests;

import org.ait.competence.dto.RegisterUserWithoutNickNameDto;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class AuthTestsErrorsRA extends TestBaseRA {
    @Test()
    public void registerUserWithoutNickName_400_TestRA1() throws SQLException { // Validation errors
        RegisterUserWithoutNickNameDto registerUserWithoutNickName =
                user.registerUserWithoutNickName_code400("user2@gmail.com", "User002!")
                .then().log().all()
                .assertThat().statusCode(400)
                .extract().response().as(RegisterUserWithoutNickNameDto.class);
        System.out.println(registerUserWithoutNickName.getMessage());
    }

    @Test()
    public void registerUserWithoutNickName_400_TestRA2() throws SQLException { //Validation errors
        user.registerUserWithoutNickName_code400("user2@gmail.com", "User002!")
                .then().log().all()
                .assertThat().statusCode(400);
    }
}
