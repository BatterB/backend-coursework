package com.batterb.views.viewobjects

import kotlinx.serialization.Serializable

@Serializable
data class FileVO (
    val fileName: String,
    val storageFileName: String?,
    val data: String?
)
