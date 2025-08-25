package data.db

import com.example.core.utils.UUIDSerializer
import com.example.core.utils.Utils.suspendTransaction
import com.example.data.db.RolesSchema
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.UUID
import kotlin.math.log

//package com.example
//
//import com.example.data.db.TaskDAO
//import com.example.models.Priority
//import com.example.models.Task
//import kotlinx.coroutines.Dispatchers
//import kotlinx.serialization.Serializable
//import org.jetbrains.exposed.sql.*
//import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
//import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
//import org.jetbrains.exposed.sql.transactions.transaction
//

@Serializable
data class User(
    val id: String,
    val name: String,
    val age: Int,
    val email: String,
    var roleId: String? = null
)
//
//class UserService(database: Database) {
//    object Users : Table() {
//        val id = integer("id").autoIncrement()
//        val name = varchar("name", length = 50)
//        val age = integer("age")
//
//        override val primaryKey = PrimaryKey(id)
//    }
//
//    init {
//        transaction(database) {
//            SchemaUtils.create(Users)
//        }
//    }
//
//    suspend fun create(user: data.db.User): Int = dbQuery {
//        Users.insert {
//            it[name] = user.name
//            it[age] = user.age
//        }[Users.id]
//    }
//
//    suspend fun read(id: Int): data.db.User? {
//        return dbQuery {
//            Users.selectAll()
//                .where { Users.id eq id }
//                .map { data.db.User(it[Users.name], it[Users.age]) }
//                .singleOrNull()
//        }
//    }
//
//    suspend fun update(id: Int, user: data.db.User) {
//        dbQuery {
//            Users.update({ Users.id eq id }) {
//                it[name] = user.name
//                it[age] = user.age
//            }
//        }
//    }
//
//    suspend fun delete(id: Int) {
//        dbQuery {
//            Users.deleteWhere { Users.id.eq(id) }
//        }
//    }
//
//    private suspend fun <T> dbQuery(block: suspend () -> T): T =
//        newSuspendedTransaction(Dispatchers.IO) { block() }
//
//    fun daoToModel(dao: data.db.User) = data.db.User(
//        dao.name,
//        dao.age
//    )
//}
//

class UsersSchema() {

    object UserTable : Table("users") {
        val id = uuid("id").autoGenerate()

        val name = varchar("name", 50)
        val email = varchar("email", 100).uniqueIndex()
        val age = integer("age")

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun all(): List<User> = suspendTransaction {
        UserTable.selectAll().map { row ->
            User(
                row[UserTable.id].toString(),
                row[UserTable.name],
                row[UserTable.age],
                row[UserTable.email]
            )
        }
    }

    suspend fun getUserById(id: String): User? = suspendTransaction {
        UserTable.selectAll()
            .where { UserTable.id eq UUID.fromString(id) }
            .map { row ->
                User(
                    row[UserTable.id].toString(),
                    row[UserTable.name],
                    row[UserTable.age],
                    row[UserTable.email]
                )
            }
            .singleOrNull()
    }

    suspend fun addNew(user: User): User? = suspendTransaction {

        val idUser = UUID.randomUUID()
        val row = UserTable.insert {
            it[id] = idUser
            it[name] = user.name
            it[age] = user.age
            it[email] = user.email
        }.resultedValues?.singleOrNull()

        println("$row")

        val userVO = row?.let { row ->
            User(
                row[UserTable.id].toString(),
                row[UserTable.name],
                row[UserTable.age],
                row[UserTable.email]
            )
        }


        user.roleId?.let {
            val roleExists = RolesSchema.Roles.select(RolesSchema.Roles.id eq UUID.fromString(user.roleId))
                .count() > 0

            if (roleExists) {
                println("data $idUser --- ${user.roleId}")
                addRoleToUser(idUser, user.roleId.toString())
            }

        }



        userVO
    }

    private fun addRoleToUser(idUser: UUID, roleId: String) {
        try {
            RolesSchema.UserRoles.insert {
                it[RolesSchema.UserRoles.userId] = idUser
                it[RolesSchema.UserRoles.roleId] = UUID.fromString(roleId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun update(user: User): User? = suspendTransaction {
        val row = UserTable.update({ UserTable.id eq UUID.fromString(user.id) }) {
            it[name] = user.name
            it[age] = user.age
            it[email] = user.email
        }

        UserTable.select(UserTable.id eq UUID.fromString(user.id))
            .map { row ->
                User(
                    id = row[UserTable.id].toString(),
                    name = row[UserTable.name],
                    age = row[UserTable.age],
                    email = row[UserTable.email]
                )
            }
            .singleOrNull()
    }

    suspend fun remove(id: String): Boolean = suspendTransaction {
        val deleteRow = UserTable.deleteWhere { UserTable.id.eq(UUID.fromString(id)) }
        deleteRow > 0
    }

}


//class UserDAO(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<UserDAO>(UserTable)
//
//    var name by UserTable.name
//    var email by UserTable.email
//    var age by UserTable.age
//}

// Función para correr transacciones suspendidas
//suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
//    newSuspendedTransaction(Dispatchers.IO, statement = block)

// Mapper DAO → Model
//fun toModelUser(dao: UserDAO) = User(
//    id = dao.id.value,
//    name = dao.name,
//    age = dao.age,
//    email = dao.email
//)


