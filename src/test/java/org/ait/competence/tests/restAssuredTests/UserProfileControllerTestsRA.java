package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.ait.competence.dto.PutUserProfileDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

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
        //второе предусловие, чтоб тест не упал, чтоб методу get было с чего получать ответ и использовать этот тест независимо от теста на метод put
        String userId = user.getUserIdByEmail("user1@gmail.com");
        PutUserProfileDto userProfile = PutUserProfileDto.builder()
                .firstName("Morgan")
                .lastName("Freeman")
                .build();
        given().cookie(cookie).contentType("application/json").body(userProfile).when().put("/api/user-profile/" + userId);


        //String userId = user.getUserIdByEmail("user1@gmail.com");
        given().cookie(cookie).when().get("/api/user-profile/" + userId)
                .then()
//                .log().all()
                .assertThat().statusCode(200);
//     .assertThat().body("firstName", containsString("Morgan"));
//     .assertThat().body("lastName", containsString("Freeman"));
    }

    @Test
    public void putUserProfile_code200_TestRA() throws SQLException {
        String userId = user.getUserIdByEmail("user1@gmail.com");
        PutUserProfileDto userProfile = PutUserProfileDto.builder()
                .firstName("Morgan")
                .lastName("Freeman")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(userProfile)
                .when()
                .put("/api/user-profile/" + userId)
                .then()
               // .log().all()
                .assertThat().statusCode(200);
//      .assertThat().body("firstName", containsString("Morgan"))
//     .assertThat().body("lastName", containsString("Freeman"));
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"user1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}