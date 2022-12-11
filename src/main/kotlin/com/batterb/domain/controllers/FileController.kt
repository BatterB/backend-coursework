package com.batterb.domain.controllers

import com.batterb.data.model.FileModel
import com.batterb.domain.dto.FileDTO
import com.batterb.domain.dto.toViewObject
import com.batterb.views.viewobjects.FileVO
import com.batterb.views.viewobjects.UserVO
import java.io.File
import java.util.*

class FileController {
    fun createFile(filename: String, userId: String): Boolean {
        try {
            val storageFileName = UUID.randomUUID().toString()
            FileModel.insert(
                FileDTO(
                    userId = userId,
                    userFileName = filename,
                    storageFileName = storageFileName
                )
            )
            val file = File("./src/main/resources/files/${storageFileName}.txt")
            file.createNewFile()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun getFile(username: String, filename: String): FileVO {
        val file = FileModel.fetchServerFileName(username, filename)
        return file.toViewObject()
    }

    fun getFileData(file: String): String {
        val file = File("./src/main/resources/files/$file.txt")
        return file.readText()
    }

    fun writeFileData(file: String, data: String) {
        val fileOnServer = File("./src/main/resources/files/$file.txt")
        fileOnServer.writeText(data)
    }

    fun getServerFilename(user: UserVO) =
        FileModel.fetchServerFileName(user.username, user.currentEditingFile!!.fileName).storageFileName

    fun addSharedFile(userID: String, filename: String) {
        val userFileName = FileModel.fetchUserFileName(filename)
        FileModel.insert(
            FileDTO(
                userId = userID,
                userFileName = userFileName,
                storageFileName = filename
            )
        )
    }

    fun getAllUserFiles(user: UserVO): List<FileVO> {
        return FileModel.fetchAllFiles(user.username).map { it.toViewObject() }
    }
}