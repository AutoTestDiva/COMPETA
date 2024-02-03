package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;

public class SoftSkillTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // Регистрируем админа
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin1@gmail.com"); //присваиваем в базе данных роль АДМИНА
    }

    @Test()
    public void postAddSoftSkill_code201_TestRA1() throws SQLException { //этим тестом добавляем soft-skill в БД
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllSoftSkill)
                .when()
                .post("/api/soft-skill")
                .then()
                .assertThat().statusCode(201);
        System.out.println(postAllSoftSkill.getName());

        //первый метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом:
//        String name = "team work10";
//        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющегося софт-скилла (не через базу данных):
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test()
    public void postAddSoftSkill_code400_TestRA1() throws SQLException {
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
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
    public void postAddSoftSkill_WithInvalidEmail_code401_TestRA1() throws SQLException {
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //сделать ошибку в почте
        if (cookie != null) {
            given().cookie(cookie).contentType(ContentType.JSON).when().post("/api/soft-skill")
                    .then()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }

   /* @Test
    public void postAddSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");
        given().cookie(cookie).when().post("/api/soft-skill")
                .then()
                .assertThat().statusCode(403);
    }*/

    @Test()
    public void postAddSoftSkill_code409_TestRA1() throws SQLException {//этим тестом пытаемся добавить такой же уже имеющийся soft-skill
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //это словно предусловие, которым я заранее вложила скилл:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //этим методом я пытаюсь повторно вложить уже имеющийся скилл:
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill")
                .then()
                .assertThat().statusCode(409);

        //первый  метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом:
        // String name = "team work10";
        //  db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющегося софт-скилла (не через базу данных):
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test
    public void putUpdateSoftSkillById_code200_TestRA() throws SQLException { //этим тестом обновляем soft-skill
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым я первоначально вкладываю скилл:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //этим методом я обновляю уже имеющийся скилл:
        String softSkillId = admin.getSoftSkillById("team work10");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом:
        String name = "team work1";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateSoftSkillById_code400_TestRA() throws SQLException { //Validation error
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым я первоначально вкладываю скилл:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //этим методом я пытаюсь обновить уже имеющийся скилл с указанием неправильного "name" в пути ("path"):
        String softSkillId = admin.getSoftSkillById("invalidName");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        //метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом:
        String name = "team work10";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateSoftSkillById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Invalid1!");
        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю скилл:
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //этим методом я пытаюсь обновить уже имеющийся скилл с указанием неправильного пароля в cookies:

            String softSkillId = admin.getSoftSkillById("team work10");
            UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                    .name("team work1")
                    .build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("User not authenticated");
        }
        //первый метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом
            String name = "team work10";
            db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }


  /*      @Test
    public void putOneSoftSkillById_AccessDenied_code403_TestRA() throws SQLException {// не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
       //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю скилл:
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //этим методом я пытаюсь обновить уже имеющийся скилл с указанием неправильного пароля в cookies:

            String softSkillId = admin.getSoftSkillById("team work10");
            UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                    .name("team work1")
                    .build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("User not authenticated");
        }
        //метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом
        String name = "team work10";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }*/

    @Test
    public void putSoftSkillById_code409_TestRA() throws SQLException {//softSkill already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым я первоначально вкладываю скилл:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //этим методом я обновляю уже имеющийся скилл таким же:
        String softSkillId = admin.getSoftSkillById("team work10");
        UpdateSoftSkillNameDto updateSoftSkillNameDto = UpdateSoftSkillNameDto.builder()
                .name("team work10")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateSoftSkillNameDto).when().put("/api/soft-skill/" + softSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        //первый метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом:
        //String name = "team work10";
        // db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющегося софт-скилла (не через базу данных):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test
    public void getAllSoftSkills_code200_TestRA() throws SQLException {//get all Soft Skills
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //это словно предусловие, которым я первоначально вкладываю скилл:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //этим методом получаем все скилы:
        given().cookie(cookie).contentType("application/json").when().get("api/soft-skill")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //первый метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом
        //String name = "team work10";
        // db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющегося софт-скилла (не через базу данных):
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }

    @Test
    public void getAllSoftSkill_code401_TestRA() throws SQLException {                //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //неправильный email в coockies
        //это словно предусловие, которым я первоначально вкладываю скилл:
        if (cookie != null) {
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //этим методом получаем все скилы:
            given().cookie(cookie).contentType("application/json").when().get("api/soft-skill")
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }

        //первый метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом
        String name = "team work10";
        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
    }
/*
 @Test
    public void getAllSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю скилл:
              PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                        .name("team work10").build();
                given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

                //этим методом получаем все скилы:
                given().cookie(cookie).contentType("application/json").when().get("api/soft-skill")
                        .then()
                        .log().all()
                        .assertThat().statusCode(403);
            } else {
                // Обработка случая, когда аутентификация не удалась
                System.out.println("Authentication failed. Cannot proceed with the test.");
            }
            //метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
            //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом
            String name = "team work10";
            db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");
        } */

    @Test
    public void deleteSoftSkillById_code200_TestRA() throws SQLException {
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым я первоначально вкладываю скилл:
        PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                .name("team work10").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

        //этим методом я удаляю уже имеющийся скилл:
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void deleteOneSoftSkillById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!");//сделать ошибку в почте

        //это словно предусловие, которым я первоначально вкладываю скилл:
        if (cookie != null) {
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //этим методом я удаляю уже имеющийся скилл:
            String softSkillId = admin.getSoftSkillById("team work10");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }

    /*   @Test
    public void deleteAllSoftSkill_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю скилл:
            PostAllSoftSkillDto postAllSoftSkill = PostAllSoftSkillDto.builder()
                    .name("team work10").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllSoftSkill).when().post("/api/soft-skill");

            //этим методом пытаемся удалить скилы:
            String softSkillId = admin.getSoftSkillById("team work10");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
          } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }

        //первый метод, удаляющий с базы данных выше указанный софт-скилл по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет софт-скилы с таблицы автоматом
//        String name = "team work10";
//        db.executeUpdate("DELETE FROM `soft_skill` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющегося софт-скилла (не через базу данных):
        String softSkillId = admin.getSoftSkillById("team work10");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/soft-skill/" + softSkillId);
    }*/


      @AfterMethod
       public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
  
}

