package menu;

import config.DBConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class MainMenu {
    public void mainMenu() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String input;
        createTables();

        while(true) {
            System.out.println("=========================================");
            System.out.println("메뉴를 선택해주세요.");
            System.out.println("-----------------------------------------");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 종료");
            System.out.println("=========================================");
            input = br.readLine();
            switch (input){
                case "1":
                    new Join().join();
                    break;
                case "2":
                    new Login().login();
                    break;
                case "3":
                    System.out.println("=========================================");
                    System.out.println("프로그램을 종료합니다.");
                    System.out.println("=========================================");
                    return;
                default:
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    private void createTables(){
        // 쿼리문
        // 테이블 생성 쿼리 정의
        String[] tableQueries = {
                "CREATE TABLE IF NOT EXISTS Users (" +
                        "    user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "    username VARCHAR(50) NOT NULL UNIQUE, " +
                        "    password VARCHAR(50) NOT NULL, " +
                        "    name VARCHAR(50) NOT NULL, " +
                        "    role VARCHAR(50) NOT NULL, " +
                        "    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");",
                "CREATE TABLE IF NOT EXISTS Stores (" +
                        "    store_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "    store_name VARCHAR(255) NOT NULL, " +
                        "    address VARCHAR(255) NOT NULL, " +
                        "    manager BIGINT NOT NULL, " +
                        "    CONSTRAINT fk_stores_manager " +
                        "        FOREIGN KEY (manager) REFERENCES users(user_id) " +
                        "        ON DELETE CASCADE" +
                        ");",
                "CREATE TABLE IF NOT EXISTS Employee (" +
                        "    employee_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "    user_id BIGINT NOT NULL, " +
                        "    store_id BIGINT NOT NULL, " +
                        "    FOREIGN KEY (user_id) REFERENCES Users(user_id), " +
                        "    FOREIGN KEY (store_id) REFERENCES Stores(store_id)" +
                        ");",
                "CREATE TABLE IF NOT EXISTS EmployeeStores ( " +
                        "    employee_id BIGINT NOT NULL, " +
                        "    store_id BIGINT NOT NULL, " +
                        "    PRIMARY KEY (employee_id, store_id), " +
                        "    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id), " +
                        "    FOREIGN KEY (store_id) REFERENCES Stores(store_id)" +
                        ");",
                "CREATE TABLE IF NOT EXISTS AttendanceRecords ( " +
                        "    record_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "    employee_id BIGINT NOT NULL, " +
                        "    date DATE NOT NULL, " +
                        "    clockIn TIMESTAMP, " +
                        "    clockOut TIMESTAMP, " +
                        "    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)" +
                        ");"
        };

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            for (String query : tableQueries) {
                stmt.executeUpdate(query); // 각 쿼리를 실행
                System.out.println("테이블 생성 완료: " + query.split(" ")[5]);
            }

        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }
}