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

public class EduLevelAllWithCode_403_testsRA extends TestBaseRA {
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
    public void postAddNewEduLevel_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        PostAddNewEduLevelDto postAddNewEduLevelDto = PostAddNewEduLevelDto.builder()
                .name("higher education").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(postAddNewEduLevelDto).when()
                    .post("/api/edu-level").then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(postAddNewEduLevelDto).when()
                    .post("/api/edu-level").then().log().all().assertThat().statusCode(403);
        }
    }

    @Test
    public void putUpdateEduLevelById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД hard-skill как админ:
        PostAddNewEduLevelDto postAddNewEduLevelDto = PostAddNewEduLevelDto.builder().name("higher education").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddNewEduLevelDto).when().post("/api/edu-level");
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String eduLevelId = admin.getEduLevelById("higher education");
        UpdateEduLevelDto updateEduLevelDto = UpdateEduLevelDto.builder().name("school").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateEduLevelDto).when()
                    .put("/api/edu-level/" + eduLevelId).then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateEduLevelDto).when()
                    .put("/api/edu-level/" + eduLevelId).then().log().all().assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteEduLevelById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД edu-level как админ:
        PostAddNewEduLevelDto postAddNewEduLevelDto = PostAddNewEduLevelDto.builder()
                .name("higher education").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAddNewEduLevelDto).when()
                .post("/api/edu-level");
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String eduLevelId = admin.getEduLevelById("higher education");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId)
                    .then().assertThat().statusCode(200); // Assuming successful response code is 200
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/edu-level/" + eduLevelId)
                    .then().log().all().assertThat().statusCode(403);
        }
    }

    @AfterMethod
    // @Test
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing edu-level from DataBase, т.е зачищаем БД:
        String name = "higher education";
        db.executeUpdate("DELETE FROM `edu_level` WHERE `name` = '" + name + "';");
        //удаляем пользователя с 4-х таблиц users в БД
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}

