package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.Cookie;
import org.ait.competence.dto.PutUserProfileDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class UserProfileControllerTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // Registering a user:
        user.registerUser("user1@gmail.com", "User001!", "superUser1");
        user.userStatusConfirmed("user1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        cookie = user.getLoginCookie("user1@gmail.com", "User001!");
    }

    @Test
    public void getUserProfile_code200_TestRA() throws SQLException {
        String userId = user.getUserIdByEmail("user1@gmail.com");
        given().cookie(cookie).when().get("/api/user-profile/" + userId)
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void putUserProfile_code200_TestRA() throws SQLException {
        String userId = user.getUserIdByEmail("user1@gmail.com");
        PutUserProfileDto userProfile = PutUserProfileDto.builder()
                .nickName("superUser1")
                .firstName("Morgan")
                .lastName("Freeman")
                .email("user1@gmail.com")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(userProfile)
                .when()
                .put("/api/user-profile/" + userId)
                .then()
                .assertThat().statusCode(200);
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"user1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}