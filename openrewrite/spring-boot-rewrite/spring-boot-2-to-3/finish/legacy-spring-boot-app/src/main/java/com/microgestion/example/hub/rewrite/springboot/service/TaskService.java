package com.microgestion.example.hub.rewrite.springboot.service;

import com.microgestion.example.hub.rewrite.springboot.model.Task;
import com.microgestion.example.hub.rewrite.springboot.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service class that provides methods for managing tasks.
 */
@Service
public class TaskService {

    /**
     * The repository for managing Task entities in the database.
     */
    private final TaskRepository taskRepository;

    /**
     * Constructs a new TaskService with the given task repository.
     *
     * @param taskRepository The repository for managing Task entities.
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return A list of all tasks.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id The unique identifier of the task.
     * @return An optional containing the task if found, or an empty optional otherwise.
     */
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Creates a new task in the database.
     *
     * @param task The task to create.
     * @return The created task.
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Updates an existing task in the database.
     *
     * @param id          The unique identifier of the task to update.
     * @param taskDetails The details of the task to update.
     * @return An optional containing the updated task if found, or an empty optional otherwise.
     */
    public Optional<Task> updateTask(Long id, Task taskDetails) {
        return taskRepository.findById(id)
            .map(task -> {
                task.setTitle(taskDetails.getTitle());
                task.setDescription(taskDetails.getDescription());
                task.setStatus(taskDetails.getStatus());
                return taskRepository.save(task);
            });
    }

    /**
     * Deletes a task from the database.
     *
     * @param id The unique identifier of the task to delete.
     * @return True if the task was deleted, false otherwise.
     */
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
            .map(task -> {
                taskRepository.delete(task);
                return true;
            })
            .orElse(false);
    }

    /**
     * Retrieves all tasks with the given status.
     *
     * @param status The status of the tasks to find.
     * @return A list of tasks with the given status.
     */
    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
}