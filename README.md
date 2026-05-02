# Smart Task & Productivity Management System (STPMS)

## Overview

The **Smart Task & Productivity Management System (STPMS)** is a Java-based application designed to help users efficiently manage tasks, track productivity, and improve focus using Pomodoro techniques.

This project demonstrates a **layered backend architecture** combined with a **JavaFX graphical user interface**, following industry-standard design principles such as separation of concerns and modularity.

---

## Features

### Task Management

* Create, update, delete tasks
* Assign priority and category
* Track task status:

  * To Do
  * In Progress
  * Completed
  * Backlog
* Automatic timestamping (`createdAt`, `completedAt`)

### Subtasks

* Add subtasks to tasks
* Track completion status independently
* Maintain parent-child relationship

### Pomodoro Timer

* Start focus sessions
* Record session duration
* Track productivity per task

### Data Persistence

* MySQL database integration
* Persistent storage for:

  * Tasks
  * Subtasks
  * Pomodoro sessions

### User Interface

* Built with JavaFX
* Interactive task management view
* Pomodoro timer interface

### Fail-Safe Mechanism

* Detects if the database is not running
* Prevents application crash
* Displays meaningful error message to the user

---

## Project Architecture

The application follows a **layered architecture**:

```
UI → Controller → Service → Repository → Database
```

### Package Structure

```
com.stpms
│
├── config        # Application configuration (DB connection)
├── controller    # Handles user input and coordinates actions
├── model         # Core entities (Task, Subtask, PomodoroSession)
├── repository    # Database interaction (JDBC)
├── service       # Business logic
├── ui            # JavaFX UI components
├── utility       # Helper classes
```

---

## Technologies Used

* **Java (JDK 17+)**
* **JavaFX**
* **MySQL**
* **JDBC**
* **JUnit** (for testing)
* **Maven** (build tool)

---

## Setup Instructions

### Clone the Repository

```bash
git clone https://github.com/your-username/stpms.git
cd stpms
```

---

### Setup Database

* Ensure MySQL is installed and running
* Create the database:

```sql
CREATE DATABASE task_manager;
```

* Run the schema file:

```bash
resources/schema.sql
```

---

### Configure Database Connection

Update your database credentials in the config class (if needed):

```java
// Example
url = "jdbc:mysql://localhost:3306/task_manager";
username = "root";
password = "your_password";
```

---

### Run the Application

Make sure MySQL is running before starting the app.

```bash
mvn clean install
mvn javafx:run
```

---

## Important Note

If the database is not running:

* The application will **not crash**
* A **fail-safe message** will notify the user to start the database

---

## Running Tests

```bash
mvn test
```

Tests cover:

* Repository layer
* Service layer

---

## Future Improvements

* User authentication system
* REST API integration
* Advanced analytics dashboard
* Cloud database support
* Notification/reminder system

---

## Learning Objectives

This project demonstrates:

* Layered architecture design
* Separation of concerns
* Database integration with JDBC
* GUI development with JavaFX
* Unit testing with JUnit

---

## License

This project is for academic and educational purposes and later may be expanded for commercial purposes.

---

## Author

**Azmine Fayeq Wassi**
BSc. in Computer Science
Faculty of Informatics
University of Debrecen
Debrecen, Hungary

---

## Acknowledgment

This project was developed as part of a thesis on **Java Application Development**, focusing on building a scalable and maintainable productivity system.
