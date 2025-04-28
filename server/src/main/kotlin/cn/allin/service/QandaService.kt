package cn.allin.service

import cn.allin.repository.QandaRepository
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import org.springframework.stereotype.Service

@Service
class QandaService(private val qandaRepository: QandaRepository) {

    fun page(index: Int?, size: Int?, isAsc: Boolean?, tagId: Int?): PageVO<QandaVO> {
        return qandaRepository.findPage(index ?: 0, size ?: 10, isAsc ?: true, tagId)
    }

    fun add(pageVO: QandaVO): Int {
        return qandaRepository.add(pageVO)
    }

    fun delete(id: Int): Boolean {
        return qandaRepository.delete(id)
    }

    fun delete(ids: List<Int>?): Int {
        if (ids == null || ids.isEmpty()) return 0
        return qandaRepository.delete(ids)
    }

    fun addList(qaList: List<QandaVO>): Int {
        return qandaRepository.add(qaList)
    }

    fun get(id: Int): QandaVO{

        return qandaRepository.find(id)
    }

    fun update(qanda: QandaVO) {
        qandaRepository.update(qanda)
    }
}
