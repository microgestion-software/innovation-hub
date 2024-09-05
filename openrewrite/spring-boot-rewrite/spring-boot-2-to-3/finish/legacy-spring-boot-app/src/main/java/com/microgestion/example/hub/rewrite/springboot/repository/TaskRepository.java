package com.microgestion.example.hub.rewrite.springboot.repository;

import com.microgestion.example.hub.rewrite.springboot.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A repository interface for managing Task entities in the database.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds all tasks with the given status.
     *
     * @param status The status of the tasks to find.
     * @return A list of tasks with the given status.
     */
    List<Task> findByStatus(Task.TaskStatus status);

}