package com.batterb.views.viewobjects

import kotlinx.serialization.Serializable

@Serializable
data class UserVO(
    val username: String,
    val currentEditingFile: FileVO?
)
