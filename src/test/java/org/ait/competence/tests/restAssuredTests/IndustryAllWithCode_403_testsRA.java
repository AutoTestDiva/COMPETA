package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostIndustryDto;
import org.ait.competence.dto.UpdateIndustryDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;

public class IndustryAllWithCode_403_testsRA extends TestBaseRA{
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
    public void postAddNewIndustry_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        PostIndustryDto postIndustry = PostIndustryDto.builder().name("education1").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when()
                    .post("/api/industry").then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when()
                    .post("/api/industry").then().log().all().assertThat().statusCode(403);
        }
    }

    @Test
    public void putUpdateIndustryById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД industry как админ:
        PostIndustryDto postIndustry = PostIndustryDto.builder().name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String industryId = admin.getIndustryById("education1");
        UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder().name("education2").build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto).when()
                    .put("/api/industry/" + industryId).then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto).when()
                    .put("/api/industry/" + industryId).then()
                    .log().all().assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteIndustryById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД industry как админ:
        PostIndustryDto postIndustry = PostIndustryDto.builder().name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String industryId = admin.getIndustryById("education1");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId)
                    .then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId)
                    .then().log().all().assertThat().statusCode(403);
        }
    }
 /*  @Test //баг. т.к. есть доступ у всех: как у админа, так и у юзера
    public void getAllIndustries_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
       //вкладываем в БД industry как админ:
       PostIndustryDto postIndustry = PostIndustryDto.builder().name("education1").build();
       given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

       //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

         //сам метод
        String userEmail = "user5@gmail.com";
        if ("invalid@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/industry/all")
                    .then().log().all().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/industry/all")
                    .then().log().all().assertThat().statusCode(403);
        }
    }  */

    @AfterMethod
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing hard-skill from DataBase, т.е зачищаем БД:
        String name = "Java";
        db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");
        //удаляем пользователя с 4-х таблиц users в БД
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
