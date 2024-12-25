package cn.allin.repository

import cn.allin.model.RegionEntity
import cn.allin.model.parentId
import cn.allin.utils.toVO
import cn.allin.vo.RegionVO
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Repository

@Repository
class RegionRepository(
    private val sqlClient: KSqlClient
) {

    fun findAllProvince(): List<RegionVO> {
        return sqlClient
            .executeQuery(RegionEntity::class) {
                where(table.parentId eq 1)
                select(table)
            }
            .map(RegionEntity::toVO)
    }

    fun findCity(provinceId: Int): List<RegionVO> {
        return sqlClient.executeQuery(RegionEntity::class) {
            where(table.parentId eq provinceId)
            select(table)
        }
            .map(RegionEntity::toVO)
    }


    fun findCounty(cityId: Int): List<RegionVO> {
        return sqlClient.executeQuery(RegionEntity::class) {
            where(table.parentId eq cityId)
            select(table)
        }
            .map(RegionEntity::toVO)
    }


}