package menu;

import config.DBConfig;
import entity.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {
    private String id, password;

    public void login() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=========================================");
        System.out.println("로그인");
        System.out.println("이전 화면으로 가려면 Q를 입력해주세요.");
        System.out.println("-----------------------------------------");
        while(true){
        System.out.print("ID: ");
        id = br.readLine(); // username과 비교
        if(id.toLowerCase().equals("q"))
            return;
        System.out.print("PASSWORD: ");
        password = br.readLine();
        if(password.toLowerCase().equals("q"))
            return;
        Users users = findUser(id, password);
            if(users!=null/*아이디와 패스워드가 db에 저장된 정보와 일치하면*/) {
                System.out.println("로그인이 완료됐습니다.");
                System.out.println("=========================================");
                // 로그인 이후 메뉴 표시
                // 유저가 점장 -> 점장메뉴
                // 유저가 직원 -> 직원메뉴
                break;
            }
            else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    private Users findUser(String username, String password){
        Users user = null; // 일치하는 결과 없을 때 null 반환

        // 쿼리문
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        // 데이터베이스에서 아이디와 비밀번호 일치하는 유저 찾음
        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // DB 연결
            Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement(query);

            // PreparedStatement에 파라미터 설정
            stmt.setString(1, username);
            stmt.setString(2, password);

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