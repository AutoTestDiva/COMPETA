package org.ait.competence.fwRA;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.ait.competence.dto.NewAdminDto;
import org.ait.competence.dto.NewUserDto;

import java.sql.ResultSet;
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

    public void adminRole(String email) {//меняет статус на CONFIRMED в  таблице БД users_roles
        try {
            String userId = getAdminIdByEmail(email);
                    db.executeUpdate("UPDATE users_roles SET roles_id = 2 WHERE users_id  = '" + userId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

                    .getString(1);

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
                    .getString(1);
        } catch (SQLException e) {
            professionId = null;
            System.out.println("The name is not found" + e);
        }
        return professionId;
    }

    public String getJobTitleIdById(String name) {
        String jobTitleId;
        try {
            jobTitleId = db.requestSelect("SELECT id FROM job_title WHERE name = '" + name + "';")
                    .getString(1);
        } catch (SQLException e) {
            jobTitleId = null;
            System.out.println("The name is not found" + e);
        }
        return jobTitleId;
    }

    public String getIndustryById(String name) {
        String industryId;
            try {
                industryId = db.requestSelect("SELECT id FROM industry WHERE name = '" + name + "';")
                        .getString(1);
            } catch (SQLException e) {
                industryId = null;
                System.out.println("The name is not found" + e);
            }
            return industryId;
        }

    public String getHardSkillById(String name) {
        String hardSkillId;
        try {
            hardSkillId = db.requestSelect("SELECT id FROM hard_skill WHERE name = '" + name + "';")
                    .getString(1);
        } catch (SQLException e) {
            hardSkillId = null;
            System.out.println("The name is not found" + e);
        }
        return hardSkillId;
    }

    public String getEduLevelById(String name) {
        String hardSkillId;
        try {
            hardSkillId = db.requestSelect("SELECT id FROM edu_level WHERE name = '" + name + "';")
                    .getString(1);
        } catch (SQLException e) {
            hardSkillId = null;
            System.out.println("The name is not found" + e);
        }
        return hardSkillId;
    }

    public String getDriverLicenceById(String name) {
        String driverLicenceId;
        try {
            driverLicenceId = db.requestSelect("SELECT id FROM driver_licence WHERE name = '" + name + "';")
                    .getString(1);
        } catch (SQLException e) {
            driverLicenceId = null;
            System.out.println("The name is not found" + e);
        }
        return driverLicenceId;
    }


    public String getProfessionIdByName(String name) throws SQLException {
        String professionId = null;
        try {
            ResultSet resultSet = db.requestSelect("SELECT id FROM profession WHERE name = '" + name + "';");
            if (resultSet.next()) {
                professionId = resultSet.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error while getting profession ID: " + e);
        }
        return professionId;
    }
}
