import menu.Join;
import menu.Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String input;

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
}