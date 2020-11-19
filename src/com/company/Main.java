package com.company;

//Import the necessary libraries

import java.sql.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import static java.sql.DriverManager.getConnection;


public class Main extends Application {
	@Override
	//Start method for Java FX visual interface
	public void start(Stage primaryStage) throws Exception {
		//Load the JavaFX file
		FXMLLoader loader = new FXMLLoader(getClass().getResource("interface.fxml"));
		Parent root = loader.load();

		//Bind the controller of the visual interface to a variable
		com.company.Controller myController = loader.getController();

		//Set the title and size of the interface, then show it
		primaryStage.setTitle("Student Management");
		primaryStage.setScene(new Scene(root, 1280, 780));
		primaryStage.show();

		//Run the setData method located in the controller.
		myController.setData();

		//Set the first index of the dropdown menus in "Overview" to selected.
		myController.courses.getSelectionModel().selectFirst();
		myController.students.getSelectionModel().selectFirst();

		//Set the grade fields to
		myController.grade.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
		myController.courseGradeEdit.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
	}

	Connection conn = null;

	public static void main(String[] args) {
		//Launch the visual interface
		launch(args);

	}



}
