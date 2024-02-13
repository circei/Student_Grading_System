package com.example.university_management_app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a label
        Label label = new Label("Hello, JavaFX!");

        // Create a root node (a StackPane) and add the label to it
        StackPane root = new StackPane(label);

        // Create a Scene with the root node
        Scene scene = new Scene(root, 300, 200);

        // Set the scene for the stage (primary stage)
        primaryStage.setScene(scene);

        // Set the title of the stage
        primaryStage.setTitle("Hello JavaFX");

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
