package com.example.data.db

import data.db.UsersSchema
import org.jetbrains.exposed.sql.Table

class RolesSchema {

    object Roles : Table("roles") {
        val id = uuid("id").autoGenerate()
        val name = varchar("name", 255)
        override val primaryKey = PrimaryKey(id)
    }

    object UserRoles : Table("user_roles") {
        val userId = reference("user_id", UsersSchema.UserTable.id)
        val roleId = reference("role_id", Roles.id)
        override val primaryKey = PrimaryKey(userId, roleId)
    }

}