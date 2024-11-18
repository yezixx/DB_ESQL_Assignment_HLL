package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import config.DBConfig;
import entity.Users;

public class Join {
    private String id, password, checkpw, name, role;
    public void join() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=========================================");
        System.out.println("회원가입");
        System.out.println("이전 화면으로 가려면 Q를 입력해주세요.");
        System.out.println("-----------------------------------------");
        while(true) {
            System.out.print("ID: ");
            id = br.readLine(); // username에 저장
            if (id.toLowerCase().equals("q"))
                return;
            // 아이디 중복확인
            if(findById(id)!=null) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("이미 사용중인 아이디입니다. 다시 입력해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }else
                break;
        }
        while(true) {
            System.out.print("PASSWORD: ");
            password = br.readLine();
            if (password.toLowerCase().equals("q"))
                return;
            System.out.print("PASSWORD 확인: ");
            checkpw = br.readLine();
            if(password.equals(checkpw))
                break;
            else {
                System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            }
        }
        System.out.print("NAME: ");
        name = br.readLine(); // name에 저장
        if (name.toLowerCase().equals("q"))
            return;
        while(true) {
            System.out.print("ROLE(점장 1, 직원 2): ");
            role = br.readLine();
            if (role.toLowerCase().equals("q"))
                return;
            if(role.equals("1")) {
                role = "점장";
                break;
            }else if(role.equals("2")){
                role = "직원";
                break;
            }
            else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("존재하지 않는 직급입니다. 다시 선택해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }

        insertUser(id, password, name, role);
    }

    private void insertUser(String id, String password, String name, String role){
        // 쿼리문
        String query = "INSERT INTO users (username, password, name, role, create_at) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // PreparedStatement에 파라미터 설정
            stmt.setString(1, id);         // username
            stmt.setString(2, password);  // password
            stmt.setString(3, name);      // name
            stmt.setString(4, role);      // role

            // 쿼리 실행
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("회원가입이 완료됐습니다.");
                System.out.println("=========================================");
            } else {
                System.out.println("오류가 발생했습니다. 다시 시도해주세요.");
                System.out.println("=========================================");
            }

        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    private Users findById(String username){
        Users user = null; // 일치하는 결과 없을 때 null 반환

        // 쿼리문
        String query = "SELECT * FROM users WHERE username = ?";

        // 데이터베이스에서 아이디 일치하는 유저 찾음
        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // DB 연결
            Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement(query);

            // PreparedStatement에 파라미터 설정
            stmt.setString(1, username);

            // SQL 쿼리 실행
            ResultSet rs = stmt.executeQuery();

            // 결과 처리
            if (rs.next()) {
                user = new Users();
                user.setUser_id(rs.getLong("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
            }

        } catch (Exception e) {
            e.printStackTrace(); // 예외 출력
        }

        return user;
    }
}
