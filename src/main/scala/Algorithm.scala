package org.template.sentimentanalysis

import io.prediction.controller.PAlgorithm
import io.prediction.controller.Params
import io.prediction.data.storage.BiMap

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import edu.stanford.nlp.Magic._

import grizzled.slf4j.Logger

case class AlgorithmParams(
  val baseWeight: Double
)extends Params

class Algorithm(val ap: AlgorithmParams)
  extends PAlgorithm[PreparedData, Model, Query, PredictedResult] {

  @transient lazy val logger = Logger[this.type]

  def train(sc: SparkContext, data: PreparedData): Model = {
    require(!data.sentiments.take(1).isEmpty,
      s"RDD[sentiments] in PreparedData cannot be empty." +
      " Please check if DataSource generates TrainingData" +
      " and Preprator generates PreparedData correctly.")

    val itemSets: RDD[(String, Double)] = data.sentiments.map(
      s => (s.phrase.toLowerCase(), s.sentiment)
    ).cache()

    val rules = itemSets.groupByKey
      .mapValues(
        // assume the last training data is the most up-to-date
        iter => iter.toVector.last
      )
    .collectAsMap.toMap

    new Model(rules)
  }

  def predict(model: Model, query: Query): PredictedResult = {
    new PredictedResult(
      model.getSentiment(query.s, ap)
    )
  }
}
