package com.ip13.main.model.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity

@Entity
class FileInfo(
    @EmbeddedId
    val fileInfoId: FileInfoId = FileInfoId(),
    val username: String = "",
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileInfo

        if (fileInfoId != other.fileInfoId) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileInfoId.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }

    override fun toString(): String {
        return "FileInfo(fileInfoId=$fileInfoId, filename='$username')"
    }
}