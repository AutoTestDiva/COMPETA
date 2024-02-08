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

public class HardSkillTestsRA extends TestBaseRA{
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }
    @Test()
    public void postAddHardSkill_code201_TestRA1() throws SQLException { //Profession added
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().cookie(cookie).contentType("application/json").body(postAddHardSkill).when()
                .post("/api/hard-skill").then().log().all().assertThat().statusCode(201);

        // The variant of deleting an already existing hard-skill (not through the database):
        String hardSkillId = admin.getHardSkillById("Java");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/hard-skill/" + hardSkillId);
    }
    @Test()
    public void postAddHardSkill_code400_TestRA1() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAddHardSkill)
                .when()
                .post("/api/hard-skill")
                .then().log().all()
                .assertThat().statusCode(400);
    }
    @Test()
    public void postAddHardSkill_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().contentType("application/json").body(postAddHardSkill).when()
                .post("/api/hard-skill").then().log().all().assertThat().statusCode(401);
    }

 @Test()
 public void postAddHardSkill_code409_TestRA1() throws SQLException { //HardSkill with that name already exists
     cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
     PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
             .name("Java").build();
     given().cookie(cookie).contentType("application/json").body(postAddHardSkill).when()
             .post("/api/hard-skill");
     given().cookie(cookie).contentType("application/json").body(postAddHardSkill).when()
             .post("/api/hard-skill").then().log().all().assertThat().statusCode(409);

     // The variant of deleting an already existing hard-skill (not through the database):
     String hardSkillId = admin.getHardSkillById("Java");
     given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/hard-skill/" + hardSkillId);
 }

    @Test
    public void putUpdateHardSkillById_code200_TestRA() throws SQLException { //Hard Skill updating successful
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the hard-skill in advance:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //Using this method we try to re-enter an already existing hard-skill:
        String hardSkillId = admin.getHardSkillById("Java");
        UpdateHardSkillDto updateHardSkillDto = UpdateHardSkillDto.builder()
                .name("C++")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateHardSkillDto).when().put("/api/hard-skill/" + hardSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //The  method that deletes the above mentioned hard-skill by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the hard-skill from the table automatically:
        String name = "C++";
        db.executeUpdate("DELETE FROM `hard_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateHardSkillById_code400_TestRA() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //This is like a precondition, by which we enter the hard-skill in advance:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //Using this method, try to update an existing hard-skill with the wrong "name" in the path ("path"):
        String hardSkillId = admin.getHardSkillById("Java");
        UpdateHardSkillDto updateHardSkillDto = UpdateHardSkillDto.builder()
                .name("")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateHardSkillDto).when().put("/api/hard-skill/" + hardSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        //The  method that deletes the above mentioned hard-skill by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the hard-skill from the table automatically:
        String name = "Java";
        db.executeUpdate("DELETE FROM `hard_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateHardSkillById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the hard-skill in advance:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //Using this method we try to re-enter an already existing hard-skill:
        String hardSkillId = admin.getHardSkillById("Java");
        UpdateHardSkillDto updateHardSkillDto = UpdateHardSkillDto.builder()
                .name("C++")
                .build();
        given().contentType(ContentType.JSON).body(updateHardSkillDto).when().put("/api/hard-skill/" + hardSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(401);

        //The  method that deletes the above mentioned hard_skill by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the hard_skill from the table automatically:
        String name = "Java";
        db.executeUpdate("DELETE FROM `hard_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateHardSkillById_code409_TestRA() throws SQLException {//Profession with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //This is like a precondition, by which we enter the hard-skill in advance:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //With this method we try to update an already existing hard-skill with the same name:
        String hardSkillId = admin.getHardSkillById("Java");
        UpdateHardSkillDto updateHardSkillDto = UpdateHardSkillDto.builder()
                .name("Java")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateHardSkillDto).when().put("/api/hard-skill/" + hardSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // The variant of deleting an already existing hard-skill (not through the database):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/hard-skill/" + hardSkillId);
    }

    @Test
    public void getAllHardSkills_code200_TestRA() throws SQLException {//get all Hard Skills
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the hard-skill in advance:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder().name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //By this method we get all hard-skills:
        given().cookie(cookie).contentType("application/json").when().get("api/hard-skill")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // The variant of deleting an already existing hard-skill (not through the database):
        String hardSkillId = admin.getHardSkillById("Java");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/hard-skill/" + hardSkillId);
    }

    @Test
    public void getAllHardSkills_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the hard-skill in advance:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder().name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //By this method we get all hard-skills:
        given().contentType("application/json").when().get("api/hard-skill")
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // The variant of deleting an already existing hard-skill (not through the database):
        String hardSkillId = admin.getHardSkillById("Java");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/hard-skill/" + hardSkillId);

        //The  method that deletes the above mentioned hard-skill by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the hard-skill from the table automatically:
        String name = "Java";
        db.executeUpdate("DELETE FROM `hard_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void deleteHardSkillById_code200_TestRA() throws SQLException { //HardSkill deletion successful
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the hard-skill:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //With this method we delete an already existing hard-skill:
        String hardSkillId = admin.getHardSkillById("Java");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/hard-skill/" + hardSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void deleteHardSkillById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the hard-skill:
        PostAddHardSkillDto postAddHardSkill = PostAddHardSkillDto.builder()
                .name("Java").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddHardSkill).when().post("/api/hard-skill");

        //With this method we delete an already existing hard-skill:
        String hardSkillId = admin.getHardSkillById("Java");
        given().contentType(ContentType.JSON).when().delete("/api/hard-skill/" + hardSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(401);
        String name = "Java";
        db.executeUpdate("DELETE FROM `hard_skill` WHERE `name` = '" + name + "';");
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
