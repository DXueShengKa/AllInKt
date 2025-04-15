package org.babyfish.jimmer.client.generator.openapi;


/**
 * 移除jimmer.client后配置报错，用来占位的
 */
public class OpenApiProperties {
    public static class Info{}


    public static Builder newBuilder(OpenApiProperties object) {
        return new Builder();
    }

     public static InfoBuilder newInfoBuilder(Info o) {
        return new InfoBuilder();
    }

    public static class Builder {


        public Builder setInfo(Info info) {
            return this;
        }



        public OpenApiProperties build() {
            return null;
        }
    }

    public static class InfoBuilder {

        public InfoBuilder setTitle(String title) {

            return this;
        }

        public InfoBuilder setDescription(String description) {

            return this;
        }


        public InfoBuilder setVersion(String version) {
            return this;
        }

        public Info build() {
            return null;
        }
    }

}
