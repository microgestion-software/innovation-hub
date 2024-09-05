package com.microgestion.example.hub.rewrite.springboot.service;

import com.microgestion.example.hub.rewrite.springboot.model.Task;
import com.microgestion.example.hub.rewrite.springboot.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus(Task.TaskStatus.PENDING);
    }

    @Test
    void getTaskById_shouldReturnTask_whenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(testTask.getTitle(), result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void createTask_shouldReturnSavedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.createTask(testTask);

        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
}