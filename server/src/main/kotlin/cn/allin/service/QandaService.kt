package cn.allin.service

import cn.allin.repository.QandaRepository
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import org.springframework.stereotype.Service

@Service
class QandaService(private val qandaRepository: QandaRepository) {

    fun page(index: Int, size: Int): PageVO<QandaVO> {
        return qandaRepository.findPage(index, size)
    }

    fun add(pageVO: QandaVO): Int {
       return qandaRepository.add(pageVO)
    }

    fun delete(id: Int) {
        qandaRepository.delete(id)
    }

}
