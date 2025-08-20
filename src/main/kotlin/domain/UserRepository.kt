package com.example.domain

import User


interface UserRepository {
    suspend fun allUsers(): List<User>
    suspend fun getUserById(id: Int): User?
    suspend fun addNewUser(name: String, email: String, age: Int): User?
    suspend fun editUser(id: Int, name: String, email: String, age: Int): User?
    suspend fun deleteUser(id: Int): Boolean
}