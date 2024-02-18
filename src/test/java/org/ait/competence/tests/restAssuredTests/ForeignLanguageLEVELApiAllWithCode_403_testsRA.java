package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostForeignLanguageLevelDto;
import org.ait.competence.dto.UpdateForeignLanguageLevelDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class ForeignLanguageLEVELApiAllWithCode_403_testsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionForTestsWithCode_403_TestsRA() throws SQLException {
        //first register a user with the role of ADMIN, otherwise we can invest ForeignLanguageLEVEL:
        user.registerUser("user5@gmail.com", "User005!", "superUser5");
        user.userStatusConfirmed("user5@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("user5@gmail.com");         //assign the ADMIN role in the database
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");
    }

    @Test
    public void putUpdateForeignLanguageLevel_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input the database Foreign Language Level as admin:
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        UpdateForeignLanguageLevelDto updateForeignLanguageLevelDto = UpdateForeignLanguageLevelDto.builder()
                .name("Intermedia")
                .build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateForeignLanguageLevelDto)
                    .when()
                    .put("/api/languageLevel/" + languageLEVELId)
                    .then()
                    .assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateForeignLanguageLevelDto)
                    .when()
                    .put("/api/languageLevel/" + languageLEVELId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteForeignLanguageById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input the database Foreign Language Level as admin:
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        // Check if the current user can delete name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/languageLevel/" + languageLEVELId)
                    .then()
                    .assertThat()
                    .statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/languageLevel/" + languageLEVELId)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(403);
        }
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");

        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}