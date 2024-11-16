package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Join {
    private String id, password, checkpw, name, role;
    public void join() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=========================================");
        System.out.println("회원가입");
        System.out.println("이전 화면으로 가려면 Q를 입력해주세요.");
        System.out.println("-----------------------------------------");
        System.out.print("ID: ");
        id = br.readLine(); // username에 저장
        if(id.toLowerCase().equals("q"))
            return;
        // 중복확인 필요
        while(true) {
            System.out.print("PASSWORD: ");
            password = br.readLine();
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
        while(true) {
            System.out.print("ROLE(점장 1, 직원 2): ");
            role = br.readLine();
            if(role.equals("1") || role.equals("2"))
                break;
            else {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
                System.out.println("존재하지 않는 직급입니다. 다시 선택해주세요.");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            }
        }
        System.out.println("회원가입이 완료됐습니다.");
        System.out.println("=========================================");
    }
}
