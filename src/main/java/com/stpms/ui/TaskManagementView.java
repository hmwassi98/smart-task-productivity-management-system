package com.stpms.ui;

import com.stpms.Application;
import com.stpms.controller.TaskController;
import com.stpms.controller.SubtaskController;
import com.stpms.controller.PomodoroSessionController;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.model.Subtask;
import com.stpms.model.PomodoroSession;
import com.stpms.model.PomodoroType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    private final SubtaskController subtaskController;
    private final PomodoroSessionController pomodoroSessionController;

    private final BorderPane root;
    private final TableView<Task> taskTable;

    private final TextField nameField;
    private final TextArea descriptionArea;
    private final ComboBox<TaskPriority> priorityBox;
    private final ComboBox<TaskCategory> categoryBox;
    private final DatePicker deadlinePicker;

    private TableView<Subtask> subtaskTable;
    private TextField subtaskNameField;

    private TableView<PomodoroSession> sessionTable;
    private ComboBox<PomodoroType> pomodoroTypeBox;
    private TextField plannedMinutesField;

    public TaskManagementView(Application app) {
        this.taskController = app.getTaskController();
        this.subtaskController = app.getSubtaskController();
        this.pomodoroSessionController = app.getPomodoroSessionController();

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
        Label titleLabel = new Label("STPMS - Smart Task & Productivity Management System");
        titleLabel.setFont(Font.font(22));

        VBox topBox = new VBox(titleLabel);
        topBox.setPadding(new Insets(15));
        root.setTop(topBox);

        buildTable();

        VBox taskFormBox = buildForm();

        ScrollPane taskFormScrollPane = new ScrollPane(taskFormBox);
        taskFormScrollPane.setFitToWidth(true);

        SplitPane taskSplitPane = new SplitPane();
        taskSplitPane.getItems().addAll(taskTable, taskFormScrollPane);
        taskSplitPane.setDividerPositions(0.6);

        TabPane tabPane = new TabPane();

        Tab taskTab = new Tab("Tasks", taskSplitPane);
        taskTab.setClosable(false);

        Tab subtaskTab = new Tab("Subtasks", buildSubtaskTab());
        subtaskTab.setClosable(false);

        Tab pomodoroTab = new Tab("Pomodoro", buildPomodoroTab());
        pomodoroTab.setClosable(false);

        tabPane.getTabs().addAll(taskTab, subtaskTab, pomodoroTab);

        root.setCenter(tabPane);
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

    private Parent buildSubtaskTab() {
        subtaskTable = new TableView<>();

        TableColumn<Subtask, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSubtaskId()));

        TableColumn<Subtask, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Subtask, Boolean> completedColumn = new TableColumn<>("Completed");
        completedColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().isCompleted()));

        subtaskTable.getColumns().addAll(idColumn, nameColumn, completedColumn);
        subtaskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        subtaskNameField = new TextField();
        subtaskNameField.setPromptText("Subtask name");

        Button loadButton = new Button("Load Subtasks for Selected Task");
        Button addButton = new Button("Add Subtask");
        Button completeButton = new Button("Complete Selected Subtask");
        Button reopenButton = new Button("Reopen Selected Subtask");
        Button deleteButton = new Button("Delete Selected Subtask");

        loadButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setMaxWidth(Double.MAX_VALUE);
        completeButton.setMaxWidth(Double.MAX_VALUE);
        reopenButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setMaxWidth(Double.MAX_VALUE);

        loadButton.setOnAction(e -> loadSubtasksForSelectedTask());
        addButton.setOnAction(e -> addSubtaskToSelectedTask());
        completeButton.setOnAction(e -> completeSelectedSubtask());
        reopenButton.setOnAction(e -> reopenSelectedSubtask());
        deleteButton.setOnAction(e -> deleteSelectedSubtask());

        VBox controls = new VBox(10,
                new Label("Subtasks for Selected Task"),
                subtaskNameField,
                addButton,
                loadButton,
                completeButton,
                reopenButton,
                deleteButton
        );

        controls.setPadding(new Insets(15));
        controls.setPrefWidth(300);

        BorderPane pane = new BorderPane();
        pane.setCenter(subtaskTable);
        pane.setRight(controls);

        return pane;
    }

    private Parent buildPomodoroTab() {
        return null;
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
        refreshButton.setOnAction(e -> {
            loadTasks();
            showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Tasks updated from database.");
        });
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

            taskTable.getItems().clear();
            taskTable.setItems(FXCollections.observableArrayList(tasks));
            taskTable.refresh();

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
            Task updatedTask = taskController.startTask(selectedTask.getTaskId());
            loadTasks();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Task started. New status: " + updatedTask.getStatus());

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
            Task updatedTask = taskController.completeTask(selectedTask.getTaskId());
            loadTasks();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Task completed. New status: " + updatedTask.getStatus());

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
            Task updatedTask = taskController.reopenTask(selectedTask.getTaskId());
            loadTasks();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Task reopened. New status: " + updatedTask.getStatus());

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

    private void loadSubtasksForSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Task Selected", "Please select a task from the Tasks tab first.");
            return;
        }

        try {
            List<Subtask> subtasks = subtaskController.getSubtasksByTaskId(selectedTask.getTaskId());
            subtaskTable.setItems(FXCollections.observableArrayList(subtasks));
            subtaskTable.refresh();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load subtasks: " + ex.getMessage());
        }
    }

    private void addSubtaskToSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Task Selected", "Please select a task first.");
            return;
        }

        String name = subtaskNameField.getText().trim();

        if (name.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Subtask name cannot be empty.");
            return;
        }

        try {
            subtaskController.createSubtask(selectedTask.getTaskId(), name);
            subtaskNameField.clear();
            loadSubtasksForSelectedTask();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add subtask: " + ex.getMessage());
        }
    }

    private void completeSelectedSubtask() {
        Subtask selectedSubtask = subtaskTable.getSelectionModel().getSelectedItem();

        if (selectedSubtask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a subtask first.");
            return;
        }

        try {
            subtaskController.completeSubtask(selectedSubtask.getSubtaskId());
            loadSubtasksForSelectedTask();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to complete subtask: " + ex.getMessage());
        }
    }

    private void reopenSelectedSubtask() {
        Subtask selectedSubtask = subtaskTable.getSelectionModel().getSelectedItem();

        if (selectedSubtask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a subtask first.");
            return;
        }

        try {
            subtaskController.reopenSubtask(selectedSubtask.getSubtaskId());
            loadSubtasksForSelectedTask();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to reopen subtask: " + ex.getMessage());
        }
    }

    private void deleteSelectedSubtask() {
        Subtask selectedSubtask = subtaskTable.getSelectionModel().getSelectedItem();

        if (selectedSubtask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a subtask first.");
            return;
        }

        try {
            subtaskController.deleteSubtask(selectedSubtask.getSubtaskId());
            loadSubtasksForSelectedTask();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete subtask: " + ex.getMessage());
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