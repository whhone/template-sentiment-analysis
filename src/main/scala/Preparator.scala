package org.template.sentimentanalysis

import io.prediction.controller.PPreparator

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

class Preparator
  extends PPreparator[TrainingData, PreparedData] {

  def prepare(sc: SparkContext, trainingData: TrainingData): PreparedData = {
    new PreparedData(sentiments = trainingData.sentiments)
  }
}

class PreparedData(
  val sentiments: RDD[Sentiment]
) extends Serializable

