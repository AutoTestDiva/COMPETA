package org.ait.competence.tests.restAssuredTests;

import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;

public class AuthTestsPositiveRegistrationRA extends TestBaseRA{
    @Test
    public void a_registerUserPositiveTestRA1() {
        // Регистрируем пользователя
        Response response = user.registerUser("user2@gmail.com", "User002!", "superUser2");
        // Проверяем, что запрос завершился успешно
        response.then().statusCode(200);
        user.userStatusConfirmed("user2@gmail.com"); //меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
      System.out.println("Response body: " + response.getBody().asString());
    }

//    @Test()
//    public void registerUserPositiveTestRA2() throws SQLException {  //второй вариант
//        user.registerUser("user2@gmail.com", "User002!", "superUser2")

//                .then()
//                .assertThat().statusCode(200);
//    }

  @AfterMethod
      public static void postConditionRA() throws SQLException {
        String[] args = {"user2@gmail.com"};

        deleteUser.deleteUserFromDB(args);
    }
 }