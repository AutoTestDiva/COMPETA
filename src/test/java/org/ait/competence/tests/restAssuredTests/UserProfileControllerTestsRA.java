package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.Cookie;
import org.ait.competence.dto.PutUserProfileDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;

public class UserProfileControllerTestsRA extends TestBaseRA {
    private Cookie cookie;
    String userId;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
//        user.registerUser("nata@gmail.com", "Nata2024!");
//        cookie = user.getLoginCookie("nata@gmail.com", "Nata2024!");
        user.registerUser("vasja.pupkin@competa.test", "userPass007!");
        cookie = user.getLoginCookie("vasja.pupkin@competa.test", "userPass007!");
    }

    @Test
    public void getUserProfilePositiveTestRA() throws SQLException {
        // String userId = user.getUserIdByEmail("nata@gmail.com");
        String userId = user.getUserIdByEmail("vasja.pupkin@competa.test");
        given().cookie(cookie).when().get("/api/user-profile/" + userId)
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void putUserProfilePositiveTestRA () throws SQLException {
        String userId = user.getUserIdByEmail("vasja.pupkin@competa.test");

        PutUserProfileDto userProfile = PutUserProfileDto.builder()
                .lastName("Pupkin")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(userProfile)
                . when()
                .put("/api/user-profile/" + userId)
                .then()
                .assertThat().statusCode(200);
    }
}