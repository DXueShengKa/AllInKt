### docker compose
* docker compose up会重建所有容器
* 在同一个项目里默认共享容器网络

````
# 启动所有服务（如果尚未运行）
docker-compose -f file1.yml -f file2.yml up -d

# 仅更新 file2.yml 中的服务
docker compose -f file2.yml up -d

````

