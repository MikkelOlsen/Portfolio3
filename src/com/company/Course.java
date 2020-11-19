package com.company;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class Course {
    Connection conn=null;
    Statement stmt;
    String sql = "";

    private String name = "";
    private String teacherName = "";
    private String semesterName = "";
    private Integer id;
    private Integer teacherId;
    private Integer semesterId;

    @Override
    public String toString() {
        return name + " - " + semesterName + ", Taught by: " + teacherName;
    }

    public Course() {

    }
    public Course(String name, Integer id, Integer teacherId, Integer semesterId, String teacherName, String semesterName) {
        this.name = name;
        this.id = id;
        this.teacherId = teacherId;
        this.semesterId = semesterId;
        this.teacherName = teacherName;
        this.semesterName = semesterName;
    }

    public String getName() {
        return name;
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

    public ResultSet getCourses() throws SQLException{
        sql = "SELECT Course.id, Course.name, Teacher.id, Teacher.name, Semester.id, Semester.name FROM 'Course' INNER JOIN Semester ON Course.semesterId = Semester.id INNER JOIN Teacher ON Course.teacherId = Teacher.id;";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs==null) {
            System.out.println("No records");
        }
        sql = "";
        return rs;

    }

    public ResultSet getSingleCourse(Integer id) throws SQLException {
        sql = "SELECT Student.name, Student.city, IFNULL(studentCourse.grade, 'Not given'), Course.name, Semester.name, Teacher.name, AVG(studentCourse.grade) OVER() AS avgGrade, studentCourse.studentId, studentCourse.courseId, course.semesterId\n" +
                "FROM studentCourse\n" +
                "    INNER JOIN Student ON studentCourse.studentId = Student.id\n" +
                "    INNER JOIN Course ON studentCourse.courseId = Course.id\n" +
                "    INNER JOIN Semester ON Course.semesterId = Semester.id\n" +
                "    INNER JOIN Teacher on Teacher.id = Course.teacherId\n" +
                "WHERE studentCourse.courseId = " + id;
        ResultSet rs = stmt.executeQuery(sql);
        if(rs==null) {
            System.out.println("No records");
        }
        sql = "";
        return rs;
    }

    public void createCourse(String name, Integer teacherId, Integer semesterId) throws SQLException {
        sql = "INSERT INTO 'Course' ('name', 'teacherId', 'semesterId') VALUES ('" + name + "', '"+ teacherId +"', '" + semesterId + "')";
        stmt.executeUpdate(sql);
        sql =  "";
        System.out.println("Inserted Course.");
    }

    public ResultSet getCoursesUpdate(Integer id) throws SQLException {
        sql = "SELECT Course.name, Course.id, Course.teacherId, Course.semesterId, Teacher.name, Semester.name\n" +
                "FROM 'Course'\n" +
                "    INNER JOIN Teacher\n" +
                "        ON Course.teacherId = Teacher.id\n" +
                "    INNER JOIN Semester\n" +
                "        ON Course.semesterId = Semester.id\n" +
                "WHERE course.id NOT IN (SELECT studentCourse.courseId FROM studentCourse WHERE studentId = " + id + ")";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs==null) {
            System.out.println("No records");
        }
        return rs;
    }


}
