package com.batterb.domain.dto

import com.batterb.views.viewobjects.FileVO

data class FileDTO(
    val fileId: Int = 0,
    val userId: String,
    val userFileName: String,
    val storageFileName: String
)

fun FileDTO.toViewObject() = FileVO(
    fileName = this.userFileName,
    storageFileName = this.storageFileName,
    data = null
)