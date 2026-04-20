package com.stpms.ui;

import com.stpms.Application;
import com.stpms.controller.TaskController;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.util.List;

public class TaskManagementView {

    private final TaskController taskController;

    private final BorderPane root;
    private final TableView<Task> taskTable;

    private final TextField nameField;
    private final TextArea descriptionArea;
    private final ComboBox<TaskPriority> priorityBox;
    private final ComboBox<TaskCategory> categoryBox;
    private final DatePicker deadlinePicker;

    public TaskManagementView(Application app) {
        this.taskController = app.getTaskController();

        root = new BorderPane();
        taskTable = new TableView<>();

        nameField = new TextField();
        descriptionArea = new TextArea();
        priorityBox = new ComboBox<>();
        categoryBox = new ComboBox<>();
        deadlinePicker = new DatePicker();

        buildUI();
        loadTasks();
    }

    public Parent getView() {
        return root;
    }

    private void buildUI() {
        Label titleLabel = new Label("STPMS - Task Management");
        titleLabel.setFont(Font.font(22));

        VBox topBox = new VBox(titleLabel);
        topBox.setPadding(new Insets(15));
        root.setTop(topBox);

        buildTable();

        VBox formBox = buildForm();

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(taskTable, formBox);
        splitPane.setDividerPositions(0.6);

        root.setCenter(splitPane);
    }

    private void buildTable() {
        TableColumn<Task, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTaskId()));

        TableColumn<Task, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Task, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().name()));

        TableColumn<Task, String> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPriority().name()));

        TableColumn<Task, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory() != null
                                ? cellData.getValue().getCategory().name()
                                : ""
                ));

        TableColumn<Task, String> deadlineColumn = new TableColumn<>("Deadline");
        deadlineColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDeadline() != null
                                ? cellData.getValue().getDeadline().toString()
                                : ""
                ));

        taskTable.getColumns().addAll(idColumn, nameColumn, statusColumn, priorityColumn, categoryColumn, deadlineColumn);
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private VBox buildForm() {
        Label formTitle = new Label("Create / Manage Task");
        formTitle.setFont(Font.font(18));

        nameField.setPromptText("Task name");

        descriptionArea.setPromptText("Task description");
        descriptionArea.setPrefRowCount(4);

        priorityBox.setItems(FXCollections.observableArrayList(TaskPriority.values()));
        priorityBox.setValue(TaskPriority.MEDIUM);

        categoryBox.setItems(FXCollections.observableArrayList(TaskCategory.values()));
        categoryBox.setValue(TaskCategory.OTHERS);

        deadlinePicker.setValue(LocalDate.now().plusDays(1));

        Button createButton = new Button("Create Task");
        Button refreshButton = new Button("Refresh Tasks");
        Button startButton = new Button("Start Selected");
        Button completeButton = new Button("Complete Selected");
        Button reopenButton = new Button("Reopen Selected");
        Button deleteButton = new Button("Delete Selected");

        createButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setMaxWidth(Double.MAX_VALUE);
        completeButton.setMaxWidth(Double.MAX_VALUE);
        reopenButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setMaxWidth(Double.MAX_VALUE);

        createButton.setOnAction(e -> createTask());
        refreshButton.setOnAction(e -> loadTasks());
        startButton.setOnAction(e -> startSelectedTask());
        completeButton.setOnAction(e -> completeSelectedTask());
        reopenButton.setOnAction(e -> reopenSelectedTask());
        deleteButton.setOnAction(e -> deleteSelectedTask());

        VBox formBox = new VBox(10,
                formTitle,
                new Label("Name"), nameField,
                new Label("Description"), descriptionArea,
                new Label("Priority"), priorityBox,
                new Label("Category"), categoryBox,
                new Label("Deadline"), deadlinePicker,
                createButton,
                refreshButton,
                startButton,
                completeButton,
                reopenButton,
                deleteButton
        );

        formBox.setPadding(new Insets(15));
        return formBox;
    }

    private void createTask() {
        try {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            TaskPriority priority = priorityBox.getValue();
            TaskCategory category = categoryBox.getValue();
            LocalDate deadline = deadlinePicker.getValue();

            if (name.isBlank()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Task name cannot be empty.");
                return;
            }

            Task createdTask = taskController.createTask(name, description, priority, category, deadline);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Task created successfully. ID: " + createdTask.getTaskId());

            clearForm();
            loadTasks();

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create task: " + ex.getMessage());
        }
    }

    private void loadTasks() {
        try {
            List<Task> tasks = taskController.getAllTasks();
            taskTable.setItems(FXCollections.observableArrayList(tasks));
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load tasks: " + ex.getMessage());
        }
    }

    private void startSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task first.");
            return;
        }

        try {
            taskController.startTask(selectedTask.getTaskId());
            loadTasks();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to start task: " + ex.getMessage());
        }
    }

    private void completeSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task first.");
            return;
        }

        try {
            taskController.completeTask(selectedTask.getTaskId());
            loadTasks();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to complete task: " + ex.getMessage());
        }
    }

    private void reopenSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task first.");
            return;
        }

        try {
            taskController.reopenTask(selectedTask.getTaskId());
            loadTasks();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to reopen task: " + ex.getMessage());
        }
    }

    private void deleteSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task first.");
            return;
        }

        try {
            boolean deleted = taskController.deleteTask(selectedTask.getTaskId());
            if (deleted) {
                loadTasks();
            }
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete task: " + ex.getMessage());
        }
    }

    private void clearForm() {
        nameField.clear();
        descriptionArea.clear();
        priorityBox.setValue(TaskPriority.MEDIUM);
        categoryBox.setValue(TaskCategory.OTHERS);
        deadlinePicker.setValue(LocalDate.now().plusDays(1));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}