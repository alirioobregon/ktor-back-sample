package com.example.data.repository

import data.db.User
import data.db.UsersSchema
import domain.repository.UserRepository
import org.jetbrains.exposed.sql.selectAll

class UserRepositoryImpl : UserRepository {

    private val usersSchema = UsersSchema()

    override suspend fun allUsers(): List<User> = usersSchema.all()

    override suspend fun getUserById(id: String): User? = usersSchema.getUserById(id)

    override suspend fun addNewUser(user: User): User? = usersSchema.addNew(user)

    override suspend fun editUser(user: User): User? = usersSchema.update(user)

    override suspend fun deleteUser(id: String): Boolean = usersSchema.remove(id)

}