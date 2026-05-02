# Student Search Management System

A full-stack web application built with **Spring Boot 4.0.3**, **Spring MVC**, and **Thymeleaf** that demonstrates server-side rendered CRUD operations for managing student records. The application supports both **Soft Delete** and **Hard Delete**, student search by multiple fields, form validation, and a custom exception handling page — all rendered directly in the browser without any separate frontend framework.

---

## Live Preview

| Page | URL |
|---|---|
| All Students | `http://localhost:8080/mvctask/students` |
| Add Student | `http://localhost:8080/mvctask/addstudent` |
| Active Students | `http://localhost:8080/mvctask/activestudents` |
| Inactive Students | `http://localhost:8080/mvctask/inactivestudents` |
| Find Students | `http://localhost:8080/mvctask/find` |

---

## Architecture

```
Browser (HTML Form / Link Click)
          |
          ▼
   StudentController (@Controller)
          |
          ▼
   StudentService (Business Logic)
     |           |
     ▼           ▼
StudentRepository   Soft/Hard Delete Logic
  (Spring Data JPA)
          |
          ▼
       MySQL DB
          |
          ▼
   Thymeleaf Template (.html)
          |
          ▼
   Browser renders the page
```

> Unlike REST APIs where the server returns JSON, in Spring MVC the server
> processes the request, adds data to the Model, and returns a fully rendered
> HTML page via Thymeleaf directly viewable in the browser.

---

## Features

### CRUD Operations
| Operation | Type | URL | Description |
|---|---|---|---|
| Read | GET | `/students` | View all students |
| Read | GET | `/activestudents` | View only ACTIVE students |
| Read | GET | `/inactivestudents` | View only INACTIVE students |
| Create | GET | `/addstudent` | Show add student form |
| Create | POST | `/savestudent` | Save new student |
| Update | GET | `/editstudent/{id}` | Show pre-filled edit form |
| Update | POST | `/updatestudent/{id}` | Save updated student |
| Soft Delete | GET | `/deletestudent/{id}` | Set status = INACTIVE (record stays in DB) |
| Hard Delete | GET | `/deletestudent1/{id}` | Permanently remove from DB |
| Restore | GET | `/setstudentactive/{id}` | Set status back to ACTIVE |

### Search Operations
| Search Type | URL | Description |
|---|---|---|
| Find by ID | `/users/findById?id=1` | Find a specific student by ID |
| Find by Name | `/users/findByName?name=Madhu` | Find all students with a given name |
| Find by Email | `/users/findByEmail?email=abc@gmail.com` | Find student by email |

### Other Features
- **Form Validation** — Empty or invalid form inputs are caught and shown back to the user
- **Soft Delete vs Hard Delete** — Two types of delete demonstrated side by side
- **Status Management** — Students can be marked ACTIVE or INACTIVE and restored
- **Custom Error Page** — All exceptions caught globally and shown on a styled error page

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 4.0.3 | Application framework |
| Spring MVC | Web layer (Controller → Model → View) |
| Spring Data JPA | Database interaction (ORM) |
| Thymeleaf | Server-side HTML template engine |
| MySQL | Relational database |
| Lombok | Reduces boilerplate (@Data) |
| Spring Validation | Form validation (@Valid, BindingResult) |
| Spring Boot DevTools | Hot reload during development |
| Maven | Build and dependency management |

---

## Project Structure

```
StudentSearchManagementSystem/
└── src/main/
    ├── java/com/madhu/
    │   ├── StudentSearchManagementSystem.java   ← @SpringBootApplication entry point
    │   ├── entity/
    │   │   └── StudentEntity.java               ← JPA Entity (maps to student_entity table)
    │   ├── repository/
    │   │   └── StudentRepository.java           ← Spring Data JPA + @Query methods
    │   ├── service/
    │   │   └── StudentService.java              ← All business logic
    │   ├── controller/
    │   │   └── StudentController.java           ← @Controller, handles browser requests
    │   └── exception/
    │       └── HandleException.java             ← @ControllerAdvice, global error handler
    └── resources/
        ├── application.properties
        └── templates/                           ← Thymeleaf HTML pages
            ├── students.html                    ← Main student list table
            ├── add-student.html                 ← Add new student form
            ├── edit-student.html                ← Edit existing student form
            ├── find.html                        ← Search students page
            └── error.html                       ← Global error display page
```

