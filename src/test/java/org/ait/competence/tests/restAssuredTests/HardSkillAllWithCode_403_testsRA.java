package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostAddHardSkillDto;
import org.ait.competence.dto.UpdateHardSkillDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class HardSkillAllWithCode_403_testsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionForTestsWithCode_403_TestsRA() throws SQLException {
        //сначала регистрируем user с ролью АДМИНА, иначене сможем вложить edu-level:
        user.registerUser("user5@gmail.com", "User005!", "superUser5");
        user.userStatusConfirmed("user5@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("user5@gmail.com");         //присваиваем в базе данных роль ADMIN
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");
    }

    @Test
    public void postAddNewHardSkill_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Then change the user role from ADMIN to USER:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java")
                .build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postAddHardSkill)
                    .when()
                    .post("/api/hard-skill").then().assertThat().statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postAddHardSkill)
                    .when()
                    .post("/api/hard-skill")
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void putUpdateHardSkillById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input hard-skill into the database as admin:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddHardSkill)
                .when()
                .post("/api/hard-skill");

        //Then change the user role from the ADMIN role to the USER role:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status.
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String hardSkillId = admin.getHardSkillById("Java");
        UpdateHardSkillDto updateHardSkillDto = UpdateHardSkillDto.builder()
                .name("C++")
                .build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateHardSkillDto)
                    .when()
                    .put("/api/hard-skill/" + hardSkillId)
                    .then()
                    .assertThat()
                    .statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(updateHardSkillDto).when()
                    .put("/api/hard-skill/" + hardSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteHardSkillById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //One more precondition:
        //Input hard-skill into the database as admin:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddHardSkill)
                .when()
                .post("/api/hard-skill");

        //Then change the user role from ADMIN to USER: add it to the database as admin:
        user.userRole("user5@gmail.com"); //Assign the USER role in the database
        //Update the cookie so that the user already has the USER status.
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //The method itself
        String hardSkillId = admin.getHardSkillById("Java");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/hard-skill/" + hardSkillId)
                    .then()
                    .assertThat()
                    .statusCode(200);
        } else {
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/api/hard-skill/" + hardSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        }
    }

    @AfterMethod
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing hard-skill from DataBase, clean up the database:
        String name = "Java";
        db.executeUpdate("DELETE FROM `hard_skill` WHERE `name` = '" + name + "';");

        //Delete the user from the 4 users tables in the database
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}