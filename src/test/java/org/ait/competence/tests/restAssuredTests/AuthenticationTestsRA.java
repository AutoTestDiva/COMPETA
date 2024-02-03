package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.Cookie;
import org.ait.competence.dto.AuthResponseDto;
import org.ait.competence.dto.LogoutResponseDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AuthenticationTestsRA extends TestBaseRA{
    private Cookie cookie;
    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // Регистрируем пользователя
        user.registerUser("user2@gmail.com", "User002!", "superUser2");
        user.userStatusConfirmed("user2@gmail.com"); //меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        cookie = user.getLoginCookie("user2@gmail.com", "User002!");
    }

    @Test
    public void loginAsUserPositiveTestRA1() {
        AuthResponseDto responseDto = user.loginUserRA("user2@gmail.com", "User002!")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);
        System.out.println(responseDto.getMessage());
    }
    @Test
    public void loginAsUserPositiveTestRA2() {
        user.loginUserRA("user2@gmail.com", "User002!")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Login successful"));
    }
    @Test
    public void loginAsUserWithIncorrectPasswordTestRA1() {
        AuthResponseDto responseDto = user.loginUserRA("user2@gmail.com", "Invalid")
                .then()
                .assertThat().statusCode(401)
                .extract().response().as(AuthResponseDto.class);
        System.out.println(responseDto.getMessage());
    }
    @Test
    public void loginAsUserWithIncorrectPasswordTestRA2() {
        user.loginUserRA("user2@gmail.com", "Invalid")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Incorrect username or password"));
    }

    @Test
    public void logoutAsUserPositiveTestRA1() {
        LogoutResponseDto response = given().cookie(cookie).when().post("/api/logout")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(LogoutResponseDto.class);
        System.out.println(response.getMessage());
    }
    @Test
    public void logoutAsUserPositiveTestRA2() {
        given().cookie(cookie).when().post("/api/logout")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Logout successful"));
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"user2@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}