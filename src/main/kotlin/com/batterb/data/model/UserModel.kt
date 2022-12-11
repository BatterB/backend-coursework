package com.batterb.data.model

import com.batterb.domain.dto.UserDTO
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object UserModel : Table("user") {
    private val userId = UserModel.integer("user_id")
    private val userName = UserModel.varchar("username", 25)

    fun insert(userDTO: UserDTO) {
        transaction {
            UserModel.insert {
                it[userId] = userDTO.id
                it[userName] = userDTO.username
            }
        }
    }
}
