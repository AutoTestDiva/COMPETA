package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostAddNewEduLevelDto;
import org.ait.competence.dto.UpdateEduLevelDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class EduLevelTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    @Test()
    public void postAddNewEduLevel_code201_TestRA1() throws SQLException { //Education level added
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education1")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level")
                .then()
                .log().all()
                .assertThat().statusCode(201);
        System.out.println(postAddNewEduLevel.getName());

        // The variant of deleting an already existing eduLevel (not through the database):
        String eduLevelId = admin.getEduLevelById("higher education1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId);
    }

    @Test()
    public void postAddNewEduLevel_code400_TestRA1() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level")
                .then()
                .log().all()
                .assertThat().statusCode(400);
    }

    @Test()
    public void postAddNewEduLevel_WithInvalidEmail_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education1")
                .build();
        given()
                .contentType("application/json")
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level")
                .then()
                .log().all()
                .assertThat().statusCode(401);
    }

    @Test()
    public void postAddNewEduLevel_code409_TestRA1() throws SQLException {//EduLevel with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put in beforehand:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //With this method we are trying to re-invest the existing industry:
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level")
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // The variant of deleting an already existing eduLevel (not through the database):
        String eduLevelId = admin.getEduLevelById("higher education");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId);
    }

    @Test
    public void putUpdateEduLevelById_code200_TestRA() throws SQLException { //Industry updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put in beforehand:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //Using this method we try to re-enter an already existing edu-level:
        String eduLevelId = admin.getEduLevelById("higher education");
        UpdateEduLevelDto updateEduLevelDto = UpdateEduLevelDto.builder()
                .name("school education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateEduLevelDto)
                .when()
                .put("/api/edu-level/" + eduLevelId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // deleting an already existing industry:
        String name = "school education";
        db.executeUpdate("DELETE FROM `edu_level` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateEduLevelById_code400_TestRA() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition that we put in beforehand:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //Using this method, try to update an existing industry with the wrong "name" in the path ("path"):
        String eduLevelId = admin.getEduLevelById("higher education");
        UpdateEduLevelDto updateEduLevelDto = UpdateEduLevelDto.builder()
                .name("")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateEduLevelDto).when().put("/api/edu-level/" + eduLevelId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        // deleting an already existing edu-level:
        String name = "higher education";
        db.executeUpdate("DELETE FROM `edu_level` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateEduLevelById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put in beforehand:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //Using this method we try to re-enter an already existing edu-level:
        String eduLevelId = admin.getEduLevelById("higher education");
        UpdateEduLevelDto updateEduLevelDto = UpdateEduLevelDto.builder()
                .name("school education")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(updateEduLevelDto)
                .when()
                .put("/api/edu-level/" + eduLevelId)
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // deleting an already existing industry:
        String name = "school education";
        db.executeUpdate("DELETE FROM `edu_level` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateEduLevelById_code404_TestRA() throws SQLException { //Education level not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put in beforehand:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //Using this method we try to re-enter an already existing edu-level:
        UpdateEduLevelDto updateEduLevelDto = UpdateEduLevelDto.builder()
                .name("school education")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateEduLevelDto).when().put("/api/edu-level/" + Integer.MAX_VALUE)//в пути указываем несуществующий id несуществующей edu-level
                .then()
                .log().all()
                .assertThat().statusCode(404);

        // deleting an already existing industry:
        String name = "school education";
        db.executeUpdate("DELETE FROM `edu_level` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateEduLevelById_code409_TestRA() throws SQLException {//EduLevel with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put in beforehand:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //With this method we try to update an already existing edu-level with the same name:
        String eduLevelId = admin.getEduLevelById("higher education");
        UpdateEduLevelDto updateEduLevelDto = UpdateEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateEduLevelDto)
                .when()
                .put("/api/edu-level/" + eduLevelId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // deleting an already existing edu-level:
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId);
    }

    @Test
    public void getListOfEduLevel_code200_TestRA() throws SQLException {// List of education level
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the edu-level:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //By this method we get all edu-level:
        given()
                .cookie(cookie)
                .contentType("application/json").when().get("api/edu-level/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // The variant of deleting an already existing eduLevel (not through the database):
        String eduLevelId = admin.getEduLevelById("higher education");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId);
    }

    @Test
    public void getListOfEduLevel_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the edu-level:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //By this method we get all edu-level:
        given()
                .contentType("application/json")
                .when()
                .get("api/edu-level/all")
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // The variant of deleting an already existing eduLevel (not through the database):
        String eduLevelId = admin.getEduLevelById("higher education");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId);
    }

    @Test
    public void deleteEduLevelById_code200_TestRA() throws SQLException { //Education level deleted
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the edu-level:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //With this method we delete an already existing edu-level:
        String eduLevelId = admin.getEduLevelById("higher education");
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/edu-level/" + eduLevelId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void deleteEduLevelById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the edu-level:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //With this method we delete an already existing edu-level:
        String eduLevelId = admin.getEduLevelById("higher education");
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/edu-level/" + eduLevelId)
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // The variant of deleting an already existing eduLevel (not through the database):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId);
    }

    @Test
    public void deleteEduLevelById_code404_TestRA() throws SQLException { //Education level not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the edu-level:
        PostAddNewEduLevelDto postAddNewEduLevel = PostAddNewEduLevelDto.builder()
                .name("higher education")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postAddNewEduLevel)
                .when()
                .post("/api/edu-level");

        //With this method we delete an already existing edu-level:
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + Integer.MAX_VALUE)//in the path specify the nonexistent id of the nonexistent edu-level
                .then()
                .log().all()
                .assertThat().statusCode(404);

        // The variant of deleting an already existing eduLevel (not through the database):
        String eduLevelId = admin.getEduLevelById("higher education");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId);
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}