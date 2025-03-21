#ARG PG_CONTAINER_VERSION=16
#FROM postgres:${PG_CONTAINER_VERSION}-alpine as builder
#
#RUN set -ex \
#  && apk --no-cache add git build-base linux-headers make postgresql-dev automake libtool autoconf m4
#
#RUN set -ex \
#  && git clone --branch 1.2.3 --single-branch --depth 1 https://github.com/hightman/scws.git \
#  && cd scws \
#  && touch README;aclocal;autoconf;autoheader;libtoolize;automake --add-missing \
#  && ./configure \
#  && make install
#
#RUN set -ex \
#  && git clone --branch master --single-branch --depth 1 https://github.com/amutu/zhparser.git \
#  && cd zhparser \
#  && make install
#
#FROM pgvector/pgvector:pg${PG_CONTAINER_VERSION}
#ENV LANG zh_CN.UTF-8
#
#COPY --from=builder /usr/local/lib/postgresql/zhparser.so /usr/local/lib/postgresql/
#COPY --from=builder /usr/local/lib/libscws.* /usr/local/lib/
#COPY --from=builder /usr/local/share/postgresql/extension/zhparser* /usr/local/share/postgresql/extension/
#COPY --from=builder /usr/local/lib/postgresql/bitcode/zhparser* /usr/local/lib/postgresql/bitcode/
#COPY --from=builder /usr/local/share/postgresql/tsearch_data/*.utf8.* /usr/local/share/postgresql/tsearch_data/


#ARG PG_MAJOR=16
#
#FROM postgres:$PG_MAJOR as builder
#
#RUN set -ex \
#&& apt-get update \
#&& apt-get install -y --no-install-recommends build-essential git ntp ca-certificates autoconf automake libtool m4 postgresql-server-dev-$PG_MAJOR
#
#RUN set -ex \
#&& git clone https://github.com/hightman/scws.git \
#&& cd scws \
#&& touch README;aclocal;autoconf;autoheader;libtoolize;automake --add-missing \
#&& ./configure \
#&& make install
#
#RUN set -ex \
#&& git clone https://github.com/amutu/zhparser.git \
#&& cd zhparser \
#&& make install
#
#FROM pgvector/pgvector:pg16
#ENV LANG zh_CN.UTF-8
#
#COPY --from=builder /usr/lib/postgresql/${PG_MAJOR}/lib/zhparser.so /usr/lib/postgresql/${PG_MAJOR}/lib/
#COPY --from=builder /usr/local/lib/libscws.* /usr/local/lib/
#COPY --from=builder /usr/share/postgresql/${PG_MAJOR}/extension/zhparser* /usr/share/postgresql/${PG_MAJOR}/extension/
#COPY --from=builder /usr/lib/postgresql/${PG_MAJOR}/lib/bitcode/zhparser* /usr/lib/postgresql/${PG_MAJOR}/lib/bitcode/
#COPY --from=builder /usr/share/postgresql/${PG_MAJOR}/tsearch_data/.utf8. /usr/share/postgresql/${PG_MAJOR}/tsearch_data/


FROM crpi-ofliz85y5t1nt3ne.cn-guangzhou.personal.cr.aliyuncs.com/allkt/kmp_server:zh_pgsql17

RUN apt-get update && \
    apt-get install -y \
        git \
        build-essential \
        postgresql-server-dev-17 \
    && rm -rf /var/lib/apt/lists/*

# 安装pgvector扩展
RUN git clone --branch v0.8.0 https://github.com/pgvector/pgvector.git \
    && cd pgvector \
    && PG_CONFIG=/usr/lib/postgresql/17/bin/pg_config make \
    && PG_CONFIG=/usr/lib/postgresql/17/bin/pg_config make install \
    && cd .. \
    && rm -rf pgvector \

# 追加pgvector初始化命令到现有脚本
RUN echo "CREATE EXTENSION IF NOT EXISTS vector;" >> /docker-entrypoint-initdb.d/initdb.sh
