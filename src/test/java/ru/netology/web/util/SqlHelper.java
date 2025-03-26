package ru.netology.web.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

@UtilityClass
public class SqlHelper {
    private String connUrl = "jdbc:mysql://localhost:3307/app";
    private String connUser = "app";
    private String connPass = "example";

    @SneakyThrows
    public String getVerifyCode(String login) {

        String verifyCodeSqlQuery = "SELECT auth_codes.code "
                        + "FROM users "
                        + "JOIN auth_codes ON users.id = auth_codes.user_id "
                        + "WHERE users.login = ?"
                        + "ORDER BY auth_codes.created DESC;";

        String result;

        try (Connection conn = DriverManager.getConnection(connUrl, connUser, connPass)) {
            QueryRunner runner = new QueryRunner();

            result = runner.query(conn, verifyCodeSqlQuery, new ScalarHandler<>(), login);
        }
        return result;
    }

    @SneakyThrows
    public String userStatus(String login) {
        String userStatusQuery =
                "SELECT status " +
                "FROM users " +
                "WHERE login = ?;";
        String result;

        try (Connection conn = DriverManager.getConnection(connUrl, connUser, connPass)) {
            QueryRunner runner = new QueryRunner();

            result = runner.query(conn, userStatusQuery, new ScalarHandler<>(), login);
        }
        return result;
    }

    @SneakyThrows
    public void cleaningDB() {

        try (Connection conn = DriverManager.getConnection(connUrl, connUser, connPass)){
            QueryRunner runner = new QueryRunner();
            String[] tableNames = {"card_transactions", "cards", "auth_codes", "users"};
            String foreignKeySet = "SET FOREIGN_KEY_CHECKS = 0;";

            runner.update(conn, foreignKeySet);

            for (int i = 0; i < 4; i++) {
                String removeQuery = "TRUNCATE table " + tableNames[i] + ";";
                runner.update(conn, removeQuery);
            }
            foreignKeySet = "SET FOREIGN_KEY_CHECKS = 1;";
            runner.update(conn, foreignKeySet);
        }
    }

    @SneakyThrows
    public void changeUserStatus(String login, String status) {
        String changeStatusQuery =
                "UPDATE users " +
                "SET status = ? " +
                "WHERE login = ?;";

        try (Connection conn = DriverManager.getConnection(connUrl, connUser, connPass)) {
            QueryRunner runner = new QueryRunner();

            if (status.equals("active")) {
                runner.update(conn, changeStatusQuery, "blocked", login);
            } else {
                runner.update(conn, changeStatusQuery, "active", login);
            }
        }
    }
}