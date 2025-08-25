package domain.usecase

import domain.repository.TaskRepository
import com.example.models.Priority
import com.example.models.Task

class TaskUseCase(private val repository: TaskRepository) {

    suspend fun allTasks(): List<Task> {
        return repository.allTasks()
    }

    suspend fun tasksByPriority(priority: Priority): List<Task> {
        return repository.tasksByPriority(priority)
    }

    suspend fun taskByName(name: String): Task? {
        return repository.taskByName(name)
    }

    suspend fun addTask(task: Task) {
        return repository.addTask(task)
    }

    suspend fun removeTask(name: String): Boolean {
        return repository.removeTask(name)
    }

}