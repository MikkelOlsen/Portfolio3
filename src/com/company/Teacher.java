package com.company;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class Teacher {
    Connection conn=null;
    Statement stmt;
    String sql = "";

    private String name = "";
    private Integer id;

    @Override
    public String toString() {
        return name;
    }

    public Teacher() {

    }
    public Teacher(String name, Integer id) {
        this.name = name;
        this.id = id;
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

    public ResultSet getTeachers() throws SQLException {
        sql = "SELECT * FROM Teacher;";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs==null) {
            System.out.println("No records");
        }
        sql = "";
        return rs;
    }


    public void createTeacher(String name) throws SQLException {
        sql = "INSERT INTO 'Teacher' ('name') VALUES ('" + name + "')";
        stmt.executeUpdate(sql);
        sql = "";
        System.out.println("Inserted Teacher.");
    }

}
