package cn.allin.model

import cn.allin.ksp.server.EntityToVo
import cn.allin.vo.RegionVO
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table

@EntityToVo([RegionVO::class])
@Entity
@Table(name = "region")
interface RegionEntity {
    @Id
    val id: Int
    @Column(name = "parent_id")
    val parentId: Int
    val name: String
}
