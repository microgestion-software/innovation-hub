package com.microgestion.example.hub.rewrite.springboot.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


/**
 * Represents a task entity with attributes such as id, title, description, status, createdAt, and updatedAt.
 * This entity is mapped to the "tasks" table in the database.
 */
@Entity
@Table(name = "tasks")
public class Task {
    /**
     * The unique identifier for the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the task. This field is required.
     */
    @NotBlank(message = "Task title is required")
    private String title;

    /**
     * A detailed description of the task.
     */
    private String description;

    /**
     * The current status of the task.
     */
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    /**
     * The date and time when the task was created.
     */
    private LocalDateTime createdAt;

    /**
     * The date and time when the task was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Constructor that initializes the task with the given title and description.
     * The status is set to PENDING and the createdAt and updatedAt timestamps are set to the current time.
     * @param title The title of the task.
     * @param description The description of the task.
     */
    public Task() {
        this.status = TaskStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Method annotated with @PreUpdate to update the updatedAt timestamp before the
     * entity is updated in the database.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Enum representing the possible statuses of a task.
     */
    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED
    }


    // Getters and Setters

    /**
     * Getter for the task id.
     * @return The task id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the task id.
     * @param id The task id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the task title.
     * @return The task title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the task title.
     * @param title The task title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the task description.
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the task description.
     * @param description The task description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the task status.
     * @return The task status.
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Setter for the task status.
     * @param status The task status to set.
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Getter for the task createdAt timestamp.
     * @return The task createdAt timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // /**
    //  * Setter for the task createdAt timestamp.
    //  * @param createdAt The task createdAt timestamp to set.
    //  */
    // public void setCreatedAt(LocalDateTime createdAt) {
    //     this.createdAt = createdAt;
    // }

    /**
     * Getter for the task updatedAt timestamp.
     * @return The task updatedAt timestamp.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // /**
    //  * Setter for the task updatedAt timestamp.
    //  * @param updatedAt The task updatedAt timestamp to set.
    //  */
    // public void setUpdatedAt(LocalDateTime updatedAt) {
    //     this.updatedAt = updatedAt;
    // }
}