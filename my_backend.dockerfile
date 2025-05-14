FROM scratch

WORKDIR /server

COPY server/build/libs/ /server/
COPY admin/build/dist/js/productionExecutable/ /html



