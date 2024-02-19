package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostForeignLanguageLevelDto;
import org.ait.competence.dto.UpdateForeignLanguageLevelDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class ForeignLanguageLEVELApiTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    /*@Test //bug - 200 instead 201. 200 в Сваггере нет
    public void postAddNewForeignLanguageLEVEL_code201_TestRA1() throws SQLException { //Foreign Language created
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel")
                .then()
                .log().all()
                .assertThat().statusCode(201);

        // deleting an already existing ForeignLanguageLevel:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/languageLevel/" + languageLEVELId);
    }*/

   /* @Test //bug - 200 instead 400
    public void postAddNewForeignLanguage_code400_TestRA1() throws SQLException { // Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
         PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel")
                .then()
                .log().all()
                .assertThat().statusCode(400);

        // deleting an already existing ForeignLanguageLevel:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/languageLevel/" + languageLEVELId);
    }*/

/*    @Test //баг - возвращает 200, но тело неверно, обновление не происходит
    public void putUpdateForeignLanguageLEVELById_code200_TestRA() throws SQLException { //Foreign Language updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
          PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //Using this method we try to re-enter an already existing ForeignLanguage:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        UpdateForeignLanguageLevelDto updateForeignLanguageLevelDto = UpdateForeignLanguageLevelDto.builder()
                .name("Intermedia")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageLevelDto)
                .when()
                .put("/api/languageLevel/" + languageLEVELId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

          // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }*/

 /*   @Test
    public void putUpdateForeignLanguageLEVELById_code400_TestRA() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //Using this method we try to re-enter an already existing ForeignLanguage:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
         UpdateForeignLanguageLevelDto updateForeignLanguageLevelDto = UpdateForeignLanguageLevelDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageLevelDto)
                .when()
                .put("/api/languageLevel/" + languageLEVELId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }*/

    @Test
    public void putUpdateForeignLanguageLEVELById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //Using this method we try to re-enter an already existing ForeignLanguage:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        UpdateForeignLanguageLevelDto updateForeignLanguageLevelDto = UpdateForeignLanguageLevelDto.builder()
                .name("Intermedia")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageLevelDto)
                .when()
                .put("/api/languageLevel/" + languageLEVELId)
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }


  /* @Test //баг - возвращает 500
    public void putUpdateForeignLanguageLevelById_code404_TestRA() throws SQLException { //Foreign Language not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //Using this method we try to re-enter an already existing ForeignLanguage:
       UpdateForeignLanguageLevelDto updateForeignLanguageLevelDto = UpdateForeignLanguageLevelDto.builder()
                .name("intermedia")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageLevelDto)
                .when()
                .put("/api/languageLevel/" + Integer.MAX_VALUE)//в пути указываем несуществующий id несуществующего уровня иностранного языка
                .then()
                .log().all()
                .assertThat().statusCode(404);

        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }*/

  /*  @Test // баг - возвращает код 200 вместо 409
    public void putUpdateForeignLanguageLevelById_code409_TestRA() throws SQLException { //Foreign Language  with that name already exists
            cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
            //It's like a precondition
            PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                    .name("advanced")
                    .build();
            given()
                    .cookie(cookie)
                    .contentType(ContentType.JSON)
                    .body(postForeignLanguageLevelDto)
                    .when()
                    .post("/api/languageLevel");

        //Using this method we try to re-enter an already existing ForeignLanguageLevel:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        UpdateForeignLanguageLevelDto updateForeignLanguageLevelDto = UpdateForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(updateForeignLanguageLevelDto)
                .when()
                .put("/api/languageLevel/" + languageLEVELId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }*/

    @Test
    public void getListOfForeignLanguageLevel_code200_TestRA() throws SQLException {// List of Foreign Language Level
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //By this method we get all Foreign Language Level:
        given()
                .cookie(cookie)
                .contentType("application/json").when().get("api/languageLevel/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }

    @Test
    public void getListOfForeignLanguageLevel_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //By this method we get all Foreign Language Level:
        given()
                .contentType("application/json").when().get("api/languageLevel/all")
                .then()
                .log().all()
                .assertThat().statusCode(401);

        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }


    /*@Test //баг- возвращает код 200 вместо 204
    public void deleteForeignLanguageLevelById_code204_TestRA() throws SQLException { //Foreign Language Level deleted
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
         //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //With this method we delete an already existing Foreign Language:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/languageLevel/" + languageLEVELId)
                .then()
                .log().all()
                .assertThat().statusCode(204);
    }*/

    @Test //баг- возвращает код 200 вместо 204
    public void deleteForeignLanguageLevelById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //With this method we delete an already existing Foreign Language:
        String languageLEVELId = admin.getLanguageLEVELById("advanced");
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/languageLevel/" + languageLEVELId)
                .then()
                .log().all()
                .assertThat().statusCode(401);
    }

    @Test
    public void getListOfForeignLanguageLevel_code404_TestRA() throws SQLException {//Foreign Language Level not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition
        PostForeignLanguageLevelDto postForeignLanguageLevelDto = PostForeignLanguageLevelDto.builder()
                .name("advanced")
                .build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postForeignLanguageLevelDto)
                .when()
                .post("/api/languageLevel");

        //By this method we get all Foreign Language Level:
        given()
                .cookie(cookie)
                .contentType("application/json").when().get("api/languageLevel" + Integer.MAX_VALUE)//в пути указываем несуществующий id несуществующего уровня иностранного языка)
                .then()
                .log().all()
                .assertThat().statusCode(404);

        // deleting an already existing ForeignLanguageLevel:
        String name = "advanced";
        db.executeUpdate("DELETE FROM `foreign_language_level` WHERE `name` = '" + name + "';");
    }


    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}