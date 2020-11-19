package com.company;

//Import all relevant libraries.
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
import java.util.concurrent.TimeUnit;

public class Controller {

    //Initialize all JavaFX elements
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

    /*
    This method fetches the information/students & grade for the overview tab from database based on the selected option in the dropdown.
    After this is makes a table in the visual interface by using JavaFX.
    Lastly it loops through all the courses that the student has attended and makes a table row for each of them, displaying the relevant information

    The average grade of the student is calculated in the SQL query itself.
     */
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

    /*
    This method fetches the information/students & grade for the overview tab from database based on the selected option in the dropdown.
    After this is makes a table in the visual interface by using JavaFX.
    Lastly it loops through all the students that are assigned to the course and makes a table row for each of them displaying the relevant information.

    The average grade of the course is calculated in the SQL query itself.
     */
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

    /*
    This method adds an entry to the studentCourse table, which means that a student is now registered for the given course.
    It checks whether or not the user has filled in a grade or not in the createStudentCourse method, if they haven't a null value will be put into the grade column instead of an integer.
     */
    public void addStudentToCourse(ActionEvent actionEvent) throws SQLException {
        try {
            SH.Connect(url);
            SH.createStatement();
            SH.createStudentCourse(grade.getText(), studentsList.getValue().getId(), coursesList.getValue().getId());
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

    //Method for inserting a student into the database
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

    // Method for inserting a course into the database
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

    //Method for setting data in all dropdowns
    public void setData() throws SQLException {
        try {
            //Establish connections from the different classes to the database
            SH.Connect(url);
            CR.Connect(url);
            SEM.Connect(url);
            TH.Connect(url);

            //Create the statements for all classes
            SH.createStatement();
            CR.createStatement();
            SEM.createStatement();
            TH.createStatement();

            //Clear the dropdowns of old information (needed for when the method is called upon insertion of new data)
            students.getItems().clear();
            studentsList.getItems().clear();
            studentGradeList.getItems().clear();
            courses.getItems().clear();
            coursesList.getItems().clear();
            courseGradeList.getItems().clear();
            semesterList.getItems().clear();
            teacherList.getItems().clear();


            /*
            The functionalities here repeat.
            Get all the relevant data from the class relating to the table in the database.
            Then create an observable list of class objects to contain this data (used for showing the name of the data row on the frontend
            and accessing the ID in the backend.
            Then initialize and add the new class objects to the observable list.
            Prepend the information to the relevant dropdown menus.
             */
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
                CRData.addAll(new Course(CRrs.getString(2), CRrs.getInt(1), CRrs.getInt(3), CRrs.getInt(5), CRrs.getString(4), CRrs.getString(6)));

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

    //Method for inserting a semester into the database.
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

    //Method for inserting a teacher into the database
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

    //Method for updating an existing student's grade on a course they are already on.
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

    /*
    This method is used for filtering the course dropdown menus when a student is selected (add to course and edit grade).
    It does so by first checking the id of the dropdown that were used.
    Depending on which dropdown it was, it runs a method that either gets all the courses that a student is or isn't already tied to.
    The purpose is that the user should not be able to edit the grade for a student on a course they did not attend, and likewise
    should not be able to add a student to a course that they are already on.
     */
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

    /*
    This method is used when the user selects a course on the edit grade page.
    It fetches the selected student grade for the selected course.
    This is set to show "Not given" if the grade is null in the database via the SQL query.
     */
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
