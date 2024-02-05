package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostDriverLicenceDto;
import org.ait.competence.dto.UpdateDriverLicenceDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class DriverLicenceAllWithCode_403_testsRA extends TestBaseRA {
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
    public void postAddDriverLicence_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        PostDriverLicenceDto updateDriverLicenceDto = PostDriverLicenceDto.builder().name("A").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when()
                    .post("/api/driver-licence").then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when()
                    .post("/api/driver-licence").then().log().all().assertThat().statusCode(403);
        }
    }

    @Test
    public void putUpdateDriverLicenceById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД лицензию как админ:
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder().name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String driverLicenceId = admin.getDriverLicenceById("A");
        UpdateDriverLicenceDto updateDriverLicenceDto = UpdateDriverLicenceDto.builder().name("C").build();
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when()
                    .put("/api/driver-licence/" + driverLicenceId).then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when()
                    .put("/api/driver-licence/" + driverLicenceId).then().log().all()
                    .assertThat().statusCode(403);
        }
    }

    @Test
    public void deleteDriverLicenceById_code403_TestRA() throws SQLException { //Access denied for user with email <{0}> and role {1}
        //еще одно предусловие:
        //вкладываем в БД лицензию как админ:
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder().name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");
        //потом меняем роль user с роли АДМИНА на роль ЮЗЕРА:
        user.userRole("user5@gmail.com"); //присваиваем в базе данных роль USER
        //обновляем cookie, чтоб юзер у нас уже был со статусом USER
        cookie = user.getLoginCookie("user5@gmail.com", "User005!");

        //сам метод
        String driverLicenceId = admin.getDriverLicenceById("A");
        // Check if the current user can update name
        String userEmail = "user5@gmail.com";
        if ("admin1@gmail.com".equals(userEmail)) {
            given().cookie(cookie).contentType(ContentType.JSON).when()
                    .delete("/api/driver-licence/" + driverLicenceId).then().assertThat().statusCode(200);
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when()
                    .delete("/api/driver-licence/" + driverLicenceId).then().log().all()
                    .assertThat().statusCode(403);
        }
    }

    @AfterMethod
    // @Test
    public static void postConditionForTestsWithCode_403_TestsRA() throws SQLException {
        // deleting an already existing driver_licence from DataBase, т.е зачищаем БД:
        String name = "A";
        db.executeUpdate("DELETE FROM `driver_licence` WHERE `name` = '" + name + "';");
        //удаляем пользователя с 4-х таблиц users в БД:
        String[] args = {"user5@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
