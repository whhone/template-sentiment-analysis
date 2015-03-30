package org.template.sentimentanalysis

import io.prediction.controller.IEngineFactory
import io.prediction.controller.Engine

case class Query(
  s: String
) extends Serializable

case class PredictedResult(
  sentiment: Double
) extends Serializable

object SentimentAnalysisEngine extends IEngineFactory {
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("nlpparse" -> classOf[Algorithm]),
      classOf[Serving])
  }
}

