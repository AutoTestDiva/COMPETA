package org.ait.competence.tests.restAssuredTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.ait.competence.dto.PostAllProfessionsDto;
import org.ait.competence.dto.UpdateProfessionNameDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class ProfessionTestsRA extends TestBaseRA {
    private Cookie cookie;

    @BeforeMethod
    public void preconditionRA() throws SQLException {
        // register admin:
        admin.registerAdmin("admin1@gmail.com", "Admin001!", "superAdmin1");
        admin.adminStatusConfirmed("admin1@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin1@gmail.com"); //assign the ADMIN role in the database
    }

    @Test()
    public void postAddNewProfession_code201_TestRA1() throws SQLException { //Profession added
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

        //The first method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        //       String name = "programmer1";
        //       db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing profession (not through the database):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test()
    public void postAddNewProfession_code400_TestRA1() throws SQLException { //Not valid value name EduLevel
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
    public void postAddNewProfession_WithInvalidEmail_code401_TestRA1() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //enter the wrong mail
        if (cookie != null) {
            given().cookie(cookie).contentType(ContentType.JSON).when().post("/api/profession")
                    .then()
                    .assertThat().statusCode(401);
        } else {
            //Handling the case when authentication failed
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }
    }

   /* @Test
    public void postAddNewProfession_code403_TestRA() throws SQLException { // Access denied for user with email <{0}> and role {1}
                                                                            //не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //changes the status to CONFIRMED in 2 database tables users, users_aud
        admin.adminRole("admin0@gmail.com"); //assign the ADMIN role in the database
        cookie = user.getLoginCookie("admin0@gmail.com", "Admin000!");
        given().cookie(cookie).when().post("/api/profession")
                .then()
                .assertThat().statusCode(403);
    }*/

    @Test()
    public void postAddNewProfession_code409_TestRA1() throws SQLException { //Profession with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //Using this method we try to re-enter an already existing profession:
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession")
                .then()
                .assertThat().statusCode(409);

        //The first method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        //       String name = "programmer1";
        //       db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing profession (not through the database):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }


    @Test
    public void putUpdateProfessionById_code200_TestRA() throws SQLException { //Profession updated
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //Using this method we try to re-enter an already existing profession:
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
               String name = "programmer2";
               db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
   }

    @Test
    public void putUpdateProfessionById_code400_TestRA() throws SQLException { //Not valid value name EduLevel
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //This is like a precondition, by which we enter the profession in advance:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //Using this method, try to update an existing profession with the wrong "name" in the path ("path"):
        String professionId = admin.getProfessionById("invalidName");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer2")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(400);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    @Test
    public void putUpdateProfessionById_code401_TestRA() throws SQLException { //User not authenticated
        cookie = user.getLoginCookie("admin1@gmail.com", "Invalid1!"); //enter incorrect password
        if (cookie != null) {
            //This is like a precondition, by which we enter the profession in advance:
            PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

            //Using this method, try to update an existing profession with an incorrect password in cookies:
            String professionId = admin.getProfessionById("programmer1");
            UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                    .name("programmer2")
                    .build();
            given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Handling the case when authentication fails:
            System.out.println("User not authenticated");
        }

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
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
                //This is like a precondition by which a profession is initially entered:
                 PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                        .name("programmer1").build();
                given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

                //этим методом я пытаюсь обновить уже имеющуюся профессию:
                String professionId = admin.getProfessionById("programmer1");
                UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                        .name("programmer2")
                        .build();
                given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                        .then()
                        .log().all()
                        .assertThat().statusCode(403);
            } else {
                // Handling the case when authentication fails
                System.out.println("User not authenticated");
            }
           //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
        }*/
    @Test
    public void putUpdateProfessionById_code404_TestRA() throws SQLException { //Profession not found
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        // Get the identifier of the existing profession or null, if there is no such profession:
        String professionId = admin.getProfessionById("programmer1");
        if (professionId == null) {
            System.out.println("Profession with id 'programmer' does not found");
        } else {
            UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                    .name("programmer2")
                    .build();

            // Send a PUT request with the correct profession ID:
            given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto)
                    .when().put("/api/profession/" + professionId)
                    .then().assertThat().statusCode(404);
        }
    }

    @Test
    public void putUpdateProfessionById_code409_TestRA() throws SQLException {//Profession with that name already exists
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition by which we initially invest the profession:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //With this method we try to update an already existing profession with the same name:
        String professionId = admin.getProfessionById("programmer1");
        UpdateProfessionNameDto updateProfessionNameDto = UpdateProfessionNameDto.builder()
                .name("programmer1")
                .build();
        given().cookie(cookie).contentType(ContentType.JSON).body(updateProfessionNameDto).when().put("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(409);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
//        String name = "programmer1";
//        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing profession (not through the database):
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void getListOfProfessions_code200_TestRA() throws SQLException {//List of profession
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");
        //It's like a precondition by which we initially invest the profession:
        PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

        //By this method we get all professions:
        given().cookie(cookie).contentType("application/json").when().get("api/profession/all")
                .then()
                .log().all()
                .assertThat().statusCode(200);

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
//        String name = "programmer1";
//        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing profession (not through the database):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    }

    @Test
    public void getListOfProfessions_code401_TestRA() throws SQLException {     //User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!"); //enter wrong mail
        //It's like a precondition by which we initially invest the profession:
        if (cookie != null) {
            PostAllProfessionsDto postAllProfession = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfession).when().post("/api/profession");

            //By this method we get all professions:
            given().cookie(cookie).contentType("application/json").when().get("api/profession/all")
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Handling the case when authentication failed:
            System.out.println("Authentication failed. Cannot proceed with the test.");
        }

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        String name = "programmer1";
        db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    }

    /*
 @Test
    public void getListOfProfessions_code403_TestRA() throws SQLException { // не работает, т.к. не воспринимает с БД роль пользователя "BANNED"
        //вместо предусловия выше, т.к. регистрация с другими параметрами:
        admin.registerAdmin("admin0@gmail.com", "Admin000!", "superAdmin0");
        admin.adminStatusBanned("admin0@gmail.com"); //Changes the status to BANNED in 2 database tables users, users_aud
        admin.adminRole("admin0@gmail.com"); //Аssign the ADMIN role in the database
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

        //The  method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
         String name = "programmer1";
         db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");
    } */

    @Test
    public void deleteProfessionById_code200_TestRA() throws SQLException { //Profession deleted
        cookie = user.getLoginCookie("admin1@gmail.com", "Admin001!");

        //It's like a precondition by which we initially invest the profession:
        PostAllProfessionsDto postAllProfessions = PostAllProfessionsDto.builder()
                .name("programmer1").build();
        given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfessions).when().post("/api/profession");

        //With this method we delete an already existing profession:
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void deleteProfessionById_code401_TestRA() throws SQLException {//User not authenticated
        cookie = user.getLoginCookie("invalid@gmail.com", "Admin001!");//enter wrong mail

        //It's like a precondition by which we initially invest the profession:
        if (cookie != null) {
            PostAllProfessionsDto postAllProfessions = PostAllProfessionsDto.builder()
                    .name("programmer1").build();
            given().cookie(cookie).contentType(ContentType.JSON).body(postAllProfessions).when().post("/api/profession");

            //With this method we delete an already existing profession:
            String professionId = admin.getProfessionById("programmer1");
            given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId)
                    .then()
                    .log().all()
                    .assertThat().statusCode(401);
        } else {
            // Handling the case when authentication fails
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

         //The first method that deletes the above mentioned profession by "name" from the database, so that it will be passed automatically afterwards
        //in JENKINS-e and since deleting a user does not delete the profession from the table automatically:
        //       String name = "programmer1";
        //       db.executeUpdate("DELETE FROM `profession` WHERE `name` = '" + name + "';");

        // The second variant of deleting an already existing profession (not through the database):
        String professionId = admin.getProfessionById("programmer1");
        given().cookie(cookie).contentType(ContentType.JSON).when().delete("/api/profession/" + professionId);
    } */

    @AfterMethod
//  @Test
    public static void postConditionRA() throws SQLException {
        String[] args = {"admin1@gmail.com"};
        deleteUser.deleteUserFromDB(args);
    }
}
