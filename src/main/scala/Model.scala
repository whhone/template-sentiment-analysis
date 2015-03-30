package org.template.sentimentanalysis

import edu.stanford.nlp.Magic._
import edu.stanford.nlp.trees.Tree

class Model (
  var rules: Map[String, Double]
) extends Serializable {

  /**
   * Return the sentiment in [-2 , 2] scale
   */
  def getWordSentiment(word: String): Double = {
    var score = rules.get(word.toLowerCase())
    if (score.isEmpty) {
      return 0.0
    } else {
      return score.get - 2.0
    }
  }

  /**
   * Parse the input to a tree structure. Calculate the sentiment from bottom
   * to the top.
   *
   * For a leaf node, it is always a word token. Use the sentiment
   * from the training data in this case. If the word did not appear in the
   * training data. Assume it is neutral.
   *
   * For a non-leaf node, calculate the sentiments of each of its children.
   * Determine whether the sentence is positive or negative by the number of
   * negative children. If it is odd, then assume the sentence is negative.
   */
  def getSentiment(s: String, ap: AlgorithmParams): Double = {
    var m = scala.collection.mutable.Map[Tree, Double]()
    var tree = s.parse
    var root = tree.preOrderNodeList().get(0)
    var post_order = tree.postOrderNodeList()
    var i = 0
    while (i < post_order.size()) {
      var cur = post_order.get(i)
      i = i + 1

      if (cur.isLeaf()) {
        m(cur) = getWordSentiment(cur.value)
      } else {
        var children = cur.children()
        var weight = 0.0000000001
        var positive = 1
        var sentiment = 0.0
        m(cur) = 0
        for (child <- children) {
          var child_sentiment = m(child)

          // The weight of a the child is proportional to the absolute value
          // of its sentiment. It avoid the sentiment to be neutralized by
          // other neutral childs
          var child_weight = Math.abs(child_sentiment) + ap.baseWeight

          weight = weight + child_weight
          sentiment = sentiment + child_weight * Math.abs(child_sentiment)
          if (child_sentiment < -0.0000000001) {
            positive = positive * -1
          }
        }
        m(cur) = ( sentiment / weight ) * positive
      }
    }

    return m(root) + 2.0
  }
}
