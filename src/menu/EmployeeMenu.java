package menu;

import entity.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                    break;
                case "2":
                    // 근무지 선택 및 퇴근 처리
                    break;
                case "3":
                    // 모든 근무지 출퇴근 기록 불러오기
                    break;
                case "4":
                    // 근무 중인 근무지 조회 및 근무지 등록/삭제
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
}
