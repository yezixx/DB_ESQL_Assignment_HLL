package menu;

import config.DBConfig;
import entity.Stores;
import entity.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
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
            System.out.println("2. 매장 관리");
            System.out.println("3. 로그아웃");
            System.out.println("=========================================");

            selectedMenu = br.readLine();
            switch (selectedMenu) {
                case "1":
                    // 출퇴근 기록 조회할 직원 선택
                    readAttendance();
                    break;
                case "2":
                    // 운영 중인 매장 조회 및 근무지 등록/삭제
                    manageStore();
                    break;
                case "3":
                    // 로그아웃
                    return;
                default:
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    private void readAttendance() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Long empId;

        System.out.println("=========================================");
        System.out.println("직원 목록");
        System.out.println("-----------------------------------------");

        // 직원 목록 출력
        if (!printEmpList()) {
            // 직원이 없으면 메소드 종료
            return;
        }

        System.out.println("-----------------------------------------");
        System.out.print("출퇴근 기록 확인할 직원 ID: ");
        String inputId = br.readLine();

        while (!isDigit(inputId)) {
            System.out.print("출퇴근 기록 확인할 직원 ID: ");
            inputId = br.readLine();
        }

        empId = Long.parseLong(inputId);
        // 직원 출퇴근 기록 조회
        getAttendanceRecord(empId);
    }

    private void manageStore() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        List<Stores> storeList = getStoreList();

        System.out.println("=========================================");
        System.out.println("매장 목록");
        System.out.println("-----------------------------------------");
        printStoreList(storeList);
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
                updateStoreInfo(storeList);
                break;
            case "3":
                deleteStoreInfo(storeList);
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

    private void addStore() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String storeName, address;

        System.out.println("=========================================");
        System.out.println("매장 등록");
        System.out.println("-----------------------------------------");
        System.out.print("매장 이름: ");
        storeName=br.readLine();
        System.out.print("매장 주소: ");
        address = br.readLine();
        insertStore(storeName, address);
    }

    private void updateStoreInfo(List<Stores> storeList) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Long updateStoreId;
        Stores updatedStore;

        System.out.println("=========================================");
        System.out.println("매장 수정");
        System.out.println("-----------------------------------------");
        System.out.print("수정할 매장의 ID: ");
        String inputId = br.readLine();
        while(!isDigit(inputId)){
            System.out.print("수정할 매장의 ID: ");
            inputId = br.readLine();
        }
        updateStoreId = Long.parseLong(inputId);
        updatedStore = findStoreById(storeList, updateStoreId);
        if (updatedStore == null) { // id가 일치하는 매장이 없으면
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("유효하지 않은 매장 ID입니다. 다시 입력해주세요.");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            updateStoreInfo(storeList);
        }
        System.out.print("매장 이름: ");
        updatedStore.setStore_name(br.readLine());
        System.out.print("매장 주소: ");
        updatedStore.setAddress(br.readLine());
        updateStore(updatedStore);
    }

    private void deleteStoreInfo(List<Stores> storeList) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Long deleteStoreId;
        Stores deletedStore;

        System.out.println("=========================================");
        System.out.println("매장 삭제");
        System.out.println("-----------------------------------------");
        System.out.print("삭제할 매장의 ID: ");
        String inputId = br.readLine();
        while(!isDigit(inputId)){
            System.out.print("삭제할 매장의 ID: ");
            inputId = br.readLine();
        }
        deleteStoreId = Long.parseLong(inputId);
        deletedStore = findStoreById(storeList, deleteStoreId);
        if(deletedStore == null){ // id가 일치하는 매장이 없으면
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("유효하지 않은 매장 ID입니다. 다시 입력해주세요.");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            deleteStoreInfo(storeList);
        }
        deleteStore(deletedStore.getStore_id());
    }
    // ================= 쿼리 =================
    private boolean printEmpList() {
        String selectQuery =
                "SELECT DISTINCT u.user_id, u.name " +
                        "FROM users u " +
                        "JOIN employee e ON u.user_id = e.user_id " +
                        "JOIN stores s ON e.store_id = s.store_id " +
                        "WHERE s.manager = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

            stmt.setLong(1, loginUser.getUser_id());

            try (ResultSet rs = stmt.executeQuery()) {

                boolean hasResult = false;

                while (rs.next()) {
                    hasResult = true;
                    Long userId = rs.getLong("user_id");
                    String name = rs.getString("name");
                    System.out.println("직원 ID: " + userId);
                    System.out.println("직원 이름: " + name);
                    System.out.println("-----------------------------------------");
                }

                if (!hasResult) {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("운영하는 매장에 소속된 직원이 없습니다.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                }

                return hasResult; // 직원이 있으면 true 반환
            }
        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("오류 발생: " + e.getMessage());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            return false; // 예외 발생시 false 반환
        }
    }

    private void getAttendanceRecord(Long empId){
        String selectQuery =
                "SELECT ar.date, ar.clockin, ar.clockout " +
                        "FROM attendancerecords ar " +
                        "JOIN employee e ON ar.employee_id = e.employee_id " +
                        "WHERE e.user_id = ? " +
                        "ORDER BY ar.date DESC";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

            stmt.setLong(1, empId);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("=========================================");
                System.out.println("출퇴근 기록");
                System.out.println("-----------------------------------------");

                boolean hasResult = false;

                while (rs.next()) {
                    hasResult = true;
                    String date = rs.getString("date");
                    String clockIn = rs.getString("clockin");
                    String clockOut = rs.getString("clockout");

                    System.out.println("날짜: " + date);
                    System.out.println("출근 시간: " + (clockIn != null ? clockIn : "출근 기록 없음"));
                    System.out.println("퇴근 시간: " + (clockOut != null ? clockOut : "퇴근 기록 없음"));
                    System.out.println("-----------------------------------------");
                }

                if (!hasResult) {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("출퇴근 기록이 없습니다.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                }
            }
        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("오류 발생: " + e.getMessage());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        }
    }

    private void insertStore(String storeName, String address) {
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

    private void updateStore(Stores store) {
        String updateQuery =
                "UPDATE Stores " +
                        "SET store_name = ?, address = ? " +
                        "WHERE store_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            // PreparedStatement에 값 설정
            stmt.setString(1, store.getStore_name());   // 새 매장명
            stmt.setString(2, store.getAddress());     // 새 주소
            stmt.setLong(3, store.getStore_id());          // 매장 ID

            // 쿼리 실행
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("매장 정보가 성공적으로 업데이트되었습니다.");
                System.out.println("=========================================");
            } else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("업데이트된 매장 정보가 없습니다.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("매장 정보 업데이트 중 오류 발생: " + e.getMessage());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        }
    }

    private void deleteStore(Long storeId) {
        String deleteQuery =
                "DELETE FROM stores " +
                        "WHERE store_id = ? " +
                        "AND manager = ? " +
                        "AND NOT EXISTS (" +
                        "    SELECT 1 FROM employee e WHERE e.store_id = stores.store_id" +
                        ")"; // 해당 매장에 근무 중인 직원이 없을 경우에만 삭제

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            // PreparedStatement에 값 설정
            stmt.setLong(1, storeId);          // 매장 ID
            stmt.setLong(2, loginUser.getUser_id());          // 점장 ID

            // 쿼리 실행
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("매장 정보가 성공적으로 삭제되었습니다.");
                System.out.println("=========================================");
            } else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("매장에 근무 중인 직원이 있거나,\n매장을 관리할 권한이 없습니다.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("매장 정보 업데이트 중 오류 발생: " + e.getMessage());
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

    private Stores findStoreById(List<Stores> storeList, long storeId) {
        return storeList.stream()
                .filter(store -> store.getStore_id() == storeId) // store_id 일치 여부 확인
                .findFirst() // 첫 번째 일치 항목 반환
                .orElse(null); // 일치하는 항목이 없으면 null 반환
    }

    private boolean isDigit(String num){
        boolean result=false;
        try{
            Long.parseLong(num);
            result=true;
        }catch (NumberFormatException e){
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("숫자를 입력해주세요.");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        }
        return result;
    }
}
