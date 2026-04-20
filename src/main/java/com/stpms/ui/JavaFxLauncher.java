package com.stpms.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxLauncher extends Application {

    private com.stpms.Application backendApp;

    @Override
    public void start(Stage primaryStage) throws Exception {
        backendApp = new com.stpms.Application();

        TaskManagementView taskManagementView = new TaskManagementView(backendApp);

        Scene scene = new Scene(taskManagementView.getView(), 900, 600);

        primaryStage.setTitle("STPMS - Smart Task & Productivity Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
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