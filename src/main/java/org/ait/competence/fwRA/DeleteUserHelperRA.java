package org.ait.competence.fwRA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.ait.competence.DataBaseRA.connection;

public class DeleteUserHelperRA extends BaseHelperRA {

    public static void deleteUser(String email) throws SQLException {
        String userId = UserHelperRA.getUserIdByEmail(email);
        if (userId != null) {
            deleteUserById(userId);
        }
    }

    private static void deleteUserById(String userId) throws SQLException {
        db.requestDelete("DELETE FROM users_roles WHERE users_id = " + userId + ";");
        // db.requestDelete("DELETE FROM user_profile WHERE id = " + userId + ";");
        db.requestDelete("DELETE FROM users_aud WHERE id = " + userId + ";");
        //db.requestDelete("DELETE FROM users WHERE id = " + userId + ";");
    }

    public static void deleteUserByEmail(String email) {
        try (Connection connection = connection()) {
            connection.setAutoCommit(false);
            // Найти идентификатор пользователя по электронной почте
            int userId = getUserIdByEmail(connection, email);
            if (userId != -1) {
                // Обнулить user_profile_id в таблице users для данного пользователя
                updateUserProfileId(connection, userId, null);
                // Теперь можно удалить записи из user_profile, связанные с пользователем
                deleteUserProfileByUserId(connection, userId);
                connection.commit();
            } else {
                System.out.println("Пользователь с email '" + email + "' не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getUserIdByEmail(Connection connection, String email) throws SQLException {
        String query = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt("id") : -1;
        }
    }

    private static void updateUserProfileId(Connection connection, int userId, Integer userProfileId) throws SQLException {
        String query = "UPDATE users SET user_profile_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, userProfileId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        }
    }

    private static void deleteUserProfileByUserId(Connection connection, int userId) throws SQLException {
        String query = "DELETE FROM user_profile WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteUserUserById(String email) throws SQLException {
        String userId = UserHelperRA.getUserIdByEmail(email);
        if (userId != null) {
            db.requestDelete("DELETE FROM users WHERE id = " + userId + ";");
        }
    }

    public static void deleteUserFromDB(String[] args) throws SQLException {
        String email = args[0];
        deleteUser(email);
        deleteUserByEmail(email);
        deleteUserUserById1(email);
    }

    public static void deleteUserUserById1(String email) throws SQLException {
        String userId = UserHelperRA.getUserIdByEmail(email);
        if (userId != null) {
            db.requestDelete("DELETE FROM confirmation_code WHERE user_id = " + userId + ";");
            db.requestDelete("DELETE FROM users WHERE id = " + userId + ";");
        }
    }
}