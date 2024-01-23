package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.NewPasswordEqualsOldDto;
import org.ait.competence.dto.UserNotAuthenticatedDto;
import org.ait.competence.dto.UserResponseDto;
import org.testng.Assert;
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
        user.registerUser("nata@gmail.com", "Nata2024!");
        cookie = user.getLoginCookie("nata@gmail.com", "Nata2024!");
    }
    @Test
    public void resetUserPasswordPositiveTestRA1() { //нет таких полей
        UserResponseDto userResponseDto = user.resetUserPasswordRA("Nata2024!", "Nata0001!")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(UserResponseDto.class);
        System.out.println(userResponseDto.getMessage());
    }
    @Test
    public void newPasswordEqualsOldTestRA1() {  //нет таких полей
        NewPasswordEqualsOldDto newPasswordEqualsOld = user.resetUserPasswordRA("Nata2024!", "Nata2024!")
                .then()
                .assertThat().statusCode(400)
                .extract().response().as(NewPasswordEqualsOldDto.class);
        System.out.println(newPasswordEqualsOld.getMessage());
    }
    @Test
    public void userNotAuthenticatedTestRA1() {
        UserNotAuthenticatedDto userNotAuthenticated = user.resetUserPasswordRA("Nata0009!", "Nata0001!")
                .then()
                .assertThat().statusCode(401)
                .extract().response().as(UserNotAuthenticatedDto.class);
        Assert.assertEquals(userNotAuthenticated.getMessage(), "User not authenticated");
    }
    @Test
    public void gettingProfilePositiveTestRA(){
        given().contentType(ContentType.JSON).cookie(cookie).when().get("/api/user/me")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("email", containsString("nata@gmail.com"));

    }
    @Test
    public void gettingProfileNotAuthenticatedUserTestRA(){
        given().contentType(ContentType.JSON).when().get("/api/user/me")
                .then()
                .assertThat().statusCode(401);
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"nata@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}