package menu;

import config.DBConfig;
import entity.Stores;
import entity.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMenu {
    private Users loginUser;
    private String selectedMenu;

    public EmployeeMenu(Users loginUser) {
        this.loginUser = loginUser;
    }


    public void selectMenu() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("=========================================");
            System.out.println(loginUser.getName() + " 직원님 환영합니다!");
            System.out.println("메뉴를 선택해주세요.");
            System.out.println("-----------------------------------------");
            System.out.println("1. 출근");
            System.out.println("2. 퇴근");
            System.out.println("3. 출퇴근 기록 확인");
            System.out.println("4. 근무지 관리");
            System.out.println("5. 로그아웃");
            System.out.println("=========================================");

            selectedMenu = br.readLine();
            switch (selectedMenu) {
                case "1":
                    // 근무지 선택 및 출근 처리
                    checkIn();
                    break;
                case "2":
                    // 근무지 선택 및 퇴근 처리
                    checkOut();
                    break;
                case "3":
                    // 모든 근무지 출퇴근 기록 불러오기
                    getAttendanceRecord();
                    break;
                case "4":
                    // 근무지 등록/삭제
                    manageStore();
                    break;
                case "5":
                    // 로그아웃
                    return;
                default:
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    private void checkIn() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=========================================");
        System.out.println("출근할 근무지 ID를 선택해주세요.");
        System.out.println("이전 화면으로 가려면 Q를 입력해주세요.");
        System.out.println("-----------------------------------------");
        List<Stores> stores = getStoreList(loginUser.getUser_id());
        if(stores.isEmpty()){
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("현재 근무 중인 곳이 없습니다.");
            System.out.println("메뉴로 돌아갑니다.");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            return;
        }else{
            printStoreList(stores);
        }
        String storeId = br.readLine();
        if(storeId.toLowerCase().equals("q")) return;


        // 매장 ID가 리스트에 있는지 확인
        boolean storeExists = stores.stream()
                .anyMatch(store -> store.getStore_id().equals(Long.parseLong(storeId)));

        if (storeExists) {
            // 출근 처리 로직 (예: DB에 출근 시간 저장 등) 추가
            insertCheckIn(Long.parseLong(storeId));
        } else {
            System.out.println("유효하지 않은 매장 ID입니다. 다시 선택해주세요.");
            checkIn(); // 재귀 호출로 다시 입력 받기
        }
    }

    private void checkOut() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=========================================");
        System.out.println("퇴근할 근무지 ID를 선택해주세요.");
        System.out.println("이전 화면으로 가려면 Q를 입력해주세요.");
        System.out.println("-----------------------------------------");

        // 근무 중인 매장 리스트를 가져오기
        List<Stores> stores = getStoreList(loginUser.getUser_id());

        if (stores.isEmpty()) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("현재 근무 중인 곳이 없습니다.");
            System.out.println("메뉴로 돌아갑니다.");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            return;
        } else {
            printStoreList(stores); // 근무 매장 목록 출력
        }

        String storeId = br.readLine(); // 퇴근할 매장 ID 입력
        if (storeId.toLowerCase().equals("q")) return;

        // 매장 ID가 리스트에 있는지 확인
        boolean storeExists = stores.stream()
                .anyMatch(store -> store.getStore_id().equals(Long.parseLong(storeId)));

        if (storeExists) {
            // 퇴근 처리 로직 (예: DB에 퇴근 시간 저장 등) 추가
            insertCheckOut(Long.parseLong(storeId));
        } else {
            System.out.println("유효하지 않은 매장 ID입니다. 다시 선택해주세요.");
            checkOut(); // 재귀 호출로 다시 입력 받기
        }
    }

    private void getAttendanceRecord(){
        System.out.println("=========================================");
        System.out.println("출퇴근 기록");
        System.out.println("-----------------------------------------");

        String getAttendanceQuery = "SELECT " +
                "    ar.date AS 출근날짜, " +
                "    ar.clockin AS 출근시간, " +
                "    ar.clockout AS 퇴근시간, " +
                "    s.store_name AS 근무지명 " +
                "FROM " +
                "    attendancerecords ar " +
                "JOIN " +
                "    employee e ON ar.employee_id = e.employee_id " +
                "JOIN " +
                "    stores s ON e.store_id = s.store_id " +
                "WHERE " +
                "    e.user_id = ? " +
                "ORDER BY " +
                "    ar.date DESC, ar.clockin DESC";

        try (Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(getAttendanceQuery)) {

            stmt.setLong(1, loginUser.getUser_id());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("출근날짜: " + rs.getDate("출근날짜"));
                System.out.println("출근시간: " + rs.getTime("출근시간"));
                System.out.println("퇴근시간: " + rs.getTime("퇴근시간"));
                System.out.println("근무지명: " + rs.getString("근무지명"));
                System.out.println("-----------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manageStore() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        List<Stores> storeList = getStoreList(loginUser.getUser_id());

        System.out.println("=========================================");
        System.out.println("근무지 목록");
        System.out.println("-----------------------------------------");
        printStoreList(storeList);
        System.out.println("1. 근무지 등록");
        System.out.println("2. 이전 메뉴로 돌아가기");
        System.out.println("=========================================");

        String input = br.readLine();

        switch (input){
            case "1":
                addStore();
                break;
            case "2":
                break;
            default:
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                manageStore();
        }
    }

    private void addStore() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String searchStoreName;
        List<Stores> foundStores = new ArrayList<>();
        System.out.println("=========================================");
        System.out.println("근무지 등록");
        System.out.println("-----------------------------------------");
        while(foundStores.isEmpty()) {
            System.out.print("매장명: ");
            searchStoreName = br.readLine();
            foundStores = searchStoreByName(searchStoreName);
        }
        while (true) {
            System.out.print("등록할 매장 ID: ");
            String inputStoreId = br.readLine();

            try {
                Long storeId = Long.parseLong(inputStoreId);

                // 입력된 매장 ID가 검색 결과에 존재하는지 확인
                boolean storeExists = foundStores.stream()
                        .anyMatch(store -> store.getStore_id().equals(storeId));

                if (storeExists) {
                    // 매장 등록 로직 호출
                    insertStore(storeId);
                    break;
                } else {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("선택하신 매장이 검색 결과에 없습니다. 다시 입력해주세요.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                }
            } catch (NumberFormatException e) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("유효하지 않은 매장 ID입니다. 숫자를 입력해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    private void insertStore(Long storeId) {
        String insertStoreQuery =
                "INSERT INTO employee (user_id, store_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertStoreQuery)) {

            // 매장 등록 쿼리 실행
            stmt.setLong(1, loginUser.getUser_id());  // 현재 로그인된 유저의 ID
            stmt.setLong(2, storeId);                // 등록할 매장의 ID
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("매장이 성공적으로 등록되었습니다.");
                System.out.println("=========================================");
            } else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("매장 등록에 실패했습니다. 다시 시도해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }

        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("매장 등록 중 오류 발생: " + e.getMessage());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        }
    }

    public List<Stores> searchStoreByName(String storeName) {
        String getStoreQuery =
                "SELECT store_id, store_name, address " +
                        "FROM stores " +
                        "WHERE store_name LIKE CONCAT('%', ?, '%')";

        List<Stores> storeList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(getStoreQuery)) {

            stmt.setString(1, storeName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Stores 객체 생성 및 데이터 설정
                Stores store = new Stores();
                store.setStore_id(rs.getLong("store_id"));
                store.setStore_name(rs.getString("store_name"));
                store.setAddress(rs.getString("address"));

                System.out.println("매장 ID: " + store.getStore_id());
                System.out.println("매장 이름: " + store.getStore_name());
                System.out.println("주소: " + store.getAddress());
                System.out.println("-----------------------------------------");

                // List에 추가
                storeList.add(store);
            }

            // 검색 결과가 없는 경우 메시지 출력
            if (storeList.isEmpty()) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("입력하신 매장명과 일치하는 매장이 없습니다.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return storeList; // 검색 결과 반환
    }

    private List<Stores> getStoreList(Long userId) {
        // 근무 매장 리스트 가져오기
        String getStoreList = "SELECT s.store_id, s.store_name, s.address , s.manager " +
                "FROM stores s " +
                "WHERE s.store_id IN (" +
                "    SELECT e.store_id " +
                "    FROM employee e " +
                "    WHERE e.user_id = ?" +
                ")";

        List<Stores> storeList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(getStoreList)) {

            stmt.setLong(1, userId); // userId로 employee_id를 찾기 위해 파라미터 설정
            ResultSet rs = stmt.executeQuery();

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
            e.printStackTrace();
        }

        return storeList; // 리스트 반환
    }

    private void insertCheckIn(Long storeId){
        String getEmployeeIdQuery = "SELECT e.employee_id FROM employee e WHERE e.user_id = ? and e.store_id = ?";
        String insertAttendanceQuery = "INSERT INTO attendancerecords (employee_id, date, clockin) VALUES (?, CURDATE(), NOW())";

        try (Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
             PreparedStatement getEmployeeStmt = conn.prepareStatement(getEmployeeIdQuery);
             PreparedStatement insertAttendanceStmt = conn.prepareStatement(insertAttendanceQuery)) {

            // user_id, store_id로 employee_id를 찾기 위한 쿼리 실행
            getEmployeeStmt.setLong(1, loginUser.getUser_id());
            getEmployeeStmt.setLong(2, storeId);
            ResultSet rs = getEmployeeStmt.executeQuery();

            if (rs.next()) {
                long employeeId = rs.getLong("employee_id"); // employee_id 가져오기

                // 출근 기록을 삽입하는 쿼리 실행
                insertAttendanceStmt.setLong(1, employeeId);  // employee_id
                int rowsAffected = insertAttendanceStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("출근 기록이 성공적으로 저장되었습니다.");
                    System.out.println("=========================================");
                } else {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("출근 기록 저장에 실패했습니다.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                }
            } else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("해당 user_id에 해당하는 직원이 없습니다.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }

        } catch (Exception e) {
            System.err.println("출근 기록 저장 중 오류 발생: " + e.getMessage());
        }
    }

    private void insertCheckOut(Long storeId) {
        String getEmployeeIdQuery = "SELECT e.employee_id FROM employee e WHERE e.user_id = ? AND e.store_id = ?";
        String updateAttendanceQuery = "UPDATE attendancerecords SET clockout = NOW() WHERE employee_id = ? AND date = CURDATE() AND clockout IS NULL ORDER BY clockin DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
             PreparedStatement getEmployeeStmt = conn.prepareStatement(getEmployeeIdQuery);
             PreparedStatement updateAttendanceStmt = conn.prepareStatement(updateAttendanceQuery)) {

            // user_id, store_id로 employee_id를 찾기 위한 쿼리 실행
            getEmployeeStmt.setLong(1, loginUser.getUser_id());
            getEmployeeStmt.setLong(2, storeId);
            ResultSet rs = getEmployeeStmt.executeQuery();

            if (rs.next()) {
                long employeeId = rs.getLong("employee_id"); // employee_id 가져오기

                // 퇴근 기록을 업데이트하는 쿼리 실행
                updateAttendanceStmt.setLong(1, employeeId);  // employee_id
                int rowsAffected = updateAttendanceStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("퇴근 기록이 성공적으로 저장되었습니다.");
                    System.out.println("=========================================");
                } else {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("퇴근 기록 저장에 실패했습니다.");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                }
            } else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("해당 user_id에 해당하는 직원이 없습니다.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }

        } catch (Exception e) {
            System.err.println("퇴근 기록 저장 중 오류 발생: " + e.getMessage());
        }
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
