package org.template.sentimentanalysis

import io.prediction.controller.PAlgorithm
import io.prediction.controller.Params
import io.prediction.data.storage.BiMap

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import grizzled.slf4j.Logger

case class DummyAlgorithmParams(
)extends Params

class DummyAlgorithm(val ap: DummyAlgorithmParams)
  extends PAlgorithm[PreparedData, DummyModel, Query, PredictedResult] {

  @transient lazy val logger = Logger[this.type]

  def train(sc: SparkContext, data: PreparedData): DummyModel = {
    //require(!data.sentiments.take(1).isEmpty,
    //  s"RDD[sentiments] in PreparedData cannot be empty." +
    //  " Please check if DataSource generates TrainingData" +
    //  " and Preprator generates PreparedData correctly.")
    // do nothing in milestone 1
    new DummyModel()
  }
  
  def predict(model: DummyModel, query: Query): PredictedResult = {
    // always return 2.0 for milestone 1
    new PredictedResult(2.0)
  }
}
