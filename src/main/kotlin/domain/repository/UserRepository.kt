package domain.repository

import data.db.User
import java.util.UUID

interface UserRepository {
    suspend fun allUsers(): List<User>
    suspend fun getUserById(id: String): User?
    suspend fun addNewUser(user: User): User?
    suspend fun editUser(user: User): User?
    suspend fun deleteUser(id: String): Boolean
}