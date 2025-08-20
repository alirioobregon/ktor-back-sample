import com.example.data.db.TaskTable
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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
data class User(val id: Int, val name: String, val age: Int, val email: String)
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
//    suspend fun create(user: User): Int = dbQuery {
//        Users.insert {
//            it[name] = user.name
//            it[age] = user.age
//        }[Users.id]
//    }
//
//    suspend fun read(id: Int): User? {
//        return dbQuery {
//            Users.selectAll()
//                .where { Users.id eq id }
//                .map { User(it[Users.name], it[Users.age]) }
//                .singleOrNull()
//        }
//    }
//
//    suspend fun update(id: Int, user: User) {
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
//    fun daoToModel(dao: User) = User(
//        dao.name,
//        dao.age
//    )
//}
//

object UserTable : IntIdTable("users") {
    //    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100).uniqueIndex()
    val age = integer("age")

//    override val primaryKey = PrimaryKey(id)
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var name by UserTable.name
    var email by UserTable.email
    var age by UserTable.age
}

// Función para correr transacciones suspendidas
//suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
//    newSuspendedTransaction(Dispatchers.IO, statement = block)

// Mapper DAO → Model
fun toModelUser(dao: UserDAO) = User(
    id = dao.id.value,
    name = dao.name,
    age = dao.age,
    email = dao.email
)


