package cn.allin.utils

import org.babyfish.jimmer.sql.kt.ast.expression.KExpression
import org.babyfish.jimmer.sql.kt.ast.expression.KNonNullExpression
import org.babyfish.jimmer.sql.kt.ast.expression.sql


infix fun KExpression<String>.substring(range: IntRange) = substring(range.start, range.endInclusive)

fun KExpression<String>.substring(start: Int, end: Int): KNonNullExpression<String> {
//    @Language("sql")
    val sql = "substring(%e,${start},${end})"
    return sql(String::class, sql) {
        expression(this@substring)
    }
}
