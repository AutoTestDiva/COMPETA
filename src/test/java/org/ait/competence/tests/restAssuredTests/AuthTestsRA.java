package org.ait.competence.tests.restAssuredTests;

import io.restassured.response.Response;
import org.ait.competence.dto.RegisterUserWithoutNickNameDto;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class AuthTestsRA extends TestBaseRA {
    @Test
    public void a_postRegisterNewUser_code200_TestRA1() { //User Details
        // user registration:
        Response response = user.registerUser("user2@gmail.com", "User002!", "superUser2");
        // Check that the query completed successfully:
        response.then().log().all().statusCode(200);
        user.userStatusConfirmed("user2@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        System.out.println("Response body: " + response.getBody().asString());
    }

    //    second variant of writing the test:
//    @Test()
//    public void postRegisterNewUser_code200_TestRA2() throws SQLException {  //User Details
//        user.registerUser("user2@gmail.com", "User002!", "superUser2")
//                .then()
//                .assertThat().statusCode(200);
//    }
    @Test()
    public void registerUserWithoutNickName_400_TestRA1() throws SQLException { // Validation errors
        RegisterUserWithoutNickNameDto registerUserWithoutNickName =
                user.registerUserWithoutNickName_code400("user2@gmail.com", "User002!")
                        .then()
                        .log().all()
                        .assertThat().statusCode(400)
                        .extract().response().as(RegisterUserWithoutNickNameDto.class);
    }

    @Test()
    public void registerUserWithoutNickName_400_TestRA2() throws SQLException { //Validation errors
        user.registerUserWithoutNickName_code400("user2@gmail.com", "User002!")
                .then()
                .log().all()
                .assertThat().statusCode(400);
    }

    // @AfterMethod
    @Test
    public static void postConditionRA() throws SQLException {
        String[] args = {"user2@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}