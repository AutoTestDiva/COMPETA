package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostTitleOfJobDto;
import org.ait.competence.dto.UpdateTitleOfJobDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class TitleOfJobAllWithCode_403_testsRA extends TestBaseRA{
    private Cookie cookie;

    @BeforeMethod
    public void preconditionForTestsWithCode_403_TestsRA() throws SQLException {
        //First register a user with the ADMIN role, otherwise we won't be able to attach a job-title:
        user.registerUser("user5@gmail.com", "User005!", "superUser5");
        user.userStatusConfirmed("user5@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("user5@gmail.com");         //Assign the ADMIN role in the database
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");
    }

     @Test
    public void putUpdateTitleOfJobById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
         //One more precondition:
         //Input the job-title into the job-title database as admin:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder()
                .name("junior1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postTitleOfJob)
                .when()
                .post("/api/job-title");

         //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
         //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

         //The method itself
        String jobTitleId = admin.getJobTitleIdById("junior1");
        UpdateTitleOfJobDto updateTitleOfJobDto = UpdateTitleOfJobDto.builder()
                .name("junior2")
                .build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateTitleOfJobDto)
                    .when()
                    .put("/api/job-title/" + jobTitleId)
                    .then()
                    .assertThat()
                    .statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateTitleOfJobDto)
                    .when()
                    .put("/api/job-title/" + jobTitleId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteTitleOfJobById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input the job-title into the job-title database as admin:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder()
                .name("junior1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postTitleOfJob)
                .when()
                .post("/api/job-title");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String jobTitleId = admin.getJobTitleIdById("junior1");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/job-title/" + jobTitleId)
                    .then()
                    .assertThat()
                    .statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when().delete("/api/job-title/" + jobTitleId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @AfterMethod
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing job_title from DataBase, we're scrubbing the database:
        String name = "junior1";
        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");

        //Delete the user from the 4 users tables in the database
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
