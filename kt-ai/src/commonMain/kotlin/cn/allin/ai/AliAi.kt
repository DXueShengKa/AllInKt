package cn.allin.ai

import com.aallam.openai.api.embedding.EmbeddingResponse
import com.aallam.openai.client.OpenAI

class AliAi(
    token: String,
) {

    val openAI: OpenAI = OpenAI(token)

    fun qa(er:EmbeddingResponse){
        er.embeddings.forEach {
            it.embedding
        }
        openAI
    }
}
