package ru.netology.web.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;

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
}