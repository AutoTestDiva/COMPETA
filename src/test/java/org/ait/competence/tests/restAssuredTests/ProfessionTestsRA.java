package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostAllProfessionsDto;
import org.ait.competence.dto.UpdateProfessionNameDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class ProfessionTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    @Test()
    public void postAddNewProfession_code201_TestRA1() throws SQLException { //Profession added
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllProfession)
                .when()
                .post("/api/profession")
                .then()
                .log().all()
                .assertThat().statusCode(201);
        System.out.println(postAllProfession.getName());

        //The first method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        //       String name = "programmer1";
        //       db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing profession (not through the database):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test()
    public void postAddNewProfession_code400_TestRA1() throws SQLException { //Not valid value name EduLevel
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllProfession)
                .when()
                .post("/api/profession")
                .then()
                .log().all()
                .assertThat().statusCode(400);
    }

    @Test()
    public void postAddNewProfession_WithInvalidEmail_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .contentType("application/json")
                .body(postAllProfession)
                .when()
                .post("/api/profession")
                .then()
                .log().all()
                .assertThat().statusCode(401);
    }

    @Test()
    public void postAddNewProfession_code409_TestRA1() throws SQLException { //Profession with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllProfession)
                .when()
                .post("/api/profession");
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllProfession)
                .when()
                .post("/api/profession")
                .then()
                .log().all()
                .assertThat().statusCode(409);

        //The first method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        //       String name = "programmer1";
        //       db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing profession (not through the database):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void putUpdateProfessionById_code200_TestRA() throws SQLException { //Profession updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfession)
                .when()
                .post("/api/profession");

        //Using this method we try to re-enter an already existing profession:
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateProfessionNameDto)
                .when()
                .put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer2";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateProfessionById_code400_TestRA() throws SQLException { //Not valid value name EduLevel
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfession)
                .when()
                .post("/api/profession");

        //Using this method, try to update an existing profession with the wrong "name" in the path ("path"):
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateProfessionNameDto)
                .when()
                .put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateProfessionById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfession)
                .when()
                .post("/api/profession");

        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(updateProfessionNameDto)
                .when()
                .put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(401);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test //возможно баг
    public void putUpdateProfessionById_code404_TestRA() throws SQLException { //Profession not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfession)
                .when()
                .post("/api/profession");

        //Using this method we try to re-enter an already existing profession:
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2").build();
        given().cookie(cookie).contentType(ContentType.JSON)
                .body(updateProfessionNameDto)
                .when()
                .put("/api/profession/" + Integer.MAX_VALUE) // in the path specify a nonexistent id of a nonexistent profession
                .then().log().all().assertThat().statusCode(404);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateProfessionById_code409_TestRA() throws SQLException {//Profession with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfession)
                .when()
                .post("/api/profession");

        //With this method, I'm upgrading an existing profession with the same:
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateProfessionNameDto)
                .when()
                .put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(409);

        // The second option of deleting an already existing profession (not through the database):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void getListOfProfessions_code200_TestRA() throws SQLException {//List of profession
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the profession:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfession)
                .when()
                .post("/api/profession");

        //By this method we get all professions:
        given()
                .cookie(cookie)
                .contentType("application/json")
                .when()
                .get("api/profession/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // The second variant of deleting an already existing profession (not through the database):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void getListOfProfessions_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the profession:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfession)
                .when()
                .post("/api/profession");

        String userEmail = "admin1@gmail.com";
        if ("another@gmail.com".equals(userEmail)) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("api/profession/all")
                    .then()
                    .log().all()
                    .assertThat().statusCode(200);
        } else {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("api/profession/all")
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        }
        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test
    public void deleteProfessionById_code200_TestRA() throws SQLException { //Profession deleted
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the profession:
        PostAllProfessionsDto postAllProfessions = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfessions)
                .when()
                .post("/api/profession");

        //With this method we delete an already existing profession:
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
                .then().log().all().assertThat().statusCode(200);
    }

    @Test
    public void deleteProfessionById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the profession:
        PostAllProfessionsDto postAllProfessions = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAllProfessions)
                .when()
                .post("/api/profession");

        //Use this method to remove an existing skillet:
        String professionId = admin.getProfessionById("programmer1");
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/soft-skill/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(401);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @AfterMethod
//  @Test
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
