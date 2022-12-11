package com.batterb.data.model

import com.batterb.domain.dto.FileDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object FileModel : Table("file") {
    private val fileId = FileModel.integer("file_id")
    private val userId = FileModel.varchar("user_id", 50)
    private val userFileName = FileModel.varchar("user_filename", 25)
    private val serverFileName = FileModel.varchar("server_filename", 50)

    fun insert(fileDTO: FileDTO) {
        transaction {
            FileModel.insert {
                it[userId] = fileDTO.userId
                it[userFileName] = fileDTO.userFileName
                it[serverFileName] = fileDTO.storageFileName
            }
        }
    }

    fun fetchAllFiles(userId: String): List<FileDTO> {
        val filesList = mutableListOf<FileDTO>()
        transaction {
            val fileModels = FileModel.select { FileModel.userId.eq(userId) }
            fileModels.forEach { file ->
                filesList.add(
                    FileDTO(
                        fileId = file[fileId],
                        userId = file[FileModel.userId],
                        userFileName = file[userFileName],
                        storageFileName = file[serverFileName]
                    )
                )
            }
        }
        return filesList.toList()
    }

    fun fetchServerFileName(userId: String, userFileName: String): FileDTO {
        val searchParam = Op.build { FileModel.userId.eq(userId) and FileModel.userFileName.eq(userFileName) }
        val file = transaction {
            FileModel.select { searchParam }.single()
        }
        return FileDTO(
            fileId = file[fileId],
            userId = file[FileModel.userId],
            userFileName = file[FileModel.userFileName],
            storageFileName = file[serverFileName]
        )
    }

    fun fetchUserFileName(fileName: String): String {
        val file = transaction {
            FileModel.select { serverFileName.eq(userFileName) }.single()
        }
        return file[userFileName]
    }
}