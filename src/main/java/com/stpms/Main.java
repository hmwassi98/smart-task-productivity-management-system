package com.stpms;

public class Main {

    public static void main(String[] args) {
        Application app = null;

        try {
            app = new Application();

            System.out.println("STPMS application started successfully.");
            System.out.println("All repositories, services, and controllers have been initialized.");

            // UI will be plugged in here later

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