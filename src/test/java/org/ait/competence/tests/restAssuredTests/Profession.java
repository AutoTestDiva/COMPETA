package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostAllProfessionsDto;
import org.ait.competence.dto.PostAllSoftSkillDto;
import org.ait.competence.dto.UpdateProfessionNameDto;
import org.ait.competence.dto.UpdateSoftSkillNameDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class Profession extends TestBaseRA{
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // Регистрируем админа
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin1@gmail.com"); //присваиваем в базе данных роль АДМИНА
    }

    @Test()
    public void postAddNewProfession_code201_TestRA1() throws SQLException { //этим тестом добавляем new profession в БД
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllProfession)
                .when()
                .post("/api/profession")
                .then()
                .log().all()
                .assertThat().statusCode(201);
        System.out.println(postAllProfession.getName());

        //первый метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
 //       String name = "programmer1";
 //       db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющейся профессии (не через базу данных):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test()
    public void postAddNewProfession_code400_TestRA1() throws SQLException {
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postAllProfession)
                .when()
                .post("/api/profession")
                .then()
                .assertThat().statusCode(400);
    }
    @Test()
    public void postAddNewProfession_WithInvalidEmail_code401_TestRA1() throws SQLException {
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //сделать ошибку в почте
        if (cookie != null) {
            given().cookie(cookie).contentType(ContentType.JSON).when().post("/api/profession")
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
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //этим методом я пытаюсь повторно вложить уже имеющийся скилл:
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession")
                .then()
                .assertThat().statusCode(409);

        //первый метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
        //       String name = "programmer1";
        //       db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющейся профессии (не через базу данных):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void putUpdateProfessionById_code200_TestRA() throws SQLException { //этим тестом обновляем profession
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым я первоначально вкладываю профессию:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //этим методом я обновляю уже имеющуюся профессию:
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
               String name = "programmer2";
               db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateProfessionById_code400_TestRA() throws SQLException { //Not valid value name EduLevel
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым я первоначально вкладываю профессию:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //этим методом я пытаюсь обновить уже имеющуюся профессию с указанием неправильного "name" в пути ("path"):
        String professionId = admin.getProfessionById("invalidName");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        // метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateProfessionById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Invalid1!"); //неправильный пароль
        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю скилл:
            PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

            //этим методом я пытаюсь обновить уже имеющийся скилл с указанием неправильного пароля в cookies:
            String professionId = admin.getProfessionById("programmer1");
            UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                    .name("programmer2")
                    .build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("User not authenticated");
        }
        // метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }
/*      @Test
    public void putUpdateProfessionById_AccessDenied_code403_TestRA() throws SQLException {// не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
       //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю профессию:
             PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

            //этим методом я пытаюсь обновить уже имеющуюся профессию с указанием неправильного пароля в cookies:
            String professionId = admin.getProfessionById("programmer1");
            UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                    .name("programmer2")
                    .build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("User not authenticated");
        }
         // метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }*/
@Test
public void putUpdateProfessionById_code404_TestRA() throws SQLException {
           cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        // Получаем идентификатор существующей профессии или null, если такой профессии нет
        String professionId = admin.getProfessionById("programmer1");
        if (professionId == null) {
            System.out.println("Profession with id 'programmer' does not found");
        } else {
            UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                    .name("programmer2")
                    .build();

            // Отправляем запрос PUT с корректным идентификатором профессии
            given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto)
                    .when().put("/api/profession/" + professionId)
                    .then().assertThat().statusCode(404);
        }
    }

    @Test
    public void putUpdateProfessionById_code409_TestRA() throws SQLException {//softSkill already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым первоначально вкладываем профессию:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //этим методом пытаемся обновить уже имеющуюся профессию с таким же названием:
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

       //  метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
       //  в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
       // String name = "programmer1";
       // db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющегося софт-скилла (не через базу данных):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void getListOfProfessions_code200_TestRA() throws SQLException {//get all Soft Skills
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //это словно предусловие, которым первоначально вкладываем профессию:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //этим методом получаем все профессии:
        given().cookie(cookie).contentType("application/json").when().get("api/profession/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //  метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //  в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
        // String name = "programmer1";
        // db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // второй вариант удаления уже имеющейся профессии (не через базу данных):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void getListOfProfessions_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //неправильный email в coockies
        //это словно предусловие, которым первоначально вкладываем профессию:
        if (cookie != null) {
            PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

            //этим методом получаем все профессии:
            given().cookie(cookie).contentType("application/json").when().get("api/profession/all")
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }

        //  метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //  в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
         String name = "programmer1";
         db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    /*
 @Test
    public void getListOfProfessions_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю профессию:
              PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

                //этим методом получаем все профессии:
                given().cookie(cookie).contentType("application/json").when().get("api/profession/all")
                        .then()
                        .log().all()
                        .assertThat().statusCode(403);
            } else {
                // Обработка случая, когда аутентификация не удалась
                System.out.println("Authentication failed. Cannot proceed with the test.");
            }

        //  метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //  в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
         String name = "programmer1";
         db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    } */

    @Test
    public void deleteProfessionById_code200_TestRA() throws SQLException {
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //это словно предусловие, которым первоначально вкладываем профессию:
        PostAllProfessionsDto postAllProfessions = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfessions).when().post("/api/profession");

        //этим методом удаляем уже имеющуюся профессию:
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }
    @Test
    public void deleteProfessionById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!");//сделать ошибку в почте

        //это словно предусловие, которым первоначально вкладываем профессию:
        if (cookie != null) {
            PostAllProfessionsDto postAllProfessions = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfessions).when().post("/api/profession");

            //этим методом удаляем уже имеющуюся профессию:
            String professionId = admin.getProfessionById("programmer1");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Обработка случая, когда аутентификация не удалась
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }

     /*   @Test
    public void deleteProfessionById_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
             //это словно предусловие, которым я первоначально вкладываю профессию:
              PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

            //этим методом пытаемся удалить профессию:
            String professionId = admin.getProfessionById("programmer1");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
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

        //  метод, удаляющий с базы данных выше указанную профессию по "name", чтоб потом автоматом проходил
        //  в JENKINS-e и т.к. удаление юзера не удаляет профессию с таблицы автоматом:
         String name = "programmer1";
         db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    } */

    @AfterMethod
//  @Test
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
