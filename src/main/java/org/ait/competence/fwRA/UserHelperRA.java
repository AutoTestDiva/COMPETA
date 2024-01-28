package org.ait.competence.fwRA;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.specification.ResponseSpecification;
import org.ait.competence.dto.NewUserDto;
import org.ait.competence.dto.ResetUserPasswordDto;
import org.ait.competence.dto.UpdateSoftSkillNameDto;
import org.hamcrest.Condition;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import static io.restassured.RestAssured.given;

public class UserHelperRA extends BaseHelperRA {
    public UserHelperRA() {
    }

    public static String loginDataEncoded(String email, String password) {
        String encodedMail;
        String encodedPassword;
        try {
            encodedMail = URLEncoder.encode(email, "UTF-8");
            encodedPassword = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "username=" + encodedMail + "&password=" + encodedPassword;
    }

    public static Response loginUserRA(String email, String password) {
        return given()
                .contentType(ContentType.fromContentType("application/x-www-form-urlencoded"))
                .body(loginDataEncoded(email, password))
                .when()
                .post("/api/login");
    }
    public Cookie getLoginCookie(String email, String password) {
        Response response = given()
                .contentType(ContentType.fromContentType("application/x-www-form-urlencoded"))
                .body(loginDataEncoded(email, password))
                .when()
                .post("/api/login");
       // return response.getDetailedCookie("JSESSIONID");

        if (response.getStatusCode() == 200) {
            return response.getDetailedCookie("JSESSIONID");
        } else {
            System.out.println("Authentication failed. Status code: " + response.getStatusCode());
            return null;
        }
    }

    public Response registerUser(String email, String password) {
        NewUserDto user = NewUserDto.builder()
                .email(email)
                .password(password)
                .build();
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register");
    }
    public Response resetUserPasswordRA(String oldPassword, String newPassword) {
        ResetUserPasswordDto resetUserPassword= ResetUserPasswordDto.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();
        return given()
                .contentType(ContentType.JSON)
                .body(resetUserPassword)
                .when()
                .put("/api/user/password-reset");
    }
    public static String getUserIdByEmail(String email) throws SQLException {
        String userId;
        try {
            userId = db.requestSelect("SELECT id FROM users WHERE email = '" + email + "';")
                    .getString(1);
        } catch (SQLException e) {
            userId = null;
            System.out.println("The user is not found" + e);
        }
        return userId;
    }

    }

