package org.ait.competence.tests.restAssuredTests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class AuthTestsPositiveRegistrationRA extends TestBaseRA{
        @Test()
    public void a_registerUserPositiveTestRA1() throws SQLException {
        user.registerUser("nata@gmail.com", "Nata2024!")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("email", containsString("nata@gmail.com"))
                .assertThat().body("email", equalTo("nata@gmail.com"));
    }

//    @Test()
//    public void registerUserPositiveTestRA2() throws SQLException {  //второй вариант
//        user.registerUser("nata@gmail.com", "Nata2024!")
//                .then()
//                .assertThat().statusCode(200);
//    }



    @AfterMethod
    @Test
    public static void postConditionRA() throws SQLException {
        String[] args = {"nata@gmail.com"};
        deleteUser.deleteUserFromDB1(args);
    }

    //@AfterMethod
//    public static void postConditionRA2() throws SQLException { //второй вариант, но не удаляющий юзера в БД с таблицы "confirmation_code"
//        String[] args = {"nata@gmail.com"};
//        deleteUser.deleteUserFromDB(args);
//    }

 }