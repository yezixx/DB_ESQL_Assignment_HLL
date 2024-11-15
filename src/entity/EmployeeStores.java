package entity;

// store와 employee의 다대다 관계를 다대일, 일대다로 풀어내기 위한 중간 테이블
public class EmployeeStores {
    private Long employee_id; // fk
    private Long store_id; // fk

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }
}
