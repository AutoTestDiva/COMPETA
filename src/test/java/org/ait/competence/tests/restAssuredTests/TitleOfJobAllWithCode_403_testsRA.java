package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostTitleOfJobDto;
import org.ait.competence.dto.UpdateTitleOfJobDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class TitleOfJobAllWithCode_403_testsRA extends TestBaseRA{
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
    public void postAddNewTitleOfJob_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when()
                    .post("/api/job-title").then().assertThat().statusCode(201);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when()
                    .post("/api/job-title").then().log().all().assertThat().statusCode(403);
        }
    } */

    @Test
    public void putUpdateTitleOfJobById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД job-title как админ:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String jobTitleId = admin.getJobTitleIdById("junior1");
        UpdateTitleOfJobDto updateTitleOfJobDto = UpdateTitleOfJobDto.builder().name("junior2").build();

        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateTitleOfJobDto).when()
                    .put("/api/job-title/" + jobTitleId).then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateTitleOfJobDto).when()
                    .put("/api/job-title/" + jobTitleId).then()
                    .log().all().assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteTitleOfJobById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД job-title как админ:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String jobTitleId = admin.getJobTitleIdById("junior1");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/job-title/" + jobTitleId)
                    .then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/job-title/" + jobTitleId)
                    .then().log().all().assertThat().statusCode(403);
        }
    }

   /*  @Test //баг. т.к. есть доступ у всех: как у админа, так и у юзера
    public void getAllTitleOfJob_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
       //вкладываем в БД industry как админ:
       PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
       given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

       //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

         //сам метод
        String userEmail = "user5@gmail.com";
        if ("invalid@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/job-title/all")
                    .then().log().all().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().get("/api/job-title/all")
                    .then().log().all().assertThat().statusCode(403);
        }
    }*/

    @AfterMethod
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing hard-skill from DataBase, т.е зачищаем БД:
        String name = "Java";
        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");
        //удаляем пользователя с 4-х таблиц users в БД
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
