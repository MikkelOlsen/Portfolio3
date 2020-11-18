package com.company;

public class studentCourse {
    private String studentName;
    private String studentCity;
    private String courseName;
    private String semesterName;
    private String grade;

    private Integer studentId;
    private Integer courseId;
    private Integer semesterId;

    public studentCourse(String studentName, String studentCity, String courseName, String semesterName, String grade, Integer studentId, Integer courseId, Integer semesterId) {
        this.studentName = studentName;
        this.studentCity = studentCity;
        this.courseName = courseName;
        this.semesterName = semesterName;
        this.grade = grade;
        this.studentId = studentId;
        this.courseId = courseId;
        this.semesterId = semesterId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCity() {
        return studentCity;
    }

    public void setStudentCity(String studentCity) {
        this.studentCity = studentCity;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
