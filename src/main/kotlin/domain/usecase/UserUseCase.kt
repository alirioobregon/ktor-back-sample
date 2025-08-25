package com.example.domain.usecase

import data.db.User
import domain.repository.UserRepository

class UserUseCase(private val repository: UserRepository) {

    suspend fun allUsers(): List<User> {
        return repository.allUsers()
    }

    suspend fun getUserById(id: String): User? {
        return repository.getUserById(id)
    }

    suspend fun addNewUser(user: User): User? {
        return repository.addNewUser(user)
    }

    suspend fun editUser(user: User): User? {
        return repository.editUser(user)
    }

    suspend fun deleteUser(id: String): Boolean {
        return repository.deleteUser(id)
    }
}