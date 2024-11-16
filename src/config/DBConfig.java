package config;

public class DBConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_db";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "winter";

    private DBConfig() {
        // 객체 생성을 방지
    }
}