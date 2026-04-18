package com.stpms;

import com.stpms.ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {
        Application app = null;

        try {
            app = new Application();

            System.out.println("STPMS application started successfully.");
            System.out.println("All repositories, services, and controllers have been initialized.");

            ConsoleUI consoleUI = new ConsoleUI(app);
            consoleUI.start();

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (app != null) {
                app.shutdown();
            }
        }
    }
}