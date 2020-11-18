package com.company;

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
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("interface.fxml"));
		Parent root = loader.load();

		com.company.Controller myController = loader.getController();

		primaryStage.setTitle("Student Management");
		primaryStage.setScene(new Scene(root, 1280, 780));
		primaryStage.show();

		myController.setData();
		myController.courses.getSelectionModel().selectFirst();
		myController.students.getSelectionModel().selectFirst();
		myController.grade.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
	}

	Connection conn = null;

	public static void main(String[] args) {
		launch(args);

	}



}
