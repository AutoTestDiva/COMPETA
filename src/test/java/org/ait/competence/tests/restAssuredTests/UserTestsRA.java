package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UserTestsRA extends TestBaseRA{
    private Cookie cookie;
    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // Регистрируем пользователя
        user.registerUser("user2@gmail.com", "User002!", "superUser2");
        user.userStatusConfirmed("user2@gmail.com"); //меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        cookie = user.getLoginCookie("user2@gmail.com", "User002!");
    }
    @Test
    public void putResetUserPasswordPositiveTestRA1() {
       ResetUserPasswordDto resetUserPassword= ResetUserPasswordDto.builder()
                .oldPassword("User002!")
                .newPassword("NewPassword002!")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(resetUserPassword)
                . when()
                .put("/api/user/password-reset")
                .then()
                .assertThat().statusCode(200);
    }
    @Test
    public void putNewPasswordEqualsOldTestRA1() {
        ResetUserPasswordDto resetUserPassword= ResetUserPasswordDto.builder()
                .oldPassword("User002!")
                .newPassword("User002!")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(resetUserPassword)
                . when()
                .put("/api/user/password-reset")
                .then()
                .assertThat().statusCode(400);
    }

    @Test
    public void putUserNotAuthenticatedTestRA1() {
       ResetUserPasswordDto resetUserPassword= ResetUserPasswordDto.builder()
                .oldPassword("User002!")
                .newPassword("NewPassword002!")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(resetUserPassword)
                . when()
                .put("/api/user/password-reset")
                .then()
                .assertThat().statusCode(401);
    }
    @Test
    public void getProfilePositiveTestRA(){
        given().contentType(ContentType.JSON).cookie(cookie).when().get("/api/user/me")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("email", containsString("user2@gmail.com"));

    }
    @Test
    public void getProfileNotAuthenticatedUserTestRA(){
        given().contentType(ContentType.JSON).when().get("/api/user/me")
                .then()
                .assertThat().statusCode(401);
    }

    @Test
    public void getUserConfirmCode_200_TestRA() throws SQLException {//
        String userId = user.getUserIdByEmail("user2@gmail.com");
        String userConfirmCode = user.getUserConfirmCodeById(userId);
        given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/user/email-confirmation/" + userConfirmCode)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //метод,  меняющий в таблице в БД confirmation_code в графе is_used 1 на 0,
        // т.е. чтоб CODE будто ранее не использовался
        user.cleanupDatabase("user2@gmail.com");
    }
    @Test
    public void getUserConfirmCode_404_TestRA() throws SQLException {
        String userId = user.getUserIdByEmail("invalid@gmail.com");
        String userConfirmCode = user.getUserConfirmCodeById(userId);
        given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/user/email-confirmation/" + userConfirmCode)
                .then()
                .assertThat().statusCode(404);
    }

    @Test
    public void getUserConfirmCode_409_TestRA() throws SQLException {
        String userId = user.getUserIdByEmail("user2@gmail.com");
        String userConfirmCode = user.getUserConfirmCodeById(userId);
        given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/user/email-confirmation/" + userConfirmCode);
        given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/user/email-confirmation/" + userConfirmCode)
                .then()
                .assertThat().statusCode(409);
    }


    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"user2@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}