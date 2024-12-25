package cn.allin.model

import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table

@Entity
@Table(name = "region")
interface RegionEntity {
    @Id
    val id: Int
    @Column(name = "parent_id")
    val parentId: Int
    val name: String
}