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

public class IndustryTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    @Test()
    public void postAddNewIndustry_code201_TestRA1() throws SQLException { //Industry added
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(postIndustry)
                .when()
                .post("/api/industry")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201);
        System.out.println(postIndustry.getName());

        // deleting an already existing industry:
        String industryId = admin.getIndustryById("education1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId);
    }

    @Test()
    public void postAddNewIndustry_code400_TestRA1() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("")
                .build();
        given()
                .cookie(cookie)
                .contentType("application/json")
                .body(postIndustry)
                .when()
                .post("/api/industry")
                .then()
                .assertThat().statusCode(400);
    }

    @Test()
    public void postAddNewIndustry_WithInvalidEmail_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //сделать ошибку в почте
        if (cookie != null) {
            //It's like a precondition that we put in beforehand:
            PostIndustryDto postIndustry = PostIndustryDto.builder()
                    .name("education1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");
            given().cookie(cookie).contentType(ContentType.JSON).when().post("/api/industry")
                    .then()
                    .assertThat().statusCode(401);
        } else {
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }
 /* @Test
      public void postAddNewIndustry_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
          admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
          admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
          admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
          cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");
            if (cookie != null) {
                //This is like a precondition, by which we enter the edu-level in advance:
               PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry")
                .then()
                .log().all()
                .assertThat().statusCode(403);
        System.out.println(postAddHardSkill.getName());
            } else {
                // Handling the case when authentication fails
                System.out.println("User not authenticated");
            }
    }*/

    @Test()
    public void postAddNewIndustry_code409_TestRA1() throws SQLException {//Industry with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition that we put in beforehand:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //With this method we are trying to re-invest the existing industry:
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry")
                .then()
                .assertThat()
                .statusCode(409);

        // deleting an already existing industry:
        String industryId = admin.getIndustryById("education1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId);
    }

    @Test
    public void putUpdateIndustryById_code200_TestRA() throws SQLException { //Industry updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition that we put in beforehand:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //Using this method we try to re-enter an already existing industry:
        String industryId = admin.getIndustryById("education1");
        UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder()
                .name("education2").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto).when().put("/api/industry/" + industryId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        // deleting an already existing industry:
        String name = "education2";
        db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateIndustryById_code400_TestRA() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition that we put in beforehand:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //Using this method, try to update an existing industry with the wrong "name" in the path ("path"):
        String industryId = admin.getIndustryById("invalidName");
        UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder()
                .name("education2").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto).when().put("/api/industry/" + industryId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        // deleting an already existing industry:
        String name = "education1";
        db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateIndustryById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Invalid1!"); //enter incorrect password
        if (cookie != null) {
            //It's like a precondition that we put in beforehand:
            PostIndustryDto postIndustry = PostIndustryDto.builder()
                    .name("education1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

            //Using this method, try to update an existing industry with an incorrect password in cookies:
            String industryId = admin.getIndustryById("education1");
            UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder()
                    .name("education2").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto).when().put("/api/industry/" + industryId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Handling the case when authentication fails:
            System.out.println("User not authenticated");
        }
        // deleting an already existing industry:
        String name = "education1";
        db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");
    }

  /* @Test
         public void putUpdateIndustryById_AccessDenied_code403_TestRA() throws SQLException {//Access denied for user with email <{0}> and role {1}
         // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
            //вместо предусловия выше, т.к. регистрация с другими параметрами:
             admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
             admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
             admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
             cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

             if (cookie != null) {
                 //это словно предусловие, которым заранее вкладываем industry:
                 PostIndustryDto postIndustry = PostIndustryDto.builder()
                         .name("education1").build();
                 given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

                 //этим методом пытаемся обновить уже имеющуюся industry:
                 String industryId = admin.getIndustryById("education1");
                 UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder()
                         .name("education2").build();
                 given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto).when().put("/api/industry/" + industryId)
                         .then()
                         .log().all()
                         .assertThat().statusCode(403);
             } else {
                System.out.println("User not authenticated");
             }
       // deleting an already existing industry:
       String name = "education1";
       db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");
   }*/

    @Test
    public void putUpdateIndustryById_code404_TestRA() throws SQLException { //Industry not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        // Get the identifier of an existing industry or null if there is no such industry:
        String industryId = admin.getIndustryById("education1");

        if (industryId == null) {
            System.out.println("Industry with name 'education1' not found");
        } else {
            // Error: Trying to update an industry that exists
            UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder()
                    .name("education2").build();

            //Send a PUT request with an existing industry identifier
            Response response = given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto)
                    .when().put("/api/industry/" + industryId);

            // Print the whole response for details (can be removed in production code)
            System.out.println("Response: " + response.asString());

            // Check that the response code is 404
            response.then().log().all().assertThat().statusCode(404);
        }
    }

    @Test
    public void putUpdateIndustryById_code409_TestRA() throws SQLException {//Industry with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition that we put in beforehand:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //With this method we try to update an already existing industry with the same name:
        String industryId = admin.getIndustryById("education1");
        UpdateIndustryDto updateIndustryDto = UpdateIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateIndustryDto).when().put("/api/industry/" + industryId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        // deleting an already existing industry:
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId);
    }

    @Test
    public void getListOfIndustries_code200_TestRA() throws SQLException {//List of industries
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the industry:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //By this method we get all industries:
        given().cookie(cookie).contentType("application/json").when().get("api/industry/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //Option to delete an existing industry:
        String industryId = admin.getIndustryById("education1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId);
    }

    @Test()
    public void getListOfIndustries_code400_TestRA1() throws SQLException { //Validation errors
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        given()
                .cookie(cookie)
                .contentType("application/json")
                .when()
                .post("/api/industry")
                .then()
                .log().all()
                .assertThat().statusCode(400);
    }

    @Test
    public void getListOfIndustries_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //enter wrong mail
        //It's like a precondition by which we initially invest the industry:
        if (cookie != null) {
            PostIndustryDto postIndustry = PostIndustryDto.builder()
                    .name("education1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

            //By this method we get all industry:
            given().cookie(cookie).contentType("application/json").when().get("api/industry/all")
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
        // deleting an already existing industry:
        String name = "education1";
        db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");
    }

  /*  @Test
    public void getListOfIndustries_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //Changes the status to BANNED in 2 database tables users, users_aud
        admin.adminRole("admin0@gmail.com"); //Аssign the ADMIN role in the database
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
            //это словно предусловие, которым я первоначально вкладываю industry:
            PostIndustryDto postIndustry = PostIndustryDto.builder()
                    .name("education1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

            //этим методом получаем все industries:
            given().cookie(cookie).contentType("application/json").when().get("api/industry/all")
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
        } else {
             System.out.println("Authentication failed. Cannot proceed with the test.");
        }
        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "education1";
        db.executeUpdate("DELETE FROM `industry` WHERE `name` = '" + name + "';");
    }*/

    @Test
    public void deleteIndustryById_code200_TestRA() throws SQLException { //Industry deleted
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition by which we initially invest the industry:
        PostIndustryDto postIndustry = PostIndustryDto.builder()
                .name("education1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

        //With this method we delete an already existing industry:
        String industryId = admin.getIndustryById("education1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void deleteIndustryById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!");//enter wrong mail

        //It's like a precondition by which we initially invest the industry:
        if (cookie != null) {
            PostIndustryDto postIndustry = PostIndustryDto.builder()
                    .name("education1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

            //With this method we delete an already existing industry:
            String industryId = admin.getIndustryById("education1");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }

     /*  @Test
    public void deleteIndustryById_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //меняет статус на BANNED в 2-х таблицах БД users, users_aud
        admin.adminRole("admin0@gmail.com"); //присваиваем в базе данных роль АДМИНА
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");

        if (cookie != null) {
             //это словно предусловие, которым я первоначально вкладываю industry:
            PostIndustryDto postIndustry = PostIndustryDto.builder()
                    .name("education1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postIndustry).when().post("/api/industry");

            //этим методом пытаемся удалить industry:
            String industryId = admin.getIndustryById("education1");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(403);
          } else {
             System.out.println("Authentication failed. Cannot proceed with the test.");
        }
           // вариант удаления уже имеющегося industry:
           String industryId = admin.getIndustryById("education1");
           given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId);
       }*/

    @Test
    public void deleteIndustryById_code404_TestRA() throws SQLException { //Industry not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        // Get the identifier of the existing industry or null, if there is no such industry:
        String industryId = admin.getIndustryById("education1");
        if (industryId == null) {
            System.out.println("Industry  with id 'education1' does not found");
        } else {
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/industry/" + industryId)
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