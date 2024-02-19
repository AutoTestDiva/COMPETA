package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class ForeignLanguageApiAllWithCode_403_testsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionForTestsWithCode_403_TestsRA() throws SQLException {
        //first register a user with the role of ADMIN, otherwise we can't put ForeignLanguage:
        user.registerUser("user5@gmail.com", "User005!", "superUser5");
        user.userStatusConfirmed("user5@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("user5@gmail.com");         //assign the ADMIN role in the database
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");
    }

    @Test
    public void postForeignLanguage_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postForeignLanguage)
                    .when()
                    .post("/api/language").then().assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postForeignLanguage)
                    .when()
                    .post("/api/language")
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void putUpdateForeignLanguage_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input the database Foreign Language as admin:
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String languageId = admin.getLanguageById("english");
        UpdateForeignLanguageDto updateForeignLanguageDto = UpdateForeignLanguageDto.builder()
                .name("french")
                .build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateForeignLanguageDto)
                    .when()
                    .put("/api/language/" + languageId)
                    .then()
                    .assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateForeignLanguageDto)
                    .when()
                    .put("/api/language/" + languageId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteForeignLanguageById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input the database Foreign Language as admin:
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String languageId = admin.getLanguageById("english");
        // Check if the current user can delete name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/language/" + languageId)
                    .then()
                    .assertThat()
                    .statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/language/" + languageId)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(403);
        }
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        // deleting an already existing foreign_language from DataBase, clean up the database:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");

        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}