package org.ait.competence.tests.restAssuredTests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.sql.SQLException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class AuthTestsPositiveRegistrationRA extends TestBaseRA{
        @Test()
    public void a_registerUserPositiveTestRA1() throws SQLException {
        user.registerUser("nata@gmail.com", "Nata-2024!")
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
   public static void postConditionRA() throws SQLException {
        String[] args = {"nata@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
 }