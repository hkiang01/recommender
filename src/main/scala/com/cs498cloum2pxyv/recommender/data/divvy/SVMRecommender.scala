package com.cs498cloum2pxyv.recommender.data.divvy

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.Vector
import org.apache.flink.ml.MLUtils
import org.apache.flink.ml.classification.SVM
import org.apache.flink.streaming.api.scala._

object SVMRecommender {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val trainingDS: DataSet[LabeledVector] = MLUtils.readLibSVM(env, "ML/input/train_feature_label.data")
    val testingDS: DataSet[Vector] = MLUtils.readLibSVM(env, "ML/input/test_feature.data").map(_.vector)

    val svm = SVM().setBlocks(10)
    svm.fit(trainingDS)
    val predictedRatings: DataSet[(Vector, Double)]= svm.predict(testingDS)
    predictedRatings.writeAsText("ML/svm_predicted_output")
    env.execute("Flink Recommendation App")
  }
}
