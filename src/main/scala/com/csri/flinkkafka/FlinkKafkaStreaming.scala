package com.csri.flinkkafka

import java.util.Properties

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.fs.bucketing.{BucketingSink, DateTimeBucketer}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08
import org.apache.flink.streaming.util.serialization.SimpleStringSchema


object FlinkKafkaStreaming {
  private val KAFKA_HOSTNAME = "<ip-kafka>"
  private val ZOOKEEPER_HOST = s"$KAFKA_HOSTNAME:2181"
  private val KAFKA_BROKER = s"$KAFKA_HOSTNAME:9092,$KAFKA_HOSTNAME:9093,$KAFKA_HOSTNAME:9094"
  private val TRANSACTION_GROUP = "myflink"
  private val HDFS = "hdfs://<ip-hdfs>:9000/wtx/test/data"

  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment()
    env.enableCheckpointing(5000)
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", KAFKA_BROKER)
    // only required for Kafka 0.8
    properties.setProperty("zookeeper.connect", ZOOKEEPER_HOST)
    properties.setProperty("group.id", TRANSACTION_GROUP)


    val stream = env.addSource(new FlinkKafkaConsumer08[String]("mykafka",
      new SimpleStringSchema(), properties))

    val sink = new BucketingSink[String](HDFS)
    sink.setBucketer(new DateTimeBucketer[String]("yyyy-MM-dd--HHmm"))
    sink.setBatchSize(20) // this is 20B
    stream.addSink(sink)
    env.execute("MyFlinkKafkaStreaming")
  }
}
