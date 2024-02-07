package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostTitleOfJobDto;
import org.ait.competence.dto.UpdateTitleOfJobDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class TitleOfJobTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    @Test()
    public void postAddNewTitleOfJob_code201_TestRA1() throws SQLException { //Title of job added
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title")
                .then().log().all().assertThat().statusCode(201);

        //first method that removes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
        //       String name = "junior1";
        //       db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing jobTitle (not through the database):
        String jobTitleId = admin.getJobTitleIdById("junior1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/job-title/" + jobTitleId);
    }

    @Test()
    public void postAddNewTitleOfJob_code400_TestRA1() throws SQLException { //Not valid value name JobTitle
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title")
                .then().log().all().assertThat().statusCode(400);
    }

    @Test()
    public void postAddNewTitleOfJob_WithInvalidEmail_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //enter wrong mail
        //It's like a precondition that we put a jobTitle in beforehand:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        //т.е. не вносим cookie
        given().contentType("application/json").body(postTitleOfJob).when().post("/api/job-title").then()
                .log().all().assertThat().statusCode(401);
    }
     @Test()
    public void postAddNewTitleOfJob_code409_TestRA1() throws SQLException {//JobTitle with that name already exists
         cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
         //It's like a precondition that we put a jobTitle in beforehand:
         PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
         given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //With this method we try to re-embed an existing jobTitle:
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title")
                .then().log().all().assertThat().statusCode(409);

        //First method that deletes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
        //       String name = "junior1";
        //       db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");

        // The second option is to delete an existing jobTitle (not through the database):
        String jobTitleId = admin.getJobTitleIdById("junior1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/job-title/" + jobTitleId);
    }

    @Test
    public void putUpdateTitleOfJobById_code200_TestRA() throws SQLException { //Title of job updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put a jobTitle in beforehand:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //Using this method we try to re-enter an already existing jobTitle:
        String jobTitleId = admin.getJobTitleIdById("junior1");
        UpdateTitleOfJobDto updateTitleOfJobDto = UpdateTitleOfJobDto.builder()
                .name("junior2").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateTitleOfJobDto).when().put("/api/job-title/" + jobTitleId)
                .then().log().all().assertThat().statusCode(200);

        //First method that deletes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
        String name = "junior2";
        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateTitleOfJobById_code400_TestRA() throws SQLException { //Not valid value name EduLevel
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put a jobTitle in beforehand:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //Using this method, try to update an existing jobTitle with the wrong "name" in the path ("path"):
        String jobTitleId = admin.getJobTitleIdById("junior1");
        UpdateTitleOfJobDto updateTitleOfJobDto = UpdateTitleOfJobDto.builder()
                .name("").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateTitleOfJobDto).when()
                .put("/api/job-title/" + jobTitleId).then().log().all().assertThat().statusCode(400);

        //First method that deletes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
        String name = "junior1";
        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateTitleOfJobById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put a jobTitle in beforehand:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //Using this method we try to re-enter an already existing jobTitle:
        String jobTitleId = admin.getJobTitleIdById("junior1");
        UpdateTitleOfJobDto updateTitleOfJobDto = UpdateTitleOfJobDto.builder().name("junior2").build();
       //т.е. не вкладываем
        given().contentType(ContentType.JSON).body(updateTitleOfJobDto).when().put("/api/job-title/" + jobTitleId)
                .then().log().all().assertThat().statusCode(401);

        //First method that deletes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
        String name = "junior1";
        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");

    }
//       @Test //возможно баг. и в целом стоит переделать как в Profession
//    public void putUpdateTitleOfJobById_code404_TestRA() throws SQLException { //Title of job not found
//        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
//
//        // Get the identifier of the existing jobTitle or null, if there is no such jobTitle:
//        String jobTitleId = admin.getJobTitleIdById("junior1");
//        if (jobTitleId == null) {
//            System.out.println("JobTitle  with id 'job_title' does not found");
//        } else {
//            UpdateTitleOfJobDto updateTitleOfJobDto = UpdateTitleOfJobDto.builder()
//                    .name("junior2")
//                    .build();
//            // Send a PUT request with the correct jobTitle ID:
//            given().cookie(cookie).contentType(ContentType.JSON).body(updateTitleOfJobDto)
//                    .when().put("/api/job-title/" + jobTitleId)
//                    .then().assertThat().statusCode(404);
//        }
//    }


    @Test
    public void putUpdateTitleOfJobById_code409_TestRA() throws SQLException {//JobTitle with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the jobTitle:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //With this method we try to update an already existing jobTitle with the same name:
        String jobTitleId = admin.getJobTitleIdById("junior1");
        UpdateTitleOfJobDto updateTitleOfJobDto = UpdateTitleOfJobDto.builder()
                .name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateTitleOfJobDto).when().put("/api/job-title/" + jobTitleId)
                .then().log().all().assertThat().statusCode(409);

        // Method that deletes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        // in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
//        String name = "junior1";
//        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing jobTitle (not through the database):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/job-title/" + jobTitleId);
    }

    @Test
    public void getListTitleOfJob_code200_TestRA() throws SQLException {//List of Title of job
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the jobTitle:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //By this method we get all jobTitles:
        given().cookie(cookie).contentType("application/json").when().get("api/job-title/all").then()
                .log().all().assertThat().statusCode(200);

        // Method that deletes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        // in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
//        String name = "junior1";
//        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");

       // The second variant of deleting an already existing jobTitle (not through the database):
        String jobTitleId = admin.getJobTitleIdById("junior1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/job-title/" + jobTitleId);
    }

    @Test
    public void getListTitleOfJob_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the jobTitle:
        PostTitleOfJobDto postTitleOfJob = PostTitleOfJobDto.builder().name("junior1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postTitleOfJob).when().post("/api/job-title");

        //By this method we get all jobTitles:
        given().contentType("application/json").when().get("api/job-title/all").then()
                .log().all().assertThat().statusCode(401);

        // Method that deletes the above jobTitle by "name" from the database, so that it will be passed automatically afterwards
        // in JENKINS-e and since deleting a user does not delete jobTitle from the table automatically:
//        String name = "junior1";
//        db.executeUpdate("DELETE FROM `job_title` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing jobTitle (not through the database):
        String jobTitleId = admin.getJobTitleIdById("junior1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/job-title/" + jobTitleId);
    }

   @AfterMethod
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}