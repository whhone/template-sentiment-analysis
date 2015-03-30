package org.template.sentimentanalysis

import io.prediction.controller.PDataSource
import io.prediction.controller.EmptyEvaluationInfo
import io.prediction.controller.EmptyActualResult
import io.prediction.controller.Params
import io.prediction.data.storage.Event
import io.prediction.data.storage.Storage

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import grizzled.slf4j.Logger

case class DataSourceParams(appId: Int) extends Params

class DataSource(val dsp: DataSourceParams)
  extends PDataSource[TrainingData,
      EmptyEvaluationInfo, Query, EmptyActualResult] {

  @transient lazy val logger = Logger[this.type]

  override
  def readTraining(sc: SparkContext): TrainingData = {
    val eventsDB = Storage.getPEvents()
    val eventsRDD: RDD[Event] = eventsDB.find(
      appId = dsp.appId,
      entityType = Some("user"),
      eventNames = Some(List("train"))
    ) (sc)
    
    val sentimentsRDD: RDD[Sentiment] = eventsRDD.map { event =>
      val sentiment = try {
        val sentimentValue: Double = event.event match {
          case "train" => event.properties.get[Double]("sentiment")
          case _ => throw new Exception(s"Unexpected event ${event} is read.")
        }
      
        Sentiment(
          event.properties.get[String]("phrase"),
          sentimentValue)
      } catch {
        case e: Exception => {
          logger.error(
            s"Cannot convert ${event} to Sentiment. Exception: ${e}.")
          throw e
        }
      }
      sentiment
    }.cache()
    
    new TrainingData(sentimentsRDD)
  }
}

case class Sentiment(
  phrase: String,
  sentiment: Double
)

class TrainingData(
  val sentiments: RDD[Sentiment]
) extends Serializable { }

