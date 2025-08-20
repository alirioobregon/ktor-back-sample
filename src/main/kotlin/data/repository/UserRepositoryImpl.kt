package com.example.data.repository

import User
import com.example.core.utils.Utils.suspendTransaction
import com.example.domain.UserRepository
import toModelUser

class UserRepositoryImpl : UserRepository {

    override suspend fun allUsers(): List<User> = suspendTransaction {
        UserDAO.all().map {
            toModelUser(it)
        }
    }

    override suspend fun getUserById(id: Int): User? = suspendTransaction {
        UserDAO.findById(id)?.let { toModelUser(it) }
    }

    override suspend fun addNewUser(name: String, email: String, age: Int): User = suspendTransaction {
        val userDao = UserDAO.new {
            this.name = name
            this.email = email
            this.age = 0
        }
        toModelUser(userDao)
    }

    override suspend fun editUser(id: Int, name: String, email: String, age: Int): User? = suspendTransaction {
        val userDao = UserDAO.findById(id)
        userDao?.apply {
            this.name = name
            this.email = email
            this.age = age
        }?.let { toModelUser(it) }
    }

    override suspend fun deleteUser(id: Int): Boolean = suspendTransaction {
        val userDao = UserDAO.findById(id)
        if (userDao != null) {
            userDao.delete()
            true
        } else {
            false
        }
    }

}