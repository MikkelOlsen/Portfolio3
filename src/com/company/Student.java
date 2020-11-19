package com.company;

import javafx.scene.control.TextField;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class Student {
    Connection conn=null;
    Statement stmt;
    String sql = "";

    private String name = "";
    private String city = "";
    private Integer id;

    @Override
    public String toString() {
        return name;
    }

    public Student() {

    }
    public Student(String name, String city, Integer id) {
        this.name = name;
        this.city = city;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Integer getId() {
        return id;
    }



    public void createStatement() throws  SQLException{
        stmt = conn.createStatement();
    }
    public void Connect(String url) throws SQLException {
        conn=getConnection(url);
    }
    public void CloseConnection() throws SQLException{
        conn.close();
    }

    public void init(String url) throws SQLException {
        try{
            Connect(url);
            createStatement();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createStudent(String city, String name) throws SQLException {
            sql = "INSERT INTO 'Student' ('name', 'city') VALUES ('" + name + "', '" + city + "')";
            stmt.executeUpdate(sql);
            sql = "";
            System.out.println("Inserted Student");
    }

    public void createStudentCourse(String grade, Integer studentId, Integer courseId) throws SQLException {
            if(grade.isEmpty()){grade = "NULL";}
            sql = "INSERT INTO 'studentCourse' ('grade', 'studentId', 'courseId') VALUES (" + grade + ", '" + studentId + "', '" + courseId +"')";
            stmt.executeUpdate(sql);
            sql = "";
            System.out.println("Inserted student");
    }


    public ResultSet getSingleStudent(Integer id) throws SQLException {
            sql = "SELECT Student.name, Student.city, ifnull(studentCourse.grade, 'Not given'), Course.name, Semester.name, Teacher.name, AVG(studentCourse.grade) OVER() AS avgGrade, studentCourse.studentId, studentCourse.courseId, course.semesterId, course.teacherId\n" +
                    "FROM studentCourse\n" +
                    "    INNER JOIN Student ON studentCourse.studentId = Student.id\n" +
                    "    INNER JOIN Course ON studentCourse.courseId = Course.id\n" +
                    "    INNER JOIN Semester ON Course.semesterId = Semester.id\n" +
                    "    INNER JOIN Teacher on Teacher.id = Course.teacherId\n" +
                    "WHERE studentCourse.studentId = " + id;

            ResultSet rs = stmt.executeQuery(sql);
            if (rs == null) {
                System.out.println("No records");
            }
            sql = "";
            return rs;

    }

    public ResultSet getStudents() throws SQLException{
            sql = "SELECT * FROM Student";
        Connect("jdbc:sqlite:student.sqlite");
        createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs == null) {
                System.out.println("No records");
            }
            sql = "";
            return rs;

    }

    public ResultSet getSingleGrade(Integer studentId, Integer courseId) throws SQLException {
        sql = "SELECT ifnull(grade, 'Not given')\n" +
                "FROM studentCourse\n" +
                "WHERE studentId = " + studentId + " AND courseId = " + courseId;
        ResultSet rs = stmt.executeQuery(sql);
        if(rs==null) {
            System.out.println("No records");
        }
        sql = "";
        return rs;
    }

    public void updateGrade(String grade, Integer studentId, Integer courseId) throws SQLException {
        if(grade.isEmpty() || grade.equalsIgnoreCase("not given")) {
            grade = null;
        }
        sql = "UPDATE studentCourse SET grade = " + grade + " WHERE studentId = " + studentId + " AND courseId = " + courseId;
        stmt.executeUpdate(sql);
        sql = "";
        System.out.println("Updated Grade");
    }

}
