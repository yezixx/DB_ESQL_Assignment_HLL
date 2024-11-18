package menu;

import entity.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            System.out.println("3. 근무지 관리");
            System.out.println("4. 로그아웃");
            System.out.println("=========================================");

            selectedMenu = br.readLine();
            switch (selectedMenu) {
                case "1":
                    // 출퇵근 기록 조회할 직원 선택
                    break;
                case "2":
                    // 출퇵근 기록 수정할 직원 선택
                    break;
                case "3":
                    // 운영 중인 매장 조회 및 근무지 등록/삭제
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
}
