package org.ait.competence.tests.restAssuredTests;

import org.ait.competence.dto.ExistEmailResponseDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;

public class AuthTestsErrorsRA extends TestBaseRA {
    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // Регистрируем пользователя
        user.registerUser("user2@gmail.com", "User002!", "superUser2");
        user.userStatusConfirmed("user2@gmail.com"); //меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
    }
    @Test()
    public void registerUserWithExistEmail_400_TestRA1() throws SQLException {
        ExistEmailResponseDto existEmail = user.registerUser("user2@gmail.com", "User002!", "superUser2")
                .then()
                .assertThat().statusCode(400)
                .extract().response().as(ExistEmailResponseDto.class);
        System.out.println(existEmail.getMessage());
    }
    @Test()
    public void registerUserWithExistEmail_400_TestRA2() throws SQLException {
        user.registerUser("user2@gmail.com", "User002!", "superUser2")

                .then()
                .assertThat().statusCode(400);
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"user2@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
