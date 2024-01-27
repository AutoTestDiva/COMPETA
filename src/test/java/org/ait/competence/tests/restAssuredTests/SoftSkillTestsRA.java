package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostAllSoftSkillDto;
import org.ait.competence.dto.PutSoftSkillDto;
import org.ait.competence.dto.PutUserProfileDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SoftSkillTestsRA extends TestBaseRA{
    private Cookie cookie;
    String userId;

    @Test //новый
    public void putOneSoftSkillById_code200_TestRA() throws SQLException { //не доделан мной
        cookie = user.getLoginCookie("nata@gmail.com", "Nata2024!");
        String userId = user.getUserIdByEmail("nata@gmail.com");

        PutSoftSkillDto softSkill = PutSoftSkillDto.builder()
                .name("Nata")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(softSkill)
                . when()
                .put("/api/soft-skill/" + userId)
                .then()
                .assertThat().statusCode(200);
    }
    @Test
    public void getAllSoftSkills_code200_TestRA() throws SQLException { // работает
        cookie = user.getLoginCookie("nata@gmail.com", "Nata2024!");
        given().cookie(cookie).when().get("api/soft-skill")
                .then()
                .assertThat().statusCode(200);
    }
    @Test
    public void getAllSoftSkill_code401_TestRA() throws SQLException { //работает
       // cookie = user.getLoginCookie("nata@gmail.com", "Nata2024!");
        given().contentType(ContentType.JSON).when().get("/api/soft-skill")
                .then()
                .assertThat().statusCode(401)
        .assertThat().body("message", containsString("User not authenticated"));
    }
    @Test
    public void getAllSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        cookie = user.getLoginCookie("0", "Nata2-2024!");
        given().cookie(cookie).when().get("/api/soft-skill")
                .then()
                .assertThat().statusCode(403);
                //.assertThat().body("message", containsString("Access denied for user with email <{0}> and role {1}"));
    }

    @Test()
    public void postAllSoftSkill_code201_TestRA1() throws SQLException {//!!! работает только
        //регистрируем нового админа
        user.registerUser("admin2@gmail.com", "Admin2-2024!");
//!!! работает только если до регистрации нового админа ниже весь метод заремить, потом зарегистрировать нового админа
//    потом в базе данных вручную  поменять на статус "admin" и  "confirmed" во всех  таблицах базы данных
//    заремить регистрацию и разремить нижнюю часть метода
        cookie = user.getLoginCookie("admin2@gmail.com", "Admin2-2024!");
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("admin2")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllSoftSkill)
                . when()
                .post("/api/soft-skill")
                .then()
                .assertThat().statusCode(201);
//удаляем созданного админа
        String[] args = {"admin2@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }

    @Test()
    public void postAllSoftSkill_code400_TestRA1() throws SQLException {
        cookie = user.getLoginCookie("admin@gmail.com", "Admin-2024!");
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllSoftSkill)
                .when()
                .post("/api/soft-skill")
                .then()
                .assertThat().statusCode(400);
    }
    @Test()
    public void postAllSoftSkill_code401_TestRA1() throws SQLException {
        given().contentType(ContentType.JSON).when().post("/api/soft-skill")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("User not authenticated"));
    }

    @Test
    public void postAllSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        cookie = user.getLoginCookie("0", "Nata2-2024!");
        given().cookie(cookie).when().post("/api/soft-skill")
                .then()
                .assertThat().statusCode(403);
     }

    @Test()
    public void postAllSoftSkill_code409_TestRA1() throws SQLException {
        cookie = user.getLoginCookie("admin@gmail.com", "Admin-2024!");
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("admin")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllSoftSkill)
                . when()
                .post("/api/soft-skill")
                .then()
                .assertThat().statusCode(409);
    }
    }

