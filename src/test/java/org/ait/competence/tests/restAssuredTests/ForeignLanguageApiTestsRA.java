package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class ForeignLanguageApiTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    @Test
    public void postAddNewForeignLanguage_code201_TestRA1() throws SQLException { //Foreign Language created
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language")
                .then()
                .log().all()
                .assertThat().statusCode(201);

        // deleting an already existing industry:
        String languageId = admin.getLanguageById("english");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/language/" + languageId);

    }

  /*  @Test //bug - 201 instead 400
    public void postAddNewForeignLanguage_code400_TestRA1() throws SQLException { // Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language")
                .then()
                .log().all()
                .assertThat().statusCode(400);
    }*/

    @Test
    public void postAddNewForeignLanguage_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language")
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // deleting an already existing industry:
        String languageId = admin.getLanguageById("english");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/language/" + languageId);
    }

   /* @Test //bug - 201 instead 409
    public void postAddNewForeignLanguage_code409_TestRA1() throws SQLException { //Foreign language with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language")
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // deleting an already existing ForeignLanguage:
        String languageId = admin.getLanguageById("english");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/language/" + languageId);
    }*/

  /*  @Test //неправильное тело ответа
    public void putUpdateForeignLanguageById_code200_TestRA() throws SQLException { //Foreign Language updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //Using this method we try to re-enter an already existing ForeignLanguage:
        String languageId = admin.getLanguageById("english");
          UpdateForeignLanguageDto updateForeignLanguageDto = UpdateForeignLanguageDto.builder()
                .name("french")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageDto)
                .when()
                .put("/api/language/" + languageId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // deleting an already existing ForeignLanguage:
        String name = "french";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
     }*/

 /*  @Test
    public void putUpdateForeignLanguageById_code400_TestRA() throws SQLException { //Not valid value name Foreign Language
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //Using this method, try to update an existing Foreign Language with the wrong "name" in the path ("path"):
        String languageId = admin.getLanguageById("english");
        UpdateForeignLanguageDto updateForeignLanguageDto = UpdateForeignLanguageDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageDto)
                .when()
                .put("/api/language/" + languageId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        // deleting an already existing ForeignLanguage:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }*/


    @Test
    public void putUpdateForeignLanguageById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the Foreign Language in advance:
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //Using this method we try to re-enter an already existing ForeignLanguage:
        String languageId = admin.getLanguageById("english");
        UpdateForeignLanguageDto updateForeignLanguageDto = UpdateForeignLanguageDto.builder()
                .name("french")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageDto)
                .when()
                .put("/api/language/" + languageId)
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // deleting an already existing ForeignLanguage:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }

/*    @Test //баг - возвращает код 500, в сваггере возвращает 200
    public void putUpdateForeignLanguageById_code404_TestRA() throws SQLException { //Foreign Language not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the Foreign Language in advance:
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //Using this method we try to re-enter an already existing Foreign Language:
        UpdateForeignLanguageDto updateForeignLanguageDto = UpdateForeignLanguageDto.builder()
                .name("french")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageDto)
                .when()
                .put("/api/language/" + Integer.MAX_VALUE)//в пути указываем несуществующий id несуществующего иностранного языка
                .then()
                .log().all()
                .assertThat().statusCode(404);

        // deleting an already existing ForeignLanguage:
        String name = "french";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }*/


   /* @Test // баг - возвращает код 200 вместо 409
    public void putUpdateForeignLanguageById_code409_TestRA() throws SQLException { //Foreign Language  with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //Using this method we try to re-enter an already existing ForeignLanguage:
        String languageId = admin.getLanguageById("english");
        UpdateForeignLanguageDto updateForeignLanguageDto = UpdateForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageDto)
                .when()
                .put("/api/language/" + languageId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // deleting an already existing ForeignLanguage:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }*/

    @Test
    public void getListOfForeignLanguage_code200_TestRA() throws SQLException {// List of Foreign Language
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //By this method we get all Foreign Language:
        given()
                .cookie(cookie)
                .contentType("application/json").when().get("api/language/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // deleting an already existing ForeignLanguage:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }

    @Test
    public void getListOfForeignLanguage_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //By this method we get all Foreign Language:
        given()

                .contentType("application/json").when().get("api/language/all")
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // deleting an already existing ForeignLanguage:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }


    /* @Test //баг- возвращает код 200 вместо 204
    public void deleteForeignLanguageById_code204_TestRA() throws SQLException { //Foreign Language deleted
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the ForeignLanguage:
       PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //With this method we delete an already existing Foreign Language:
        String languageId = admin.getLanguageById("english");
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/language/" + languageId)
                .then()
                .log().all()
                .assertThat().statusCode(204);
    }*/
    @Test
    public void deleteForeignLanguageById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the ForeignLanguage:
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //With this method we delete an already existing Foreign Language:
        String languageId = admin.getLanguageById("english");
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/language/" + languageId)
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // deleting an already existing ForeignLanguage:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }

    @Test
    public void deleteForeignLanguageById_code404_TestRA() throws SQLException { //Foreign Language not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the ForeignLanguage:
        PostForeignLanguageDto postForeignLanguage = PostForeignLanguageDto.builder()
                .name("english")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguage)
                .when()
                .post("/api/language");

        //With this method we delete an already existing Foreign Language:
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/language/" + Integer.MAX_VALUE)//в пути указываем несуществующий id несуществующего иностранного языка
                .then()
                .log().all()
                .assertThat().statusCode(404);

        // deleting an already existing ForeignLanguage:
        String name = "english";
        db.executeUpdate("DELETE FROM `foreign_language` WHERE `name` = '" + name + "';");
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}