---

## Key Concepts Demonstrated

### 1. Spring MVC vs REST API
This project uses `@Controller` (not `@RestController`). The controller returns a view name (HTML page), not JSON data.

```java
// REST API style — returns JSON
@RestController
public String getStudents() {
    return service.getAllStudents();
}

// Spring MVC style — returns HTML page (used in this project)
@Controller
public String getStudents(Model model) {
    model.addAttribute("students", service.getAllStudents());
    return "students";  // renders students.html via Thymeleaf
}
```

### 2. Thymeleaf Binding
```html
<!-- Loop through students list -->
<tr th:each="student : ${students}">
    <td th:text="${student.name}"></td>
    <td th:text="${student.email}"></td>
</tr>

<!-- Form bound to StudentEntity object -->
<form th:action="@{/savestudent}" th:object="${student}" method="post">
    <input type="text" th:field="*{name}" />
    <input type="email" th:field="*{email}" />
</form>

<!-- Conditional rendering -->
<div th:if="${student != null}">
    <p th:text="${student.name}"></p>
</div>
```

### 3. Soft Delete vs Hard Delete
```java
// Soft Delete — record stays in DB, status changes to INACTIVE
public void deleteStudent(int id) {
    StudentEntity student = getStudentBYID(id);
    student.setStatus("INACTIVE");
    repository.save(student);
}

// Hard Delete — record is permanently removed from DB
public void deleteStudent1(int id) {
    repository.deleteById(id);
}
```

### 4. Native SQL Query with @Query
```java
@Query(value = "SELECT * FROM student_entity WHERE status='ACTIVE'", nativeQuery = true)
List<StudentEntity> findActiveStudents();

@Query(value = "SELECT * FROM student_entity WHERE status='INACTIVE'", nativeQuery = true)
List<StudentEntity> findInActiveStudents();
```

### 5. Form Validation
```java
@PostMapping("/savestudent")
public String addStudent(@Valid @ModelAttribute("student") StudentEntity entity,
                          BindingResult result) {
    if (result.hasErrors()) {
        return "add-student";  // stay on form, show errors
    }
    service.addStudents(entity);
    return "redirect:/students";
}
```

### 6. Global Exception Handling
```java
@ControllerAdvice
public class HandleException {

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, Model model) {
        model.addAttribute("error", "Invalid form input: " + ex.getMessage());
        return "error";  // renders error.html
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
```

---

## Database

**Database name:** `mydb1`

**Table:** `student_entity` (auto-created by Hibernate via `ddl-auto=update`)

| Column | Type | Description |
|---|---|---|
| id | INT PK AUTO_INCREMENT | Primary key |
| name | VARCHAR | Student name |
| email | VARCHAR | Student email |
| course | VARCHAR | Enrolled course |
| fees | DOUBLE | Course fee |
| status | VARCHAR | ACTIVE or INACTIVE |

---

## How to Run

### Prerequisites
- Java 17
- MySQL running on port 3306
- Database `mydb1` created

```sql
CREATE DATABASE mydb1;
```

### Step 1 — Set Environment Variables

**Windows:**
```
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_mysql_password
```

**Mac/Linux:**
```
export DB_USERNAME=your_mysql_username
export DB_PASSWORD=your_mysql_password
```

### Step 2 — Run the Application

```
./mvnw spring-boot:run
```

### Step 3 — Open in Browser

```
http://localhost:8080/mvctask/students
```

Hibernate automatically creates the `student_entity` table on first run.

---

## How It Differs from a REST API Project

| Feature | This Project (Spring MVC) | REST API Project |
|---|---|---|
| Controller | `@Controller` | `@RestController` |
| Response | HTML page (via Thymeleaf) | JSON data |
| Tested with | Browser | Postman / Swagger |
| Form handling | `@ModelAttribute`, `th:field` | `@RequestBody` |
| Redirect | `return "redirect:/students"` | HTTP 3xx response |
| Error handling | Returns error.html | Returns JSON error body |

---

**Venkata Madhu Thota**
GitHub: [@VenkataMadhuThota](https://github.com/VenkataMadhuThota)
