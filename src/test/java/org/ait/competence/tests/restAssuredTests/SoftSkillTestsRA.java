package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.ait.competence.dto.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SoftSkillTestsRA extends TestBaseRA{
    private Cookie cookie;
    String userId;

    @Test
    public void putOneSoftSkillById_Update_code200_TestRA() throws SQLException { //работает , если заранее добавить имя в БД
        //регистрируем нового админа
        // user.registerUser("vasja.pupkin@competa.test", "userPass007!");

        cookie = user.getLoginCookie("vasja.pupkin@competa.test", "userPass007!");
        String userId = user.getUserIdByEmail("vasja.pupkin@competa.test");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work3")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(updateSoftSkillNameDto)
                . when()
                .put("/api/soft-skill/"+ userId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
        //удаляем созданного админа
//        String[] args = {"vasja.pupkin@competa.test"};
//        deleteUser.deleteUserFromDB(args);
 }

    @Test
    public void putOneSoftSkillByIdWithWrongEmailForID_code400_TestRA() throws SQLException {//
        //регистрируем нового админа
        // user.registerUser("vasja.pupkin@competa.test", "userPass007!");

        cookie = user.getLoginCookie("vasja.pupkin@competa.test", "userPass007!");
        String userId = user.getUserIdByEmail("invalid@competa.test"); //вводим неверный email
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work2")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(updateSoftSkillNameDto)
                . when()
                .put("/api/soft-skill/"+ userId)
                .then()
                .log().all()
                .assertThat().statusCode(400);
        //удаляем созданного админа
//        String[] args = {"vasja.pupkin@competa.test"};
//        deleteUser.deleteUserFromDB(args);
    }

    @Test
    public void putOneSoftSkillByIdWithWrongPasswordForCookie_code401_TestRA() throws SQLException {
        //регистрируем нового админа
        // user.registerUser("vasja.pupkin@competa.test", "userPass007!");
        cookie = user.getLoginCookie("vasja.pupkin@competa.test", "invalid"); //неверный пароль

        if (cookie != null) {
            String userId = user.getUserIdByEmail("vasja.pupkin@competa.test");
            UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                    .name("team work2")
                    .build();
            given()
                    .cookie(cookie)
                    .contentType("application/json")
                    .body(updateSoftSkillNameDto)
                    .when()
                    .put("/api/soft-skill/" + userId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
        //удаляем созданного админа
//        String[] args = {"vasja.pupkin@competa.test"};
//        deleteUser.deleteUserFromDB(args);
    }


    @Test
    public void putOneSoftSkillById_AccessDenied_code403_TestRA() throws SQLException {// не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //регистрируем нового админа
        // user.registerUser("0", "Nata2-2024!");

        cookie = user.getLoginCookie("0", "Nata2-2024!");

            String userId = user.getUserIdByEmail("0");
            UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                    .name("Nata2")
                    .build();
            given()
                    .cookie(cookie)
                    .contentType("application/json")
                    .body(updateSoftSkillNameDto)
                    .when()
                    .put("/api/soft-skill/" + userId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);

        //удаляем созданного админа
//        String[] args = {"0"};
//        deleteUser.deleteUserFromDB(args);
    }


    @Test
    public void putOneSoftSkillById_code409_TestRA() throws SQLException {
        //регистрируем нового админа
       // user.registerUser("vasja.pupkin@competa.test", "userPass007!");

        cookie = user.getLoginCookie("vasja.pupkin@competa.test", "userPass007!");
        String userId = user.getUserIdByEmail("vasja.pupkin@competa.test");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(updateSoftSkillNameDto)
                . when()
                .put("/api/soft-skill/"+ userId)
                .then()
                .log().all()
                .assertThat().statusCode(409);
        //удаляем созданного админа
//        String[] args = {"vasja.pupkin@competa.test"};
//        deleteUser.deleteUserFromDB(args);
    }
    @Test
    public void getAllSoftSkills_code200_TestRA() throws SQLException {
        //регистрируем нового юзера
        // user.registerUser("nata@gmail.com", "Nata2024!");

        cookie = user.getLoginCookie("nata@gmail.com", "Nata2024!");
        given().cookie(cookie).contentType("application/json").when().get("api/soft-skill")
                .then()
                .assertThat().statusCode(200);
        //удаляем созданного админа
//        String[] args = {"nata@gmail.com"};
//        deleteUser.deleteUserFromDB(args);
    }
    @Test
    public void getAllSoftSkill_code401_TestRA() throws SQLException {
        //регистрируем нового юзера
        // user.registerUser("nata@gmail.com", "Nata2024!");
        cookie = user.getLoginCookie("invalid@gmail.com", "Nata2024!");//неправильная почта
        if (cookie != null) {
        given().cookie(cookie).contentType("application/json").when().get("/api/soft-skill")
                .then()
                .assertThat().statusCode(401)
        .assertThat().body("message", containsString("User not authenticated"));
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
        //удаляем созданного админа
//        String[] args = {"nata@gmail.com"};
//        deleteUser.deleteUserFromDB(args);
    }
    @Test
    public void getAllSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //регистрируем нового юзера
        // user.registerUser("0", "Nata2-2024!");
        cookie = user.getLoginCookie("0", "Nata2-2024!");
        given().cookie(cookie).contentType("application/json").when().get("/api/soft-skill")
                .then()
                .assertThat().statusCode(403);
              //удаляем созданного админа
//        String[] args = {"0"};
//        deleteUser.deleteUserFromDB(args);
    }

    @Test()
    public void postAllSoftSkill_code201_TestRA1() throws SQLException {
        //регистрируем нового админа
        // user.registerUser("admin2@gmail.com", "Admin2-2024!");
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
                .when()
                .post("/api/soft-skill")
                .then()
                .assertThat().statusCode(201);
//удаляем созданного админа
//        String[] args = {"admin2@gmail.com"};
//        deleteUser.deleteUserFromDB(args);
//    }
    }

    @Test()
    public void postAllSoftSkill_code400_TestRA1() throws SQLException {
            //регистрируем нового админа
            // user.registerUser("admin@gmail.com", "Admin-2024!");
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
            //удаляем созданного админа
//        String[] args = {"admin2@gmail.com"};
//        deleteUser.deleteUserFromDB(args);
    }
    @Test()
    public void postAllSoftSkill_code401_TestRA1() throws SQLException {
        //регистрируем нового админа
        // user.registerUser("admin@gmail.com", "Admin-2024!");
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin-2024!"); //сделать ошибку в почте
        if (cookie != null) {
            given().cookie(cookie).contentType(ContentType.JSON).when().post("/api/soft-skill")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("User not authenticated"));
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
        //удаляем созданного админа
//        String[] args = {"admin@gmail.com"};
//        deleteUser.deleteUserFromDB(args);
    }

    @Test
    public void postAllSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //регистрируем нового юзера
        // user.registerUser("0", "Nata2-2024!");
        cookie = user.getLoginCookie("0", "Nata2-2024!");
        given().cookie(cookie).when().post("/api/soft-skill")
                .then()
                .assertThat().statusCode(403);
        //удаляем созданного админа
//        String[] args = {"0"};
//        deleteUser.deleteUserFromDB(args);
     }

    @Test()
    public void postAllSoftSkill_code409_TestRA1() throws SQLException {
        //регистрируем нового юзера
        // user.registerUser("admin@gmail.com", "Admin-2024!");

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
        //удаляем созданного админа
//        String[] args = {"admin@gmail.com"};
//        deleteUser.deleteUserFromDB(args);
    }
    @Test
    public void deleteOneSoftSkillById_code200_TestRA() throws SQLException { //работает, если такой пользователь есть с именем
        //регистрируем нового админа
        // user.registerUser("vasja.pupkin@competa.test", "userPass007!");

        cookie = user.getLoginCookie("vasja.pupkin@competa.test", "userPass007!");
        String userId = user.getUserIdByEmail("vasja.pupkin@competa.test");
        given().cookie(cookie).when().delete("/api/soft-skill/"+ userId)
                .then()
                .assertThat().statusCode(200);

        //удаляем созданного админа
//        String[] args = {"vasja.pupkin@competa.test"};
//        deleteUser.deleteUserFromDB(args);
    }
    @Test
    public void deleteOneSoftSkillById_code401_TestRA() throws SQLException { //выдает 400, а не 401
        //регистрируем нового админа
        // user.registerUser("vasja.pupkin@competa.test", "userPass007!");

        cookie = user.getLoginCookie("invalid@gmail.com", "Admin-2024!");//сделать ошибку в почте
        if (cookie != null) {
        String userId = user.getUserIdByEmail("admin@gmail.com");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/"+ userId)
                .then()
                .log().all()
                .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    //удаляем созданного админа
//        String[] args = {"vasja.pupkin@competa.test"};
//        deleteUser.deleteUserFromDB(args);
    }


    @Test
    public void deleteAllSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //регистрируем нового юзера
        // user.registerUser("0", "Nata2-2024!");
        cookie = user.getLoginCookie("0", "Nata2-2024!");
        given().cookie(cookie).when().delete("/api/soft-skill")
                .then()
                .assertThat().statusCode(403);
        //удаляем созданного админа
//        String[] args = {"0"};
//        deleteUser.deleteUserFromDB(args);
    }

}

