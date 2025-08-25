package com.example.data.db

import com.example.core.utils.UUIDSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


@Serializable
data class DeviceVO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val name: String,
    val description: String
)

class DeviceService() {
    object Devices : Table("devices") {
        val id = uuid("id").autoGenerate()
        val name = varchar("name", length = 255)
        val description = varchar("description", 300)
        val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

        override val primaryKey = PrimaryKey(id)
    }

//    init {
//        transaction(database) {
//            SchemaUtils.create(Users)
//        }
//    }

    suspend fun create(device: DeviceVO): UUID = dbQuery {
        val row = Devices.insert {
            it[id] = UUID.randomUUID()
            it[name] = device.name
            it[description] = device.description
        } get Devices.id
        row
    }

    suspend fun read(id: String): DeviceVO? {
        return dbQuery {
            Devices.selectAll()
                .where { Devices.id eq UUID.fromString(id) }
                .map { DeviceVO(it[Devices.id], it[Devices.name], it[Devices.description]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: String, device: DeviceVO) {
        dbQuery {
            Devices.update({ Devices.id eq UUID.fromString(id) }) {
                it[name] = device.name
                it[description] = device.description
            }
        }
    }

    suspend fun delete(id: String) {
        dbQuery {
            Devices.deleteWhere { Devices.id.eq(UUID.fromString(id)) }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}


