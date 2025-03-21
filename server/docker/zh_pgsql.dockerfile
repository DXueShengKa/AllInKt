FROM postgres:latest

RUN localedef -i zh_CN -c -f UTF-8 -A usr/share/locale/locale.alias zh_CN.UTF-8

ENV LANG=zh_CN.UTF-8 \
    LANGUAGE=zh_CN:en_US \
    LC_ALL=zh_CN.UTF-8
