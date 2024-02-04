package org.ait.competence.fwRA;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.ait.competence.dto.NewAdminDto;
import org.ait.competence.dto.NewUserDto;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class AdminHelperRA extends BaseHelperRA {
    public AdminHelperRA() {
    }

    public Response registerAdmin(String email, String password, String nickName) {
        NewAdminDto admin = NewAdminDto.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
        return given()
                .contentType(ContentType.JSON)
                .body(admin)
                .when()
                .post("/api/auth/register");
    }

    public void adminStatusConfirmed(String email) {//меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        try {
            String userId = getAdminIdByEmail(email);
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

    public void adminRole(String email) {//меняет статус на CONFIRMED в 2-х таблицах БД users, users_aud
        try {
            String userId = getAdminIdByEmail(email);
           // if (userId != null) {
                db.executeUpdate("UPDATE users_roles SET roles_id = 2 WHERE users_id  = '" + userId + "';");
//            } else {
//                System.out.println("Admin not found");
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//                db.executeUpdate("UPDATE users_roles SET roles_id = 2 WHERE users_id  = '" + userId + "';");
//        } catch (SQLException e) {
//                e.printStackTrace();
    }

    public static String getAdminIdByEmail(String email) throws SQLException {
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

    public void adminStatusBanned(String email) {//меняет статус на BANNED в 2-х таблицах БД users, users_aud
            try {
                String userId = getAdminIdByEmail(email);
                if (userId != null) {
                    db.executeUpdate("UPDATE users SET user_status = 'BANNED' WHERE id = '" + userId + "';");
                    db.executeUpdate("UPDATE users_aud SET user_status = 'BANNED' WHERE id = '" + userId + "';");
                } else {
                    System.out.println("User not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    public String getSoftSkillById(String name) throws SQLException {
        String softSkillId;
        try {
            softSkillId = db.requestSelect("SELECT id FROM soft_skill WHERE name = '" + name + "';")
                    .getString(1); // Используйте название колонки вместо индекса
        } catch (SQLException e) {
            softSkillId = null;
            System.out.println("The name is not found" + e);
        }
        return softSkillId;
    }

    public String getProfessionById(String name) throws SQLException {
        String professionId;
        try {
            professionId = db.requestSelect("SELECT id FROM profession WHERE name = '" + name + "';")
                    .getString(1); // Используйте название колонки вместо индекса
        } catch (SQLException e) {
            professionId = null;
            System.out.println("The name is not found" + e);
        }
        return professionId;
    }
}
