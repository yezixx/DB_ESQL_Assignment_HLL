package menu;

import config.DBConfig;
import entity.Stores;
import entity.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ManagerMenu {
    private Users loginUser;
    private String selectedMenu;

    public ManagerMenu(Users loginUser) {
        this.loginUser = loginUser;
    }


    public void selectMenu() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("=========================================");
            System.out.println(loginUser.getName() + " 점장님 환영합니다!");
            System.out.println("메뉴를 선택해주세요.");
            System.out.println("-----------------------------------------");
            System.out.println("1. 직원별 출퇴근 기록 확인");
            System.out.println("2. 직원별 출퇴근 기록 수정");
            System.out.println("3. 매장 관리");
            System.out.println("4. 로그아웃");
            System.out.println("=========================================");

            selectedMenu = br.readLine();
            switch (selectedMenu) {
                case "1":
                    // 출퇴근 기록 조회할 직원 선택
                    break;
                case "2":
                    // 출퇴근 기록 수정할 직원 선택
                    break;
                case "3":
                    // 운영 중인 매장 조회 및 근무지 등록/삭제
                    manageStore();
                    break;
                case "4":
                    // 로그아웃
                    return;
                default:
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    public void manageStore() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=========================================");
        System.out.println("매장 목록");
        System.out.println("-----------------------------------------");
        printStoreList(getStoreList());
        System.out.println("1. 매장 등록");
        System.out.println("2. 매장 수정");
        System.out.println("3. 매장 삭제");
        System.out.println("4. 이전 메뉴로 돌아가기");
        System.out.println("=========================================");

        String input = br.readLine();

        switch (input){
            case "1":
                addStore();
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            default:
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                manageStore();
        }
    }

    public void addStore() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String storeName, address;

        System.out.println("=========================================");
        System.out.println("매장 등록");
        System.out.println("-----------------------------------------");
        System.out.println("매장 이름: ");
        storeName=br.readLine();
        System.out.println("매장 주소: ");
        address = br.readLine();
        insertStore(storeName, address);
    }

    public void updateStore() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void insertStore(String storeName, String address) {
        String insertQuery =
                "INSERT INTO Stores (store_name, address, manager) " +
                        "VALUES (?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            // PreparedStatement에 값 설정
            stmt.setString(1, storeName);                  // store_name
            stmt.setString(2, address);                   // address
            stmt.setLong(3, loginUser.getUser_id());       // manager (loginUser의 user_id)

            // 쿼리 실행
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("매장 정보가 성공적으로 저장되었습니다.");
                System.out.println("=========================================");
            } else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("매장 정보 저장에 실패했습니다.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("매장 정보 저장 중 오류 발생: " + e.getMessage());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        }
    }

    private List<Stores> getStoreList() {
        // 운영중인 매장 리스트 가져오기
        String query =
                "SELECT * " +
                        "FROM Stores " +
                        "WHERE manager = ?";

        List<Stores> storeList = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // 로그인한 사용자의 user_id를 쿼리 파라미터로 설정
            stmt.setLong(1, loginUser.getUser_id());

            // 쿼리 실행
            ResultSet rs = stmt.executeQuery();

            // 결과 출력
            while (rs.next()) {
                // 쿼리 결과에서 데이터 추출
                Stores store = new Stores();
                store.setStore_id(rs.getLong("store_id"));
                store.setStore_name(rs.getString("store_name"));
                store.setAddress(rs.getString("address"));
                store.setManager(rs.getLong("manager"));

                storeList.add(store);
            }
        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("매장 목록 조회 중 오류 발생: " + e.getMessage());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        }

        return storeList; // 리스트 반환
    }

    private void printStoreList(List<Stores> storesList){
        for(Stores store : storesList){
            System.out.println("매장 ID: "+store.getStore_id());
            System.out.println("매장명: "+store.getStore_name());
            System.out.println("매장 주소: "+store.getAddress());
            System.out.println("-----------------------------------------");
        }
    }
}
