package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    public Button getStudent;
    public Button getCourse;

    public ComboBox<Student> students, studentsList, studentGradeList;
    public ComboBox<Course> courses, coursesList, courseGradeList;
    public ComboBox<Teacher> teacherList;
    public ComboBox<Semester> semesterList;

    public TableView<studentCourse> studentTable;
    public TableColumn<studentCourse, String> courseName;
    public TableColumn<studentCourse, String> semesterName;
    public TableColumn<studentCourse, String> studentGrade;

    public Text studentCourseName;
    public Text total;

    public TextField teacherName;
    public TextField semesterNameField;
    public TextField studentName;
    public TextField studentCity;
    public TextField createCourseName;
    public TextField grade;
    public TextField courseGradeEdit;


    Semester SEM = new Semester();
    Teacher TH = new Teacher();
    Course CR = new Course();
    Student SH = new Student();

    String url = "jdbc:sqlite:student.sqlite";

    public void fetchStudent(ActionEvent actionEvent) throws SQLException {
        try {
            SH.Connect(url);
            SH.createStatement();
            if(students.getValue() != null) {
                studentTable.getItems().clear();
                studentTable.getColumns().clear();
                courseName = new TableColumn<>("Course");
                courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));

                semesterName = new TableColumn<>("Semester");
                semesterName.setCellValueFactory(new PropertyValueFactory<>("semesterName"));

                studentGrade = new TableColumn<>("Grade");
                studentGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

                studentTable.getColumns().addAll(courseName, semesterName, studentGrade);
                ResultSet student = SH.getSingleStudent(students.getValue().getId());

                studentCourseName.setText("Student selected - " + student.getString(1));
                total.setText("The student's average is: " + student.getString(7));
                while (student != null && student.next()) {
                    studentTable.getItems().add(new studentCourse(student.getString(1), student.getString(2), student.getString(4), student.getString(5), student.getString(3), student.getInt(8), student.getInt(9), student.getInt(10)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(SH.conn != null) {
                try {
                    SH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void fetchCourse(ActionEvent actionEvent) throws SQLException {
        try {
            CR.Connect(url);
            CR.createStatement();
            if(courses.getValue() != null) {
                studentTable.getItems().clear();
                studentTable.getColumns().clear();
                courseName = new TableColumn<>("Student name");
                courseName.setCellValueFactory(new PropertyValueFactory<>("studentName"));

                semesterName = new TableColumn<>("City");
                semesterName.setCellValueFactory(new PropertyValueFactory<>("studentCity"));

                studentGrade = new TableColumn<>("Grade");
                studentGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

                studentTable.getColumns().addAll(courseName, semesterName, studentGrade);
                ResultSet course = CR.getSingleCourse(courses.getValue().getId());

                if(course != null) {
                    studentCourseName.setText("Selected course - " + course.getString(4) + " (" + course.getString(5) + ")" + " taught by: " + course.getString(6));

                    if (course.getString(7) != null) {
                        total.setText("The average grade for the course is: " + course.getString(7));
                    } else {
                        total.setText("The grades for this course hasn't been given yet.");
                    }
                    while (course != null && course.next()) {
                        studentTable.getItems().add(new studentCourse(course.getString(1), course.getString(2), course.getString(4), course.getString(5), course.getString(3), course.getInt(8), course.getInt(9), course.getInt(10)));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(CR.conn != null) {
                try {
                    CR.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void addStudentToCourse(ActionEvent actionEvent) throws SQLException {
        try {
            SH.Connect(url);
            SH.createStatement();
            if(!grade.getText().isEmpty()) {
                SH.createStudentCourse(grade.getText(), studentsList.getValue().getId(), coursesList.getValue().getId());
            } else {
                SH.createStudentCourseNoGrade(studentsList.getValue().getId(), coursesList.getValue().getId());
            }
            setData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(SH.conn != null) {
                try {
                    SH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createStudent(ActionEvent actionEvent) throws SQLException {

        try {
            SH.Connect(url);
            SH.createStatement();
            SH.createStudent(studentCity.getText(), studentName.getText());
            studentCity.setText("");
            studentName.setText("");
            setData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(SH.conn != null) {
                try {
                    SH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createCourse(ActionEvent actionEvent) throws SQLException {

        try {
            CR.Connect(url);
            CR.createStatement();
            CR.createCourse(createCourseName.getText(), teacherList.getValue().getId(), semesterList.getValue().getId());
            createCourseName.setText("");
            setData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(CR.conn != null) {
                try {
                    CR.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setData() throws SQLException {
        try {
            SH.Connect(url);
            CR.Connect(url);
            SEM.Connect(url);
            TH.Connect(url);

            SH.createStatement();
            CR.createStatement();
            SEM.createStatement();
            TH.createStatement();


            students.getItems().clear();
            studentsList.getItems().clear();
            studentGradeList.getItems().clear();
            courses.getItems().clear();
            coursesList.getItems().clear();
            courseGradeList.getItems().clear();
            semesterList.getItems().clear();
            teacherList.getItems().clear();


            ResultSet SHrs = SH.getStudents();
            ObservableList<Student> SHData = FXCollections.observableArrayList();
            while (SHrs != null && SHrs.next()) {
                SHData.addAll(new Student(SHrs.getString(3), SHrs.getString(2), SHrs.getInt(1)));
            }
            students.getItems().addAll(SHData);
            studentsList.getItems().addAll(SHData);
            studentGradeList.getItems().addAll(SHData);

            ResultSet CRrs = CR.getCourses();
            ObservableList<Course> CRData = FXCollections.observableArrayList();
            while (CRrs != null && CRrs.next()) {
                CRData.addAll(new Course(CRrs.getString(2), CRrs.getInt(1), CRrs.getInt(3), CRrs.getInt(4), CRrs.getString(8), CRrs.getString(6)));

            }
            courses.getItems().addAll(CRData);
            coursesList.getItems().addAll(CRData);
            courseGradeList.getItems().addAll(CRData);

            ResultSet SEMrs = SEM.getSemesters();
            ObservableList<Semester> SEMData = FXCollections.observableArrayList();
            while(SEMrs != null && SEMrs.next()) {
                SEMData.addAll(new Semester(SEMrs.getString(2), SEMrs.getInt(1)));
            }
            semesterList.getItems().addAll(SEMData);

            ResultSet THrs = TH.getTeachers();
            ObservableList<Teacher> THData = FXCollections.observableArrayList();
            while(THrs != null && THrs.next()) {
                THData.addAll(new Teacher(THrs.getString(2), THrs.getInt(1)));
            }
            teacherList.getItems().addAll(THData);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(SH.conn != null && CR.conn != null && SEM.conn != null && TH.conn !=null) {
                try {
                    SH.CloseConnection();
                    CR.CloseConnection();
                    SEM.CloseConnection();
                    TH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void createSemester(ActionEvent actionEvent) throws SQLException {

        try {
            SEM.Connect(url);
            SEM.createStatement();
            SEM.createSemester(semesterNameField.getText());
            semesterNameField.setText("");
            setData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(SEM.conn != null) {
                try {
                    SEM.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createTeacher(ActionEvent actionEvent) throws SQLException {

        try {
            TH.Connect(url);
            TH.createStatement();
            TH.createTeacher(teacherName.getText());
            teacherName.setText("");
            setData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(TH.conn != null) {
                try {
                    TH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void editStudentGrade(ActionEvent actionEvent) throws SQLException {

        try {
            SH.Connect(url);
            SH.createStatement();
            SH.updateGrade(courseGradeEdit.getText(), studentGradeList.getValue().getId(), courseGradeList.getValue().getId());
            setData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(SH.conn != null) {
                try {
                    SH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateCourse(ActionEvent actionEvent) throws SQLException {
        try {
            CR.Connect(url);
            SH.Connect(url);

            CR.createStatement();
            SH.createStatement();

            final Node source = (Node) actionEvent.getSource();
            String id = source.getId();
            ResultSet CRrs = null;
            ObservableList<Course> CRData = FXCollections.observableArrayList();
            if(id.equals("studentsList") && studentsList.getValue() != null) {
                CRrs = CR.getCoursesUpdate(studentsList.getValue().getId());
                while (CRrs != null && CRrs.next()) {
                    CRData.addAll(new Course(CRrs.getString(1), CRrs.getInt(2), CRrs.getInt(3), CRrs.getInt(4), CRrs.getString(5), CRrs.getString(6)));
                }
                coursesList.getItems().clear();
                coursesList.getItems().addAll(CRData);
            } else if(id.equals("studentGradeList") && studentGradeList.getValue() != null) {
                CRrs = SH.getSingleStudent(studentGradeList.getValue().getId());
                while (CRrs != null && CRrs.next()) {
                    CRData.addAll(new Course(CRrs.getString(4), CRrs.getInt(9), CRrs.getInt(11), CRrs.getInt(10), CRrs.getString(6), CRrs.getString(5)));
                }
                courseGradeList.getItems().clear();
                courseGradeList.getItems().addAll(CRData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(CR.conn != null && SH.conn != null) {
                try {
                    CR.CloseConnection();
                    SH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    public void getStudentGrade(ActionEvent actionEvent) throws SQLException{
        try {
            SH.Connect(url);
            SH.createStatement();

            if(studentGradeList.getValue() != null && courseGradeList.getValue() != null) {
                ResultSet studentGrade = SH.getSingleGrade(studentGradeList.getValue().getId(), courseGradeList.getValue().getId());
                courseGradeEdit.setText(studentGrade.getString(1));
            } else {
                courseGradeEdit.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(SH.conn != null) {
                try {
                    SH.CloseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
