package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class SoftSkillTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //Changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //Assign the ADMIN role in the database
    }

    @Test()
    public void postAddSoftSkill_code201_TestRA1() throws SQLException { //SoftSkill is adding successful
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllSoftSkill)
                .when()
                .post("/api/soft-skill")
                .then()
                .assertThat().statusCode(201);
        System.out.println(postAllSoftSkill.getName());

        //The first method deleting the above soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not automatically delete soft-skills from the table:
//        String name = "team work10";
//        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        //The second option of deleting an already existing soft-skill (not through the database):
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test()
    public void postAddSoftSkill_code400_TestRA1() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllSoftSkill)
                .when()
                .post("/api/soft-skill")
                .then()
                .assertThat().statusCode(400);
    }

    @Test()
    public void postAddSoftSkill_WithInvalidEmail_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //enter wrong mail
        if (cookie != null) {
            //It's like a precondition by which we invest a skill in advance:
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");
            given().cookie(cookie).contentType(ContentType.JSON).when().post("/api/soft-skill")
                    .then()
                    .assertThat().statusCode(401);
        } else {
            // Handling the case when authentication fails:
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }


    @Test()

    public void postAddSoftSkill_code409_TestRA1() throws SQLException {//SoftSkill already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we invest a skill in advance:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //With this method, we try to re-inject a skill that we already have:
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill")
                .then()
                .assertThat().statusCode(409);

        //First method deleting the above mentioned soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not delete soft-skills from the table automatically:
        // String name = "team work10";
        //  db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // The second option of deleting an already existing soft-skill (not through the database):
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test
    public void putUpdateSoftSkillById_code200_TestRA() throws SQLException { // Updating SoftSkill is successful
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition by which we invest a skill in advance:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //With this method, I'm upgrading an existing skill:
        String softSkillId = admin.getSoftSkillById("team work10");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //Method to delete the above soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not automatically delete soft-skills from the table:
        String name = "team work1";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateSoftSkillById_code400_TestRA() throws SQLException { //Validation error
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition by which we initially invest the skillet:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //With this method we are trying to update an existing skillet with the wrong "name" in the path ("path"):
        String softSkillId = admin.getSoftSkillById("invalidName");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                .then()
                .assertThat().statusCode(400);

        //Method to delete the above soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not automatically delete soft-skills from the table:
        String name = "team work10";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateSoftSkillById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Invalid1!");
        if (cookie != null) {
            //It's like a precondition by which we initially invest the skillet:
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //This method tries to update an existing skillet with the wrong password in cookies:
            String softSkillId = admin.getSoftSkillById("team work10");
            UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                    .name("team work1")
                    .build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                    .then()
                    .assertThat().statusCode(401);
        } else {
            // Handling the case when authentication fails:
            System.out.println("User not authenticated");
        }
        //Method to delete the above soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not automatically delete soft-skills from the table:
        String name = "team work10";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putSoftSkillById_code409_TestRA() throws SQLException {//SoftSkill already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition by which we initially invest the skillet:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //With this method, I'm upgrading an existing skill with the same:
        String softSkillId = admin.getSoftSkillById("team work10");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work10")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        //Method to delete the above soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not automatically delete soft-skills from the table:
        //String name = "team work10";
        // db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // The second option of deleting an already existing soft-skill (not through the database):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test
    public void getAllSoftSkills_code200_TestRA() throws SQLException {//get all Soft Skills
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the skillet:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //Use this method to get all the skills:
        given().cookie(cookie).contentType("application/json").when().get("api/soft-skill")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //Method to delete the above soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not automatically delete soft-skills from the table:
        //String name = "team work10";
        // db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // The second option of deleting an already existing soft-skill (not through the database):
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test
    public void getAllSoftSkill_code401_TestRA() throws SQLException {                //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //enter wrong mail в coockies
        //It's like a precondition by which we initially invest the skillet:
        if (cookie != null) {
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //Use this method to get all skills:
            given().cookie(cookie).contentType("application/json").when().get("api/soft-skill")
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Handling the case when authentication fails
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
        //Method to delete the above soft-skill by "name" from the database, so that it will be automatically passed later on
        //in JENKINS-e and since deleting a user does not automatically delete soft-skills from the table:
        String name = "team work10";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void deleteSoftSkillById_code200_TestRA() throws SQLException { //deletion successful
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the skillet:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //Use this method to remove an existing skillet:
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void deleteOneSoftSkillById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!");//сделать ошибку в почте
        //It's like a precondition by which we initially invest the skillet:
        if (cookie != null) {
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //Use this method to remove an existing skillet:
            String softSkillId = admin.getSoftSkillById("team work10");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}

