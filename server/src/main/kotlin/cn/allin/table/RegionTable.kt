package cn.allin.table

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("region")
class RegionTable(
    @Id val id: Int,
    val name: String,
    @Column("parent_id")
    val parentId: Int,
)
