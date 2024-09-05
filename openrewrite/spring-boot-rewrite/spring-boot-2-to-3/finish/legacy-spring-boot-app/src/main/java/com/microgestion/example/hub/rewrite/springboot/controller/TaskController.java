package com.microgestion.example.hub.rewrite.springboot.controller;

import com.microgestion.example.hub.rewrite.springboot.model.Task;
import com.microgestion.example.hub.rewrite.springboot.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * A controller class that provides RESTful endpoints for managing tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    /**
     * The service for managing tasks.
     */
    private final TaskService taskService;

    /**
     * Constructs a new TaskController with the given task service.
     *
     * @param taskService The service for managing tasks.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return A list of all tasks.
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id The unique identifier of the task.
     * @return The task if found, or a 404 response otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new task in the database.
     *
     * @param task The task to create.
     * @return The created task.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    /**
     * Updates an existing task in the database.
     *
     * @param id          The unique identifier of the task to update.
     * @param taskDetails The details of the task to update.
     * @return The updated task if found, or a 404 response otherwise.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        return taskService.updateTask(id, taskDetails)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a task from the database.
     *
     * @param id The unique identifier of the task to delete.
     * @return A 200 response if the task was deleted, or a 404 response otherwise.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all tasks with the given status.
     *
     * @param status The status of the tasks to find.
     * @return A list of tasks with the given status.
     */
    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable Task.TaskStatus status) {
        return taskService.getTasksByStatus(status);
    }
}