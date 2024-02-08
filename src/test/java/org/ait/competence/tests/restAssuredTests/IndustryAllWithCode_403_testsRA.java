package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostIndustryDto;
import org.ait.competence.dto.UpdateIndustryDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class IndustryAllWithCode_403_testsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionForTestsWithCode_403_TestsRA() throws SQLException {
        //First register a user with the ADMIN role, otherwise we won't be able to attach edu-level:
        user.registerUser("user5@gmail.com", "User005!", "superUser5");
        user.userStatusConfirmed("user5@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("user5@gmail.com");         //Assign the ADMIN role in the database
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");
    }

    @Test
    public void postAddNewIndustry_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Then change the user role from the ADMIN role to the USER role:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status.
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        PostIndustryDto postIndustry = PostIndustryDto.builder().name("education1").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postIndustry)
                    .when()
                    .post("/api/industry")
                    .then()
                    .assertThat()
                    .statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postIndustry)
                    .when()
                    .post("/api/industry")
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void putUpdateIndustryById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input to the industry database as admin:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postIndustry)
                .when()
                .post("/api/industry");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String industryId = admin.getIndustryById("education1");
        UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder()
                .name("education2")
                .build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateIndustryDto)
                    .when()
                    .put("/api/industry/" + industryId)
                    .then()
                    .assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateIndustryDto)
                    .when()
                    .put("/api/industry/" + industryId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteIndustryById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input to the industry database as admin:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postIndustry)
                .when()
                .post("/api/industry");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com");//Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String industryId = admin.getIndustryById("education1");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/industry/" + industryId)
                    .then()
                    .assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/industry/" + industryId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @AfterMethod
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing industry from DataBase, clean up the database:
        String name = "education1";
        db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");

        //Delete the user from the 4 users tables in the database
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
