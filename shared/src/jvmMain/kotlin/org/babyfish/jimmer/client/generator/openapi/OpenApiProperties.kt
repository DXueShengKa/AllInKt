package org.babyfish.jimmer.client.generator.openapi


/**
 * 移除jimmer.client后配置报错，用来占位的
 */
class OpenApiProperties {
    companion object{

        @JvmStatic
        fun newBuilder(o: OpenApiProperties?): Builder {
            return Builder()
        }
        @JvmStatic
        fun newInfoBuilder(o: Info?): InfoBuilder {
            return InfoBuilder()
        }
    }

    class Info


    class Builder {
        fun setInfo(info: Info?): Builder {
            return this
        }


        fun build(): OpenApiProperties? {
            return null
        }
    }

    class InfoBuilder {
        fun setTitle(title: String?): InfoBuilder {
            return this
        }

        fun setDescription(description: String?): InfoBuilder {
            return this
        }


        fun setVersion(version: String?): InfoBuilder {
            return this
        }

        fun build(): Info? {
            return null
        }
    }
}