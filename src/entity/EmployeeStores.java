package entity;

// store와 employee의 다대다 관계를 다대일, 일대다로 풀어내기 위한 중간 테이블
public class EmployeeStores {
    private Long employee_id; // fk
    private Long store_id; // fk

}
