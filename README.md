# DB_ESQL_Assignment_HLL

## 프로젝트 소개

> Java JDBC와 SQL을 활용한 직원 출퇴근 및 근무지 관리 시스템 구현

데이터베이스 설계 및 SQL 실습을 목적으로 진행한 프로젝트입니다.

Java에서 JDBC를 활용하여 데이터베이스와 연동하고,
직원 및 근무지 정보를 조회·등록하며 출퇴근 기록을 관리하는 기능을 구현했습니다.

## 주요 기능

### 사용자

- 회원가입
- 로그인
- 로그아웃

### 직원

- 근무지 조회
- 근무지 등록
- 출근 처리
- 퇴근 처리
- 출퇴근 기록 조회

### 관리자

- 직원 및 근무지 관리

## 기술 스택

### Language
- Java

### Database
- MySQL

### Database Connectivity
- JDBC

## 주요 구현

### 1. 사용자 인증

- 회원가입 및 로그인 기능 구현
- 사용자 정보를 데이터베이스와 연동하여 관리

### 2. 근무지 관리

- 근무지 검색
- 사용자의 근무지 등록
- 등록된 근무지 조회

### 3. 출퇴근 관리

- 근무지를 선택하여 출근 처리
- 출근 기록을 기반으로 퇴근 처리
- 출퇴근 날짜 및 시간 저장
- 사용자의 전체 출퇴근 기록 조회

### 4. SQL을 활용한 데이터 처리

JDBC의 `PreparedStatement`를 활용하여 데이터베이스에 SQL을 실행하고,
조회 결과를 Java 객체로 변환하여 프로그램에서 활용했습니다.

## 프로젝트 구조

```text
src
├── config
│   └── DBConfig
│
├── entity
│   ├── AttendanceRecords
│   ├── Employee
│   ├── Stores
│   └── Users
│
├── menu
│   ├── EmployeeMenu
│   ├── Join
│   ├── Login
│   ├── MainMenu
│   └── ManagerMenu
│
└── Main.java
