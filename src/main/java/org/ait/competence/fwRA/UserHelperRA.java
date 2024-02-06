package org.ait.competence.fwRA;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.ait.competence.dto.NewUserDto;
import org.ait.competence.dto.NewUserWithoutNickNameDto;

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

    public Response registerUser(String email, String password, String nickName) {
        NewUserDto user = NewUserDto.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    public Response registerUserWithoutNickName_code400(String email, String password) {
        NewUserWithoutNickNameDto user = NewUserWithoutNickNameDto.builder()
                .email(email)
                .password(password)
                .build();
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register");
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

    public String getUserConfirmCodeById(String userId) throws SQLException {
        String userConfirmCode;
        try {
            userConfirmCode = db.requestSelect("SELECT code FROM confirmation_code WHERE user_id = '" + userId + "';")
                    .getString("code"); // Используйте название колонки вместо индекса
        } catch (SQLException e) {
            userConfirmCode = null;
            System.out.println("The confirmation-code is not found" + e);
        }
        return userConfirmCode;
    }

    public void cleanupDatabase(String email) { //метод,  меняющий в таблице в БД confirmation_code в графе is_used 1 на 0, т.е. чтоб CODE будто ранее не использовался
        // код для обновления значений в базе данных; например, сброс значения is_used обратно на 0
        // и очистка других данных, если необходимо
        try {
            String userId = getUserIdByEmail(email);
            db.executeUpdate("UPDATE confirmation_code SET is_used = 0 WHERE user_id = '" + userId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void userStatusConfirmed(String email) {//меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        try {
            String userId = getUserIdByEmail(email);
            if (userId != null) {
                db.executeUpdate("UPDATE users SET user_status = 'CONFIRMED' WHERE id = '" + userId + "';");
                db.executeUpdate("UPDATE users_aud SET user_status = 'CONFIRMED' WHERE id = '" + userId + "';");
            } else {
                System.out.println("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void userRole(String email) {//меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        try {
            String userId = getUserIdByEmail(email);
            db.executeUpdate("UPDATE users_roles SET roles_id = 1 WHERE users_id  = '" + userId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}