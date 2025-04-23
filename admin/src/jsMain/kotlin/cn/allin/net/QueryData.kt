package cn.allin.net

import react.useEffect
import react.useState


class QueryData<D : Any>(
    val data: D?,
    private val refresh: () -> Unit,
) {
    fun refresh() {
        refresh.invoke()
    }
}


fun <D : Any, P> useQuery(
    params: P? = null,
    queryFn: suspend Req.(P?) -> D?
): QueryData<D> {
    var data by useState<D>()
    var count by useState(0)
    val refresh: () -> Unit = {
        count++
    }

    useEffect(params, count) {
        data = queryFn(Req, params)
    }

    return QueryData<D>(
        data, refresh
    )
}
