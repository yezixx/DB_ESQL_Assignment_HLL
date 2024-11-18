package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_db";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "winter";

    private DBConfig() {
        // 객체 생성을 방지
    }

    // DB 연결을 위한 공통 메서드
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        // JDBC 드라이버 로드
        Class.forName("com.mysql.cj.jdbc.Driver");
        // DB 연결 및 반환
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}