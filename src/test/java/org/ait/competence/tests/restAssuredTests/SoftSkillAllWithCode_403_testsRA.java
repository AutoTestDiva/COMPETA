package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostAllSoftSkillDto;
import org.ait.competence.dto.UpdateSoftSkillNameDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class SoftSkillAllWithCode_403_testsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionForTestsWithCode_403_TestsRA() throws SQLException {
        //сначала регистрируем user с ролью АДМИНА, иначене сможем вложить edu-level:
        user.registerUser("user5@gmail.com", "User005!", "superUser5");
        user.userStatusConfirmed("user5@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("user5@gmail.com");        //Assign the ADMIN role in the database
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");
    }

    @Test
    public void postAddNewSoftSkill_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status.
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10")
                .build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postAllSoftSkill)
                    .when()
                    .post("/api/soft-skill")
                    .then()
                    .assertThat().statusCode(201);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postAllSoftSkill)
                    .when()
                    .post("/api/soft-skill")
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void putUpdateSoftSkillById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Enter the soft-skill database as admin:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllSoftSkill)
                .when()
                .post("/api/soft-skill");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status.
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String softSkillId = admin.getSoftSkillById("team work10");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work2")
                .build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateSoftSkillNameDto)
                    .when()
                    .put("/api/soft-skill/" + softSkillId)
                    .then()
                    .assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateSoftSkillNameDto).when()
                    .put("/api/soft-skill/" + softSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteSoftSkillById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Enter the soft-skill database as admin:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllSoftSkill)
                .when()
                .post("/api/soft-skill");

        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String softSkillId = admin.getSoftSkillById("team work10");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/soft-skill/" + softSkillId)
                    .then()
                    .assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/soft-skill/" + softSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @AfterMethod
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing soft-skill from DataBase, we're scrubbing the database:
        String name = "team work10";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        //Delete the user from the 4 users tables in the database
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
