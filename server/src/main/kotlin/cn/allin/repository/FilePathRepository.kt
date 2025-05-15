package cn.allin.repository

import cn.allin.model.FilePathEntity
import org.babyfish.jimmer.spring.repository.KRepository

interface FilePathRepository: KRepository<FilePathEntity, Int> {

    fun findAllByParentIdIsNull(): List<FilePathEntity>

}
