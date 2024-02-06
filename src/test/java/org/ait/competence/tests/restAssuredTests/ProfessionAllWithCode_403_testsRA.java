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

public class ProfessionAllWithCode_403_testsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionForTestsWithCode_403_TestsRA() throws SQLException {
        //сначала регистрируем user с ролью АДМИНА, иначене сможем вложить edu-level:
        user.registerUser("user5@gmail.com", "User005!", "superUser5");
        user.userStatusConfirmed("user5@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("user5@gmail.com");         //присваиваем в базе данных роль ADMIN
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

    }

  /*  @Test //баг. т.к. есть доступ у всех: как у админа, так и у юзера
    public void postAddNewProfession_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        PostAllProfessionsDto postAllProfessions = PostAllProfessionsDto.builder().name("team work10").build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfessions).when()
                    .post("/api/profession").then().assertThat().statusCode(201);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfessions).when()
                    .post("/api/profession").then().log().all().assertThat().statusCode(403);
        }
    }*/

    @Test
    public void putUpdateProfessionById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД profession как админ:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder().name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when()
                    .put("/api/profession/" + professionId).then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when()
                    .put("/api/profession/" + professionId).then()
                    .log().all().assertThat().statusCode(403);
        }
    }
    @Test
    public void deleteProfessionById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД profession как админ:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder().name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String professionId = admin.getProfessionById("programmer1");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
                    .then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
                    .then().log().all().assertThat().statusCode(403);
        }
    }
   /* @Test //баг. т.к. есть доступ у всех: как у админа, так и у юзера
    public void getAllProfession_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД profession как админ:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder().name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

         //сам метод
        String userEmail = "user5@gmail.com";
        if ("invalid@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/profession/all")
                    .then().log().all().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/profession/all")
                    .then().log().all().assertThat().statusCode(403);
        }
    }*/

    @AfterMethod
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing profession from DataBase, т.е зачищаем БД:
        String name = "Java";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
        //удаляем пользователя с 4-х таблиц users в БД
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}

