package com.ip13.main

import com.ip13.main.model.entity.FileInfo
import com.ip13.main.model.entity.FileInfoId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FileInfoRepository : CrudRepository<FileInfo, FileInfoId>