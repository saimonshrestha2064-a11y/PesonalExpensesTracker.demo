package com.expensetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * MainApp – JavaFX Application Entry Point.
 *
 * Loads the FXML layout, sets up the primary Stage (window),
 * and starts the application.
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Load the FXML layout file
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/expensetracker/main.fxml")
        );
        Scene scene = new Scene(loader.load(), 1100, 700);

        // Apply the CSS stylesheet
        scene.getStylesheets().add(
            Objects.requireNonNull(
                getClass().getResource("/styles/styles.css")
            ).toExternalForm()
        );

        // Get the controller to hook up shutdown event
        MainController controller = loader.getController();
        primaryStage.setOnCloseRequest(e -> controller.shutdown());

        // Configure the window
        primaryStage.setTitle("Personnel Expenses Tracker – Saimon Shrestha");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    /**
     * Main entry point – required for some IDEs and Maven.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
