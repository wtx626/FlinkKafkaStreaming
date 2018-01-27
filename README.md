## flink kafka demo
本程序读取kafka数据按桶写到hdfs上，文件分区按照1分钟一次，分区大小设为20B
### version
kafka 0.8.2.2

flink 1.4.0

hadoop 2.6.5

scala 2.10

java 1.8
### runtime env
为快速测试程序，本程序使用docker环境下的flink kafka来测试，kafka镜像为自己制作。

flink docker镜像
```bash
docker pull flink:1.4-hadoop26
export FLINK_DOCKER_IMAGE_NAME=docker.io/flink:1.4-hadoop26
```
使用docker-compose快速搭建flink集群，示例[docker-compose.yaml](https://github.com/apache/flink/blob/master/flink-contrib/docker-flink/docker-compose.yml)

### Usage
**集群建立**

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
**提交作业**

将打包好的jar包放在某个目录下运行下列脚本提交作业
```bash
JOBMANAGER_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
docker cp $1 "$JOBMANAGER_CONTAINER":/job.jar
docker exec -t -i "$JOBMANAGER_CONTAINER" flink run /job.jar
```
将上述脚本保存为submitjob.sh，打包之后的flink jar包为bigdataflink-1.0-SNAPSHOT-jar-with-dependencies.jar，使用下面脚本提交作业
```bash
./submitjob.sh bigdataflink-1.0-SNAPSHOT-jar-with-dependencies.jar
```

