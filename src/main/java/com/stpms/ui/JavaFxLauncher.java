package com.stpms.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class JavaFxLauncher extends Application {

    private com.stpms.Application backendApp;

    @Override
    public void start(Stage primaryStage) {
        try {
            backendApp = new com.stpms.Application();

            TaskManagementView taskManagementView = new TaskManagementView(backendApp);
            Scene scene = new Scene(taskManagementView.getView(), 900, 600);

            primaryStage.setTitle("STPMS - Smart Task & Productivity Management System");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            showDatabaseErrorAndExit();
        }
    }

    private void showDatabaseErrorAndExit() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("Cannot connect to MySQL");
        alert.setContentText("Please make sure MySQL server is running, then restart the application.");
        alert.showAndWait();

        System.exit(1);
    }

    @Override
    public void stop() {
        if (backendApp != null) {
            backendApp.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}