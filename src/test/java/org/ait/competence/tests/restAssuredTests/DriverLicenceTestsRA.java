package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.ait.competence.dto.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.ait.competence.tests.restAssuredTests.TestBaseRA.*;

public class DriverLicenceTestsRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    @Test()
    public void postAddDriverLicence_code201_TestRA1() throws SQLException { //Driver licence type added
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                .name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when()
                .post("/api/driver-licence").then().log().all().assertThat().statusCode(201);
        System.out.println(postDriverLicence.getName());

        // deleting an already existing driver-licence:
        String driverLicenceId = admin.getDriverLicenceById("A");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api//" + driverLicenceId);
    }

    @Test()
    public void postAddDriverLicence_code400_TestRA1() throws SQLException { //Not valid value name DriverLicence
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                .name("").build();
        given().cookie(cookie).contentType("application/json").body(postDriverLicence).when()
                .post("/api/driver-licence").then().assertThat().statusCode(400);
    }
    @Test()
    public void postAddDriverLicence_WithInvalidEmail_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //сделать ошибку в почте
        if (cookie != null) {
            //This is like a precondition, by which we enter the driver-licence in advance:
            PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                    .name("A").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");
            given().cookie(cookie).contentType(ContentType.JSON).when().post("/api/driver-licence")
                    .then().assertThat().statusCode(401);
        } else {
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }
@Test()
public void postAddDriverLicence_code409_TestRA1() throws SQLException {//DriverLicence with that name already exists
    cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
    //This is like a precondition, by which we enter the driver-licence in advance:
    PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
            .name("A").build();
    given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

    //With this method we are trying to re-invest the existing driver-licence:
    given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence")
            .then().assertThat().statusCode(409);

    // deleting an already existing driver-licence:
    String driverLicenceId = admin.getDriverLicenceById("A");
    given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/driver-licence/" + driverLicenceId);
}
    @Test
    public void putUpdateDriverLicenceById_code200_TestRA() throws SQLException { //Driver licence updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the driver-licence in advance:
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                .name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

        //Using this method we try to re-enter an already existing driver-licence:
        String driverLicenceId = admin.getDriverLicenceById("A");
        UpdateDriverLicenceDto updateDriverLicenceDto = UpdateDriverLicenceDto.builder()
                .name("C").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when().put("/api/driver-licence/" + driverLicenceId)
                .then().log().all().assertThat().statusCode(200);

        // deleting an already existing driver-licence:
        String name = "C";
        db.executeUpdate("DELETE FROM `driver_licence` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateDriverLicenceById_code400_TestRA() throws SQLException { //Not valid value name DriverLicence
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the driver-licence in advance:
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                .name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

        //Using this method, try to update an existing driver-licence with the wrong "name" in the path ("path"):
        String driverLicenceId = admin.getDriverLicenceById("invalidLetter");
        UpdateDriverLicenceDto updateDriverLicenceDto = UpdateDriverLicenceDto.builder()
                .name("C").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when().put("/api/driver-licence/" + driverLicenceId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        // deleting an already existing driver-licence:
        String name = "A";
        db.executeUpdate("DELETE FROM `driver_licence` WHERE `name` = '" + name + "';");
    }
    @Test
    public void putUpdateDriverLicenceById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Invalid1!"); //enter incorrect password
        if (cookie != null) {
            //This is like a precondition, by which we enter the driver-licence in advance:
            PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                    .name("A").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

            //Using this method, try to update an existing driver-licence with an incorrect password in cookies:
            String driverLicenceId = admin.getDriverLicenceById("A");
            UpdateDriverLicenceDto updateDriverLicenceDto = UpdateDriverLicenceDto.builder()
                    .name("C").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when().put("/api/driver-licence/" + driverLicenceId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
           System.out.println("User not authenticated");
        }
        // deleting an already existing driver-licence:
        String name = "A";
        db.executeUpdate("DELETE FROM `driver_licence` WHERE `name` = '" + name + "';");
    }


    @Test
    public void putUpdateDriverLicenceById_code404_TestRA() throws SQLException { //Driver licence not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        // Get the identifier of an existing industry or null if there is no such driver-licence:
        String driverLicenceId = admin.getDriverLicenceById("A");

        if (driverLicenceId == null) {
            System.out.println("Driver LicenceId with name 'A' not found");
        } else {
            // Error: Trying to update a driver-licence that exists
            UpdateDriverLicenceDto updateDriverLicenceDto = UpdateDriverLicenceDto.builder()
                    .name("C").build();

            //Send a PUT request with an existing industry driver-licence
            Response response = given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto)
                    .when().put("/api/driver-licence/" + driverLicenceId);

            // Print the whole response for details
            System.out.println("Response: " + response.asString());

            // Check that the response code is 404
            response.then().log().all().assertThat().statusCode(404);

            // deleting an already existing driver-licence:
            String name = "A";
            db.executeUpdate("DELETE FROM `driver_licence` WHERE `name` = '" + name + "';");
        }
    }

    @Test
    public void putUpdateDriverLicenceById_code409_TestRA() throws SQLException {//driver-licence with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //This is like a precondition, by which we enter the driver-licence in advance:
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                .name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

        //With this method we try to update an already existing driver-licence with the same name:
        String driverLicenceId = admin.getDriverLicenceById("A");
        UpdateDriverLicenceDto updateDriverLicenceDto = UpdateDriverLicenceDto.builder()
                .name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateDriverLicenceDto).when().put("/api/driver-licence/" + driverLicenceId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // deleting an already existing driver-licence:
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/driver-licence/" + driverLicenceId);
    }

    @Test
    public void getListOfDriverLicenseTypes_code200_TestRA() throws SQLException {//List of driver Licence
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the driver-licence in advance:
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                .name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

        //By this method we get all driver-licence:
        given().cookie(cookie).contentType("application/json").when().get("api/driver-licence/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //Option to delete an existing driver-licence:
        String driverLicenceId = admin.getDriverLicenceById("A");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/driver-licence/" + driverLicenceId);
    }
    @Test
    public void getListOfDriverLicenceTypes_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //enter wrong mail
        //It's like a precondition by which we initially invest the driver-licence:
        if (cookie != null) {
            PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                    .name("A").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

            //By this method we get all driver-licence:
            given().cookie(cookie).contentType("application/json").when().get("api/driver-licence/all")
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
        // deleting an already existing driver-licence:
        String name = "A";
        db.executeUpdate("DELETE FROM `driver_licence` WHERE `name` = '" + name + "';");
    }
    @Test
    public void deleteDriverLicenceById_code200_TestRA() throws SQLException { //Driver licence deleted
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition by which we initially invest the driver-licence:
        PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                .name("A").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

        //With this method we delete an already existing driver-licence:
        String driverLicenceId = admin.getDriverLicenceById("A");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/driver-licence/" + driverLicenceId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void deleteDriverLicenceById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!");//enter wrong mail

        if (cookie != null) {
            //It's like a precondition by which we initially invest the driver-licence:
            PostDriverLicenceDto postDriverLicence = PostDriverLicenceDto.builder()
                    .name("A").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postDriverLicence).when().post("/api/driver-licence");

            //With this method we delete an already existing driver-licence:
            String driverLicenceId = admin.getDriverLicenceById("A");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/driver-licence/" + driverLicenceId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }

     @Test
    public void deleteDriverLicenceById_code404_TestRA() throws SQLException { //Driver licence not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        // Get the identifier of the existing driver-licence or null, if there is no such driver-licence:
        String driverLicenceId = admin.getDriverLicenceById("A");
        if (driverLicenceId == null) {
            System.out.println("Driver licence  with id 'A' does not found");
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/driver-licence/" + driverLicenceId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(404);
        }
    }

    @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}