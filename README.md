## flink kafka示例程序
&emsp;&emsp;本程序读取kafka数据按桶写到hdfs上，文件分区按照1分钟一次，分区大小设为20B
### 各组件版本
kafka 0.8.2.2

flink 1.4.0

hadoop 2.6.5

scala 2.10

java 1.8
### 运行环境
&emsp;&emsp;为快速测试程序，本程序使用docker环境下的flink kafka来测试，kafka镜像为自己制作。

flink docker镜像
> docker pull flink:1.4-hadoop26
> export FLINK_DOCKER_IMAGE_NAME=docker.io/flink:1.4-hadoop26

&emsp;&emsp;使用docker-compose快速搭建flink集群，docker-compose.yaml示例如https://github.com/apache/flink/blob/master/flink-contrib/docker-flink/docker-compose.yml

### Usage
集群建立

* 启动集群
```bash
docker-compose up
```
* 后台启动
```bash
docker-compose up -d
```
* 集群启动后调整TaskManagers的数目
```bash
docker-compose scale taskmanager=<N>
```
提交作业
&emsp;&emsp;将打包好的jar包放在某个目录下
运行下列脚本提交作业
```bash
JOBMANAGER_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
docker cp $1 "$JOBMANAGER_CONTAINER":/job.jar
docker exec -t -i "$JOBMANAGER_CONTAINER" flink run /job.jar
```

